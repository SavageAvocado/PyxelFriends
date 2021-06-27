package net.savagedev.friends.friend;

import org.bukkit.Bukkit;

import java.util.UUID;

public class Friend {
    private UUID uuid;

    public Friend(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.isOnline() ? Bukkit.getPlayer(this.uuid).getDisplayName() : Bukkit.getOfflinePlayer(this.uuid).getName();
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public boolean isOnline() {
        return Bukkit.getOfflinePlayer(this.uuid).isOnline();
    }
}
