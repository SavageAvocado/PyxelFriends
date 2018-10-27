package net.savagedev.hf.commands.subcommands;

import org.bukkit.command.CommandSender;

interface ISubCommand {
    void execute(CommandSender user, String... args);
}
