package com.github.mamizu03123.itemlostcompensation;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private FileConfiguration config = null;
    private ItemLostCompensation plugin;
    public ConfigManager(ItemLostCompensation plugin) {
        this.plugin = plugin;
    }
    public void load() {
        plugin.DB = plugin.getConfig().getString("mysql.db");
        plugin.HOST = plugin.getConfig().getString("mysql.host");
        plugin.USER = plugin.getConfig().getString("mysql.user");
        plugin.PASS = plugin.getConfig().getString("mysql.pass");
        plugin.PORT = plugin.getConfig().getString("mysql.port");
    }
}
