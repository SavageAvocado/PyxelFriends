package net.savagedev.hf.commands.subcommands;

import net.savagedev.hf.HypixelFriends;
import net.savagedev.hf.utils.HFUser;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class ToggleCmd extends SubCommand {
    public ToggleCmd(HypixelFriends plugin, Permission permission) {
        super(plugin, permission);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        if (!this.validatePermissions(user)) return;
        HFUser hfUser = this.getPlugin().getHfUserManager().getUser((Player) user);

        hfUser.toggleRequests();
        this.getPlugin().getHfUserManager().saveUser((OfflinePlayer) user);

        for (String line : this.getPlugin().getConfig().getStringList("messages.toggle"))
            this.getPlugin().getMessageUtil().message(user, line.replace("%0%", hfUser.isAllowingRequests() ? "enabled" : "disabled"));
    }
}
