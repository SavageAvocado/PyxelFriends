package net.savagedev.hf.commands;

import org.bukkit.command.CommandSender;

interface ICommand {
    void registerSubCommands();

    void execute(CommandSender user, String... args);
}
