package net.savagedev.hf.utils;

import net.savagedev.hf.friend.FriendList;

public class HFUser {
    private boolean allowingRequests;
    private FriendList friendList;
    private int friendLimit;

    HFUser(FriendList friendList, boolean allowingRequests, int friendLimit) {
        this.allowingRequests = allowingRequests;
        this.friendLimit = friendLimit;
        this.friendList = friendList;
    }

    public void toggleRequests() {
        this.allowingRequests = !this.allowingRequests;
    }

    public boolean hasReachedFriendLimit() {
        return this.getFriendList().getFriendsListSize() >= this.friendLimit;
    }

    public boolean isAllowingRequests() {
        return this.allowingRequests;
    }

    public FriendList getFriendList() {
        return this.friendList;
    }

    public int getFriendLimit() {
        return this.friendLimit;
    }
}
