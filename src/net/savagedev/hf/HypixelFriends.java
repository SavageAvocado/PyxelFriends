package net.savagedev.hf;

import net.savagedev.hf.commands.FriendCmd;
import net.savagedev.hf.friend.FriendRequestManager;
import net.savagedev.hf.listeners.JoinE;
import net.savagedev.hf.listeners.QuitE;
import net.savagedev.hf.utils.HFUserManager;
import net.savagedev.hf.utils.MessageUtil;
import net.savagedev.hf.utils.StorageUtil;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HypixelFriends extends JavaPlugin {
    private FriendRequestManager friendRequestManager;
    private HFUserManager hfUserManager;
    private MessageUtil messageUtil;
    private StorageUtil storageUtil;

    @Override
    public void onEnable() {
        this.loadUtils();
        this.loadConfig();
        this.loadCommands();
        this.loadListeners();
    }

    private void loadUtils() {
        this.friendRequestManager = new FriendRequestManager(this);
        this.storageUtil = new StorageUtil(this);
        this.hfUserManager = new HFUserManager(this);
        this.messageUtil = new MessageUtil();
    }

    private void loadConfig() {
        this.saveDefaultConfig();
    }

    private void loadCommands() {
        this.getCommand("friend").setExecutor(new FriendCmd(this));
    }

    private void loadListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new JoinE(this), this);
        pluginManager.registerEvents(new QuitE(this), this);
    }

    public FriendRequestManager getFriendRequestManager() {
        return this.friendRequestManager;
    }

    public HFUserManager getHfUserManager() {
        return this.hfUserManager;
    }

    public boolean isInteger(String potentialInt) {
        try {
            Integer.parseInt(potentialInt);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public MessageUtil getMessageUtil() {
        return this.messageUtil;
    }

    public StorageUtil getStorageUtil() {
        return this.storageUtil;
    }
}
