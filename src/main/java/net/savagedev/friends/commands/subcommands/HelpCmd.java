package net.savagedev.friends.commands.subcommands;

import net.savagedev.friends.PyxelFriends;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class HelpCmd extends SubCommand {
    public HelpCmd(PyxelFriends plugin, Permission permission) {
        super(plugin, permission);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        if (!this.validatePermissions(user)) return;

        this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.help"));
    }
}
