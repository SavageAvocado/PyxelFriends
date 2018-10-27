package net.savagedev.hf.friend;

import net.savagedev.hf.HypixelFriends;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class FriendRequest {
    private int expireTime;
    private UUID requested;
    private Long sent;
    private UUID uuid;

    public FriendRequest(UUID uuid, UUID requested, int expireTime) {
        this.sent = System.currentTimeMillis();
        this.expireTime = expireTime;
        this.requested = requested;
        this.uuid = uuid;
    }

    public void accept(HypixelFriends plugin) {
        plugin.getFriendRequestManager().removeRequest(this.getRequested().getUniqueId(), this);

        if (plugin.getStorageUtil().getFile(this.getSender().getUniqueId().toString()).getBoolean("friends-list-full")) {
            plugin.getMessageUtil().message((Player) this.getRequested(), plugin.getConfig().getStringList("messages.request-not-allowed"));

            if (this.getSender().isOnline())
                for (String line : plugin.getConfig().getStringList("messages.max-friends"))
                    plugin.getMessageUtil().message((Player) this.getSender(), line.replace("%0%", String.valueOf(plugin.getHfUserManager().getUser((Player) this.getSender()).getFriendLimit())));

            return;
        }

        plugin.getHfUserManager().getUser((Player) this.getRequested()).getFriendList().addFriend(new Friend(this.uuid));
        plugin.getHfUserManager().saveUser(this.getRequested());

        if (this.getSender().isOnline()) {
            Player user = (Player) this.getSender();
            for (String line : plugin.getConfig().getStringList("messages.request-accepted"))
                plugin.getMessageUtil().message(user, line.replace("%0%", ((Player) this.getRequested()).getDisplayName()));

            plugin.getHfUserManager().getUser(user).getFriendList().addFriend(new Friend(this.requested));
            plugin.getHfUserManager().saveUser(user);
            return;
        }

        FileConfiguration storage = plugin.getStorageUtil().getFile(this.uuid.toString());
        List<String> friends = storage.getStringList("friends");
        friends.add(this.requested.toString());

        storage.set("friends", friends);
        plugin.getStorageUtil().saveFile(storage, this.uuid.toString());
    }

    public void deny(HypixelFriends plugin) {
        plugin.getFriendRequestManager().removeRequest(this.getRequested().getUniqueId(), this);
    }

    public OfflinePlayer getSender() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }

    OfflinePlayer getRequested() {
        return Bukkit.getOfflinePlayer(this.requested);
    }

    public int getRemainingTime() {
        return (int) ((this.expireTime * 60) - ((System.currentTimeMillis() / 1000) - (this.sent / 1000))) / 60;
    }

    boolean isExpired() {
        return ((System.currentTimeMillis() / 1000) - (this.sent / 1000)) >= this.expireTime * 60;
    }
}
