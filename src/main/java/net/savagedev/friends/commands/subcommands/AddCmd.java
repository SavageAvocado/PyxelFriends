package net.savagedev.friends.commands.subcommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.savagedev.friends.PyxelFriends;
import net.savagedev.friends.friend.Friend;
import net.savagedev.friends.friend.FriendRequest;
import net.savagedev.friends.utils.HFUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class AddCmd extends SubCommand {
    public AddCmd(PyxelFriends plugin, Permission permission) {
        super(plugin, permission);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        if (!this.validatePermissions(user)) return;
        HFUser hfUser = this.getPlugin().getHfUserManager().getUser((Player) user);

        if (hfUser.hasReachedFriendLimit()) {
            for (String line : this.getPlugin().getConfig().getStringList("messages.max-friends"))
                this.getPlugin().getMessageUtil().message(user, line.replace("%0%", String.valueOf(hfUser.getFriendLimit())));
            return;
        }

        String potentialFriendsName;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("add")) {
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.invalid-arguments").replace("%0%", "friend add <name>"));
                return;
            }

            potentialFriendsName = args[0];
        } else
            potentialFriendsName = args[1];

        if (potentialFriendsName.length() > 16 || potentialFriendsName.replaceAll("[a-zA-Z0-9_]", "").length() > 0) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.invalid-input").replace("%0%", potentialFriendsName));
            return;
        }

        if (potentialFriendsName.equalsIgnoreCase(user.getName())) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.cannot-friend-yourself"));
            return;
        }

        OfflinePlayer potentialFriend;
        if ((potentialFriend = this.getPlugin().getServer().getOfflinePlayer(potentialFriendsName)) == null || (!potentialFriend.hasPlayedBefore() || !potentialFriend.isOnline())) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.player-not-found").replace("%0%", potentialFriendsName));
            return;
        }

        for (Friend friend : this.getPlugin().getHfUserManager().getUser((Player) user).getFriendList().getAllFriends()) {
            if (friend.getUniqueId().equals(potentialFriend.getUniqueId())) {
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.already-friends"));
                return;
            }
        }

        if (this.getPlugin().getStorageUtil().getFile(potentialFriend.getUniqueId().toString()).getBoolean("friends-list-full")) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.request-not-allowed"));
            return;
        }

        if (!this.getPlugin().getStorageUtil().getFile(potentialFriend.getUniqueId().toString()).getBoolean("preferences.allow-friend-requests")) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.request-not-allowed"));
            return;
        }

        this.getPlugin().getFriendRequestManager().addRequest(new FriendRequest(((Player) user).getUniqueId(), potentialFriend.getUniqueId(), this.getPlugin().getConfig().getInt("options.friend-add-timeout")));

        if (potentialFriend.isOnline()) {
            for (String line : this.getPlugin().getConfig().getStringList("messages.friend-request-header"))
                this.getPlugin().getMessageUtil().message(Bukkit.getPlayer(potentialFriend.getUniqueId()), line.replace("%0%", ((Player) user).getDisplayName()));

            TextComponent optionsMessage = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.getPlugin().getConfig().getString("messages.friend-request-options")));

            TextComponent optionAccept = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.getPlugin().getConfig().getString("messages.request-option-accept")));
            ClickEvent clickEventAccept = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f accept " + user.getName());
            HoverEvent hoverEventAccept = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&bClick to accept the friend request")).create());

            optionAccept.setClickEvent(clickEventAccept);
            optionAccept.setHoverEvent(hoverEventAccept);

            TextComponent optionDeny = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.getPlugin().getConfig().getString("messages.request-option-deny")));
            ClickEvent clickEventDeny = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f deny " + user.getName());
            HoverEvent hoverEventDeny = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&bClick to deny the friend request")).create());

            optionDeny.setClickEvent(clickEventDeny);
            optionDeny.setHoverEvent(hoverEventDeny);

            TextComponent spacer = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.getPlugin().getConfig().getString("messages.spacer")));

            this.getPlugin().getMessageUtil().message(Bukkit.getPlayer(potentialFriend.getUniqueId()), new TextComponent(optionsMessage, optionAccept, spacer, optionDeny));

            this.getPlugin().getMessageUtil().message(Bukkit.getPlayer(potentialFriend.getUniqueId()), this.getPlugin().getConfig().getStringList("messages.friend-request-footer"));
        }

        for (String line : this.getPlugin().getConfig().getStringList("messages.request-sent"))
            this.getPlugin().getMessageUtil().message(user, line.replace("%0%", potentialFriend.getName()).replace("%1%", String.valueOf(this.getPlugin().getConfig().getInt("options.friend-add-timeout"))));
    }
}
