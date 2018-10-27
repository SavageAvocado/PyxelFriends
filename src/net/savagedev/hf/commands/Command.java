package net.savagedev.hf.commands;

import net.savagedev.hf.HypixelFriends;
import net.savagedev.hf.commands.subcommands.SubCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

abstract class Command implements CommandExecutor, ICommand {
    private Map<String, SubCommand> subCommands;
    private HypixelFriends plugin;
    private CommandSender user;
    private String[] args;

    Command(HypixelFriends plugin) {
        this.subCommands = new HashMap<>();
        this.plugin = plugin;

        this.registerSubCommands();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        this.user = commandSender;
        this.args = strings;

        this.execute(this.user, this.args);
        return true;
    }

    void addSubCommand(String name, SubCommand subCommand) {
        this.subCommands.put(name, subCommand);
    }

    void executeSubCommand(String name) {
        if (!this.getSubCommands().containsKey(name)) {
            this.getSubCommands().get("add").execute(this.user, this.args);
            return;
        }

        this.getSubCommands().get(name).execute(this.user, this.args);
    }

    private Map<String, SubCommand> getSubCommands() {
        return this.subCommands;
    }

    HypixelFriends getPlugin() {
        return this.plugin;
    }
}
