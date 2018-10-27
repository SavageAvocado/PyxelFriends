package net.savagedev.hf.commands.subcommands;

import net.savagedev.hf.HypixelFriends;
import net.savagedev.hf.friend.Friend;
import net.savagedev.hf.friend.FriendRequest;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class AcceptCmd extends SubCommand {
    public AcceptCmd(HypixelFriends plugin, Permission permission) {
        super(plugin, permission);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        if (!this.validatePermissions(user)) return;

        if (args.length == 1) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.invalid-arguments").replace("%0%", "friend accept <name>"));
            return;
        }

        if (args[1].length() > 16 || args[1].replaceAll("[a-zA-Z0-9_]", "").length() > 0) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.invalid-input").replace("%0%", args[1]));
            return;
        }

        OfflinePlayer potentialFriend;
        if ((potentialFriend = this.getPlugin().getServer().getOfflinePlayer(args[1])) == null || (!potentialFriend.hasPlayedBefore() || !potentialFriend.isOnline())) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.player-not-found").replace("%0%", args[1]));
            return;
        }

        for (Friend friend : this.getPlugin().getHfUserManager().getUser((Player) user).getFriendList().getAllFriends()) {
            if (friend.getUniqueId().equals(potentialFriend.getUniqueId())) {
                this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.already-friends"));
                return;
            }
        }

        if (this.getPlugin().getFriendRequestManager().getFriendRequests(((Player) user).getUniqueId()).isEmpty()) {
            for (String line : this.getPlugin().getConfig().getStringList("messages.no-request"))
                this.getPlugin().getMessageUtil().message(user, line.replace("%0%", Bukkit.getOfflinePlayer(args[1]).isOnline() ? Bukkit.getPlayer(args[1]).getDisplayName() : Bukkit.getOfflinePlayer(args[1]).getName()));

            return;
        }

        FriendRequest request = null;
        for (FriendRequest request1 : this.getPlugin().getFriendRequestManager().getFriendRequests(((Player) user).getUniqueId()))
            if (request1.getSender().equals(potentialFriend))
                request = request1;

        if (request == null) {
            for (String line : this.getPlugin().getConfig().getStringList("messages.no-request"))
                this.getPlugin().getMessageUtil().message(user, line.replace("%0%", potentialFriend.isOnline() ? Bukkit.getPlayer(args[1]).getDisplayName() : potentialFriend.getName()));

            return;
        }

        request.accept(this.getPlugin());

        for (String line : this.getPlugin().getConfig().getStringList("messages.request-accepted"))
            this.getPlugin().getMessageUtil().message(user, line.replace("%0%", potentialFriend.isOnline() ? Bukkit.getPlayer(args[1]).getDisplayName() : potentialFriend.getName()));
    }
}
