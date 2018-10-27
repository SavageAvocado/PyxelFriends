package net.savagedev.hf.commands.subcommands;

import net.savagedev.hf.HypixelFriends;
import net.savagedev.hf.friend.Friend;
import net.savagedev.hf.utils.HFUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.List;

public class RemoveCmd extends SubCommand {
    public RemoveCmd(HypixelFriends plugin, Permission permission) {
        super(plugin, permission);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        if (!this.validatePermissions(user)) return;
        HFUser hfUser = this.getPlugin().getHfUserManager().getUser((Player) user);

        if (args.length == 1) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.invalid-arguments").replace("%0%", "friend remove <name>"));
            return;
        }

        if (args[1].length() > 16 || args[1].replaceAll("[a-zA-Z0-9_]", "").length() > 0) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.invalid-input").replace("%0%", args[1]));
            return;
        }

        OfflinePlayer oldFriend;
        if ((oldFriend = Bukkit.getOfflinePlayer(args[1])) == null || !oldFriend.hasPlayedBefore()) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getString("messages.player-not-found").replace("%0%", args[1]));
            return;
        }

        for (Friend friend : hfUser.getFriendList().getAllFriends()) {
            if (!friend.getUniqueId().equals(oldFriend.getUniqueId()))
                continue;

            hfUser.getFriendList().removeFriend(oldFriend);
            this.getPlugin().getHfUserManager().saveUser((OfflinePlayer) user);

            if (oldFriend.isOnline()) {
                this.getPlugin().getHfUserManager().getUser((Player) oldFriend).getFriendList().removeFriend((OfflinePlayer) user);
                this.getPlugin().getHfUserManager().saveUser(oldFriend);

                for (String line : this.getPlugin().getConfig().getStringList("messages.unfriend-receiver"))
                    this.getPlugin().getMessageUtil().message((Player) oldFriend, line.replace("%0%", ((Player) user).getDisplayName()));
            }

            FileConfiguration storage = this.getPlugin().getStorageUtil().getFile(oldFriend.getUniqueId().toString());
            List<String> friends = storage.getStringList("friends");
            friends.remove(((Player) user).getUniqueId().toString());

            storage.set("friends", friends);
            this.getPlugin().getStorageUtil().saveFile(storage, oldFriend.getUniqueId().toString());

            for (String line : this.getPlugin().getConfig().getStringList("messages.unfriend-sender"))
                this.getPlugin().getMessageUtil().message(user, line.replace("%0%", oldFriend.isOnline() ? Bukkit.getPlayer(oldFriend.getUniqueId()).getDisplayName() : oldFriend.getName()));

            return;
        }

        for (String line : this.getPlugin().getConfig().getStringList("messages.not-friends"))
            this.getPlugin().getMessageUtil().message(user, line.replace("%0%", oldFriend.isOnline() ? Bukkit.getPlayer(oldFriend.getUniqueId()).getDisplayName() : oldFriend.getName()));
    }
}
