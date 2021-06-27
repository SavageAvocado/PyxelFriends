package net.savagedev.friends.commands;

import net.savagedev.friends.PyxelFriends;
import net.savagedev.friends.commands.subcommands.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class FriendCmd extends Command {
    public FriendCmd(PyxelFriends plugin) {
        super(plugin);
    }

    @Override
    public void registerSubCommands() {
        this.addSubCommand("accept", new AcceptCmd(this.getPlugin(), new Permission("hypixelfriends.accept")));
        this.addSubCommand("add", new AddCmd(this.getPlugin(), new Permission("hypixelfriends.add")));
        this.addSubCommand("deny", new DenyCmd(this.getPlugin(), new Permission("hypixelfriends.deny")));
        this.addSubCommand("help", new HelpCmd(this.getPlugin(), new Permission("hypixelfriends.help")));
        this.addSubCommand("list", new ListCmd(this.getPlugin(), new Permission("hypixelfriends.list")));
        this.addSubCommand("removeall", new RemoveAllCmd(this.getPlugin(), new Permission("hypixelfriends.removeall")));
        this.addSubCommand("remove", new RemoveCmd(this.getPlugin(), new Permission("hypixelfriends.remove")));
        this.addSubCommand("requests", new RequestsCmd(this.getPlugin(), new Permission("hypixelfriends.requests")));
        this.addSubCommand("toggle", new ToggleCmd(this.getPlugin(), new Permission("hypixelfriends.toggle")));
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        if (!(sender instanceof Player)) return;

        if (args.length == 0) {
            this.executeSubCommand("help");
            return;
        }

        this.executeSubCommand(args[0]);
    }
}
