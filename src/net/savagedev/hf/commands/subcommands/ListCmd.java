package net.savagedev.hf.commands.subcommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.savagedev.hf.HypixelFriends;
import net.savagedev.hf.friend.Friend;
import net.savagedev.hf.utils.HFUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class ListCmd extends SubCommand {
    public ListCmd(HypixelFriends plugin, Permission permission) {
        super(plugin, permission);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        if (!this.validatePermissions(user)) return;
        HFUser hfUser = this.getPlugin().getHfUserManager().getUser((Player) user);
        int page = 1;

        if (args.length > 1) {
            if (this.getPlugin().isInteger(args[1]) && Integer.valueOf(args[1]) <= Math.round(((float) hfUser.getFriendList().getFriendsListSize() / 8) + .375))
                page = Integer.valueOf(args[1]);
            else {
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.invalid-page-number"));
                return;
            }
        }

        TextComponent last = new TextComponent(ChatColor.translateAlternateColorCodes('&', "                " + this.getPlugin().getConfig().getString("messages.last-page")));
        TextComponent next = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.getPlugin().getConfig().getString("messages.next-page")));

        HoverEvent nextHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&cClick to view page " + String.valueOf(Integer.valueOf(page + 1)))).create());
        ClickEvent nextClickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f list " + String.valueOf(Integer.valueOf(page + 1)));

        HoverEvent lastHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&cClick to view page " + String.valueOf(Integer.valueOf(page - 1)))).create());
        ClickEvent lastClickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f list " + String.valueOf(Integer.valueOf(page - 1)));

        next.setHoverEvent(nextHoverEvent);
        next.setClickEvent(nextClickEvent);

        last.setHoverEvent(lastHoverEvent);
        last.setClickEvent(lastClickEvent);

        TextComponent headerPage = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.getPlugin().getConfig().getString("messages.list-page").replace("%1%", String.valueOf(page)).replace("%2%", String.valueOf(Math.round(((float) hfUser.getFriendList().getFriendsListSize() / 8) + .375) < 1 ? 1 : Math.round(((float) hfUser.getFriendList().getFriendsListSize() / 8) + .375)))));

        for (String line : this.getPlugin().getConfig().getStringList("messages.list-header"))
            this.getPlugin().getMessageUtil().message(user, line);

        this.getPlugin().getMessageUtil().message((Player) user, new TextComponent(page > 1 ? last : new TextComponent("                 "), headerPage, next));

        if (hfUser.getFriendList().getFriendsListSize() <= 0)
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.no-friends"));
        else
            for (Friend friend : hfUser.getFriendList().getFriends(page))
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.list-format").replace("%0%", friend.getName()).replace("%1%", friend.isOnline() ? this.getPlugin().getConfig().getString("messages.list-status.online").replace("%0%", Bukkit.getPlayer(friend.getUniqueId()).getLocation().getWorld().getName()) : this.getPlugin().getConfig().getString("messages.list-status.offline")));

        this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.list-footer"));
    }
}
