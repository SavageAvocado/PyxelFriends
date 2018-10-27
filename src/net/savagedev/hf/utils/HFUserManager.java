package net.savagedev.hf.utils;

import net.savagedev.hf.HypixelFriends;
import net.savagedev.hf.friend.Friend;
import net.savagedev.hf.friend.FriendList;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class HFUserManager {
    private Map<UUID, HFUser> users;
    private HypixelFriends plugin;

    public HFUserManager(HypixelFriends plugin) {
        this.plugin = plugin;
        this.init();
    }

    private void init() {
        this.users = new HashMap<>();

        if (!this.plugin.getServer().getOnlinePlayers().isEmpty())
            for (Player user : this.plugin.getServer().getOnlinePlayers())
                this.cacheUser(user);
    }

    public void saveUser(OfflinePlayer user) {
        FileConfiguration storage = this.plugin.getStorageUtil().getFile(user.getUniqueId().toString());
        List<String> friends = new ArrayList<>();

        for (Friend friend : this.getUser((Player) user).getFriendList().getAllFriends())
            friends.add(friend.getUniqueId().toString());

        storage.set("preferences.allow-friend-requests", this.getUser((Player) user).isAllowingRequests());
        storage.set("friends", friends);

        this.plugin.getStorageUtil().saveFile(storage, user.getUniqueId().toString());
    }

    public void cacheUser(Player user) {
        if (!this.plugin.getStorageUtil().fileExists(user.getUniqueId().toString())) {
            this.plugin.getStorageUtil().createFile(user.getUniqueId().toString());

            FileConfiguration config = this.plugin.getStorageUtil().getFile(user.getUniqueId().toString());

            config.set("preferences.allow-friend-requests", true);
            config.set("friends-list-full", false);
            config.set("friends", new ArrayList<>());

            this.plugin.getStorageUtil().saveFile(config, user.getUniqueId().toString());
        }

        FileConfiguration config = this.plugin.getStorageUtil().getFile(user.getUniqueId().toString());
        List<Friend> friendsList = new ArrayList<>();
        int friendLimit = this.plugin.getConfig().getInt("options.max-friends.default");

        if (friendsList.size() > friendLimit)
            config.set("friends-list-full", true);

        for (String uuid : config.getStringList("friends"))
            friendsList.add(new Friend(UUID.fromString(uuid)));

        FriendList friendList = new FriendList(friendsList);

        boolean allowingRequests = config.getBoolean("preferences.allow-friend-requests");

        for (String perm : this.plugin.getConfig().getConfigurationSection("options.max-friends").getKeys(false))
            if (user.hasPermission("hypixelfriends.limit." + perm))
                friendLimit = this.plugin.getConfig().getInt("options.max-friends." + perm);

        this.users.put(user.getUniqueId(), new HFUser(friendList, allowingRequests, friendLimit));
    }

    public void uncacheUser(Player user) {
        this.saveUser(user);

        this.users.remove(user.getUniqueId());
    }

    public HFUser getUser(Player user) {
        return this.users.get(user.getUniqueId());
    }
}
