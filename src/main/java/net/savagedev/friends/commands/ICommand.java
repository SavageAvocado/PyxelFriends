package net.savagedev.friends.commands;

import org.bukkit.command.CommandSender;

interface ICommand {
    void registerSubCommands();

    void execute(CommandSender user, String... args);
}
