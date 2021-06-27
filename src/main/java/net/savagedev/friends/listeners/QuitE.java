package net.savagedev.friends.listeners;

import net.savagedev.friends.PyxelFriends;
import net.savagedev.friends.friend.Friend;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitE implements Listener {
    private PyxelFriends plugin;

    public QuitE(PyxelFriends plugin) {
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
