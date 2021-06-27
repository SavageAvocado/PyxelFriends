package net.savagedev.friends.listeners;

import net.savagedev.friends.PyxelFriends;
import net.savagedev.friends.friend.Friend;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinE implements Listener {
    private PyxelFriends plugin;

    public JoinE(PyxelFriends plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoinE(PlayerJoinEvent e) {
        Player user = e.getPlayer();

        this.plugin.getHfUserManager().cacheUser(user);

        if (!this.plugin.getFriendRequestManager().getFriendRequests(user.getUniqueId()).isEmpty())
            for (String line : this.plugin.getConfig().getStringList("messages.pending-requests"))
            this.plugin.getMessageUtil().message(user, line.replace("%0%", String.valueOf(this.plugin.getFriendRequestManager().getFriendRequests(user.getUniqueId()).size())));

        if (this.plugin.getServer().getOnlinePlayers().isEmpty())
            return;

        for (Friend friendUUID : this.plugin.getHfUserManager().getUser(user).getFriendList().getAllFriends())
            if (this.plugin.getServer().getOfflinePlayer(friendUUID.getUniqueId()).isOnline())
                this.plugin.getMessageUtil().message(this.plugin.getServer().getPlayer(friendUUID.getUniqueId()), this.plugin.getConfig().getString("messages.join").replace("%0%", user.getName()));
    }
}
