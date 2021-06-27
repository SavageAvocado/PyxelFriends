package net.savagedev.friends.utils;

import net.savagedev.friends.PyxelFriends;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class StorageUtil {
    private PyxelFriends plugin;

    public StorageUtil(PyxelFriends plugin) {
        this.plugin = plugin;
    }

    void createFile(String name) {
        File file = new File(this.plugin.getDataFolder(), "data/" + name + ".yml");

        if (file.exists()) return;

        file.getParentFile().mkdirs();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile(FileConfiguration config, String name) {
        try {
            config.save(new File(this.plugin.getDataFolder(), "data/" + name + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean fileExists(String name) {
        return new File(this.plugin.getDataFolder(), "data/" + name + ".yml").exists();
    }

    public FileConfiguration getFile(String name) {
        return YamlConfiguration.loadConfiguration(new File(this.plugin.getDataFolder(), "data/" + name + ".yml"));
    }
}
