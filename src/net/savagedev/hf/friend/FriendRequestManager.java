package net.savagedev.hf.friend;

import net.savagedev.hf.HypixelFriends;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FriendRequestManager {
    private Map<UUID, FriendRequest> friendRequests;
    private HypixelFriends plugin;

    public FriendRequestManager(HypixelFriends plugin) {
        this.friendRequests = new HashMap<>();
        this.plugin = plugin;

        this.startExpiryCheckThread();
    }

    private void startExpiryCheckThread() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : friendRequests.keySet()) {
                    if (friendRequests.get(uuid).isExpired()) {
                        if (friendRequests.get(uuid).getSender().isOnline()) {
                            Player user = (Player) friendRequests.get(uuid).getSender();
                            for (String line : plugin.getConfig().getStringList("messages.request-expired-sender"))
                                plugin.getMessageUtil().message(user, line.replace("%0%", friendRequests.get(uuid).getRequested().getName()));
                        }

                        if (friendRequests.get(uuid).getRequested().isOnline()) {
                            Player user = (Player) friendRequests.get(uuid).getRequested();
                            for (String line : plugin.getConfig().getStringList("messages.request-expired-receiver"))
                                plugin.getMessageUtil().message(user, line.replace("%0%", friendRequests.get(uuid).getSender().getName()));
                        }

                        removeRequest(uuid, friendRequests.get(uuid));
                    }
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 0L, 20L);
    }

    public void addRequest(FriendRequest request) {
        this.friendRequests.put(request.getRequested().getUniqueId(), request);
    }

    void removeRequest(UUID uuid, FriendRequest request) {
        this.friendRequests.remove(uuid, request);
    }

    public List<FriendRequest> getFriendRequests(UUID uuid) {
        List<FriendRequest> requests = new ArrayList<>();

        if (this.friendRequests.isEmpty())
            return requests;

        for (UUID uuid1 : this.friendRequests.keySet())
            if (uuid1.equals(uuid))
                requests.add(this.friendRequests.get(uuid1));

        return requests;
    }
}
