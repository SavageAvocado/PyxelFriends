package net.savagedev.hf.commands.subcommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.savagedev.hf.HypixelFriends;
import net.savagedev.hf.friend.FriendRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class RequestsCmd extends SubCommand {
    public RequestsCmd(HypixelFriends plugin, Permission permission) {
        super(plugin, permission);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        if (!this.validatePermissions(user)) return;

        for (String line : this.getPlugin().getConfig().getStringList("messages.requests"))
            this.getPlugin().getMessageUtil().message(user, line.replace("%0%", String.valueOf(1)).replace("%1%", String.valueOf(1)));

        for (FriendRequest request : this.getPlugin().getFriendRequestManager().getFriendRequests(((Player) user).getUniqueId())) {
            TextComponent username = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&eFrom " + (request.getSender().isOnline() ? Bukkit.getPlayer(request.getSender().getUniqueId()).getDisplayName() : request.getSender().getName())));

            TextComponent optionAccept = new TextComponent(ChatColor.translateAlternateColorCodes('&', " " + this.getPlugin().getConfig().getString("messages.request-option-accept")));
            ClickEvent clickEventAccept = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f accept " + request.getSender().getName());
            HoverEvent hoverEventAccept = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&bClick to accept the friend request")).create());

            optionAccept.setClickEvent(clickEventAccept);
            optionAccept.setHoverEvent(hoverEventAccept);

            TextComponent optionDeny = new TextComponent(ChatColor.translateAlternateColorCodes('&', " " + this.getPlugin().getConfig().getString("messages.request-option-deny")));
            ClickEvent clickEventDeny = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f deny " + request.getSender().getName());
            HoverEvent hoverEventDeny = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&bClick to deny the friend request")).create());

            optionDeny.setClickEvent(clickEventDeny);
            optionDeny.setHoverEvent(hoverEventDeny);

            this.getPlugin().getMessageUtil().message((Player) user, new TextComponent(username, optionAccept, optionDeny));
        }
    }
}
