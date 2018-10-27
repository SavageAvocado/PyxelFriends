package net.savagedev.hf.listeners;

import net.savagedev.hf.HypixelFriends;
import net.savagedev.hf.friend.Friend;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitE implements Listener {
    private HypixelFriends plugin;

    public QuitE(HypixelFriends plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuitE(PlayerQuitEvent e) {
        Player user = e.getPlayer();

        for (Friend friendUUID : this.plugin.getHfUserManager().getUser(user).getFriendList().getAllFriends())
            if (this.plugin.getServer().getOfflinePlayer(friendUUID.getUniqueId()).isOnline())
                this.plugin.getMessageUtil().message(this.plugin.getServer().getPlayer(friendUUID.getUniqueId()), this.plugin.getConfig().getString("messages.quit").replace("%0%", user.getName()));

        this.plugin.getHfUserManager().uncacheUser(user);
    }
}
