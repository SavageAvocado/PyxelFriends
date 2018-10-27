package net.savagedev.hf.commands.subcommands;

import net.savagedev.hf.HypixelFriends;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class HelpCmd extends SubCommand {
    public HelpCmd(HypixelFriends plugin, Permission permission) {
        super(plugin, permission);
    }

    @Override
    public void execute(CommandSender user, String... args) {
        if (!this.validatePermissions(user)) return;

        this.getPlugin().getMessageUtil().message(user, this.getPlugin().getConfig().getStringList("messages.help"));
    }
}
