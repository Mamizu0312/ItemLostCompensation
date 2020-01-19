package com.github.mamizu03123.itemlostcompensation;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
    public HashMap<UUID, UUID> onOpeningItemList = new HashMap<>(); //key: commandrunplayer, value: targetplayer
    public EventManager event;
    public List<UUID> onChoosingItem = new ArrayList<>();
    public HashMap<UUID, HashMap<Inventory, String>> onOverWriteSLOT = new HashMap<>(); //key: puuid, value: (pinv:slottype)


    @Override
    public void onEnable() {
        getLogger().info("ItenLostCompensation Enabled.");
        saveDefaultConfig();
        config = new ConfigManager(this);
        config.load();
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
