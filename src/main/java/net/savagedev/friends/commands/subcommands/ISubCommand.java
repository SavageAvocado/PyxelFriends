package net.savagedev.friends.commands.subcommands;

import org.bukkit.command.CommandSender;

interface ISubCommand {
    void execute(CommandSender user, String... args);
}
