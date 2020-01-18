package com.github.mamizu03123.itemlostcompensation;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class ItemLostCompensation extends JavaPlugin {
    public String prefix = "[ItemLostCompensation]";
    public MySQLManager sql;
    public ConfigManager config;
    public String HOST;
    public String DB;
    public String USER;
    public String PASS;
    public String PORT;
    public List<UUID> onYNFirst = new ArrayList<>();
    public HashMap<UUID, UUID> onOpeningItemList = new HashMap<>();
    public EventManager event;

    @Override
    public void onEnable() {
        getLogger().info("ItenLostCompensation Enabled.");
        saveDefaultConfig();
        config = new ConfigManager(this);
        config.load();
        sql = new MySQLManager(this, "ItemLostCompensation");
        event = new EventManager(this);
        getServer().getPluginManager().registerEvents(event, this);
        getCommand("ilc").setExecutor(new CommandManager(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void registerPlayer(Player p) {
        sql.execute("INSERT INTO PLAYERDATA VALUE ('"+p.getName()+"' , '"+p.getUniqueId()+"', NULL, NULL, NULL");
    }
}
