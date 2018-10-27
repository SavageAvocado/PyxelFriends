package net.savagedev.hf.commands.subcommands;

import net.savagedev.hf.HypixelFriends;
import net.savagedev.hf.utils.HFUser;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class RemoveAllCmd extends SubCommand {
    public RemoveAllCmd(HypixelFriends plugin, Permission permission) {
        super(plugin, permission);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        if (!this.validatePermissions(user)) return;
        HFUser hfUser = this.getPlugin().getHfUserManager().getUser((Player) user);

        if (hfUser.getFriendList().getFriendsListSize() <= 0) {
            this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.friends-list-already-empty"));
            return;
        }

        hfUser.getFriendList().clear();
        this.getPlugin().getHfUserManager().saveUser((OfflinePlayer) user);
        this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.friends-list-cleared"));
    }
}
