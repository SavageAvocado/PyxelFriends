package net.savagedev.friends.commands.subcommands;

import net.savagedev.friends.PyxelFriends;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public abstract class SubCommand implements ISubCommand {
    private PyxelFriends plugin;
    private Permission permission;

    public SubCommand(PyxelFriends plugin, Permission permission) {
        this.permission = permission;
        this.plugin = plugin;
    }

    boolean validatePermissions(CommandSender user) {
        if (!user.hasPermission(this.getPermission())) {
            this.plugin.getMessageUtil().message(user, this.plugin.getConfig().getString("messages.no-permission"));
            return false;
        }

        return true;
    }

    private Permission getPermission() {
        return this.permission;
    }

    PyxelFriends getPlugin() {
        return this.plugin;
    }
}
