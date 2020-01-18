package com.github.mamizu03123.itemlostcompensation;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventManager implements Listener {
    JavaPlugin plugin;
    public EventManager(JavaPlugin plugin) {
        this.plugin = plugin;

    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        if(p == null) return;
    }
}
