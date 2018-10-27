package net.savagedev.hf.friend;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class FriendList {
    private Queue<Friend> friends;

    public FriendList(List<Friend> friends) {
        this.friends = new ConcurrentLinkedDeque<>(friends);
    }

    public int getFriendsListSize() {
        return this.friends.size();
    }

    public boolean contains(Player user) {
        for (Friend friend : this.friends)
            return friend.getUniqueId().equals(user.getUniqueId());

        return false;
    }

    void addFriend(Friend friend) {
        this.friends.add(friend);
    }

    public void removeFriend(OfflinePlayer player) {
        for (Friend friend : this.friends)
            if (friend.getUniqueId().equals(player.getUniqueId()))
                this.friends.remove(friend);
    }

    public void clear() {
        this.friends.clear();
    }

    public List<Friend> getAllFriends() {
        return new ArrayList<>(this.friends);
    }

    public List<Friend> getFriends(int page) {
        List<Friend> friendsTemp = new ArrayList<>(this.friends);
        List<Friend> friends = new ArrayList<>();

        if (this.friends.isEmpty())
            return friends;

        int pageSize = 8;
        for (int i = ((page - 1) * pageSize) > this.friends.size() ? 0 : (page - 1) * pageSize; i < ((pageSize * page) > this.friends.size() ? this.friends.size() : pageSize * page); i++)
            friends.add(friendsTemp.get(i));

        return friends;
    }
}
