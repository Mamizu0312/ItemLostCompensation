package com.github.mamizu03123.itemlostcompensation;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ItemLostCompensation extends JavaPlugin {
    public static String prefix = "[ItemLostCompensation]";
    public MySQLManager sql;
    public ConfigManager config;
    public CommandManager cmd;
    public String HOST;
    public String DB;
    public String USER;
    public String PASS;
    public String PORT;
    public List<UUID> onChoosingItem = new ArrayList<UUID>();
    @Override
    public void onEnable() {
        getLogger().info("ItenLostCompensation Enabled.");
        sql = new MySQLManager(this, "ItemLostCompensatiopn");
        getCommand("ilc").setExecutor(new CommandManager(this, sql));
        config = new ConfigManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
