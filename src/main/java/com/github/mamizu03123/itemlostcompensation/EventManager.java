package com.github.mamizu03123.itemlostcompensation;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventManager implements Listener {

    private ItemLostCompensation plugin;
    private MySQLManager sql;

    public EventManager(ItemLostCompensation plugin) {
        this.plugin = plugin;
        this.sql = plugin.sql;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {

        Player p = (Player) event.getWhoClicked();

        if(p == null) {

            return;

        }

        if(plugin.onOverWriteSLOT.containsKey(p.getUniqueId())) {

            switch(plugin.onOverWriteSLOT.get(p.getUniqueId()).get(event.getInventory())) {

                case "SLOT1":

                    event.setCancelled(event.getClickedInventory().equals(plugin.onOverWriteSLOT.get(p.getUniqueId()).get()));
                    if(event.getSlot() == 11) {

                        p.closeInventory();
                        plugin.onOverWriteSLOT.remove(p.getUniqueId());
                        p.chat("/ilc saveslot1 regist");
                        return;

                    }
                    if(event.getSlot() == 15) {

                        p.closeInventory();
                        plugin.onOverWriteSLOT.remove(p.getUniqueId());
                        p.sendMessage(plugin.prefix + "キャンセルしました");
                        return;

                    }

                    break;

                case "SLOT2":

                    //TODO:SLOT2の処理
                    break;

                case "SLOT3":

                    //TODO:SLOT3の処理
                    break;
                default:
                    p.sendMessage(plugin.prefix + "エラーが発生しました。1001");

            }
        }

        if(plugin.onYNFirst.contains(p.getUniqueId())) {

            event.setCancelled(true);

            if(event.getSlot() == 11) {

                plugin.registerPlayer(p);
                p.sendMessage(plugin.prefix + "登録が完了しました！");
                return;

            }

            if(event.getSlot() == 15) {

                p.sendMessage(plugin.prefix + "キャンセルされました");
                return;

            }

            return;

        }

        if(plugin.onOpeningItemList.containsKey(p.getUniqueId())) {

            event.setCancelled(true);
            //TODO:Playerが現在参照しているプレイヤーのスロット1の情報を取得

            if(event.getSlot() == 0) {

                ItemStack paper = new ItemStack(Material.PAPER, 1);

                if(event.getCurrentItem().equals(paper)) {

                    p.sendMessage(plugin.prefix + "スロット1には何も登録されていません");
                    return;

                }

                ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE UUID = '"+plugin.onOpeningItemList.get(p.getUniqueId())+"'");
                if(rs == null) return;
                String slot1str = null;

                try {

                    slot1str = rs.getString("ITEMLOST1");

                } catch (SQLException e) {

                    p.sendMessage(plugin.prefix + "データベースエラーが発生しました");
                    e.printStackTrace();
                    return;

                }

                Material slot1m = Material.getMaterial(slot1str);
                ItemStack slot1 = new ItemStack(slot1m, 1);
                p.getInventory().addItem(slot1);
                p.sendMessage(plugin.prefix + "アイテムを発行しました");
                plugin.onOpeningItemList.remove(p.getUniqueId());
                return;

            }

            if(event.getSlot() == 1) {

                ItemStack paper = new ItemStack(Material.PAPER, 1);

                if(event.getCurrentItem().equals(paper)) {

                    p.sendMessage(plugin.prefix + "スロット2には何も登録されていません");
                    return;

                }

                ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE UUID ='"+p.getUniqueId()+"'");

                if(rs == null) return;

                String slot2str =null;

                try {

                    slot2str = rs.getString("ITEMSLOT2");

                } catch(SQLException e) {

                    p.sendMessage(plugin.prefix + "データベースエラーが発生しました");
                    e.printStackTrace();
                    return;

                }

                Material slot2m = Material.getMaterial(slot2str);
                ItemStack slot2 = new ItemStack(slot2m, 1);
                p.getInventory().addItem(slot2);
                p.sendMessage(plugin.prefix + "アイテムを発行しました");
                plugin.onOpeningItemList.remove(p.getUniqueId());
                return;

            }

            if(event.getSlot() == 3) {

                ItemStack paper = new ItemStack(Material.PAPER, 1);

                if(event.getCurrentItem().equals(paper)) {

                    p.sendMessage(plugin.prefix + "スロット3には何も登録されていません");

                    return;

                }

                ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE UUID = '"+p.getUniqueId()+"'");

                if(rs == null) return;

                String slot3str = null;

                try{

                    slot3str = rs.getString("ITEMSLOT3");

                } catch (SQLException e) {

                    p.sendMessage(plugin.prefix + "データベースエラーが発生しました");
                    e.printStackTrace();
                    return;

                }

                Material slot3m = Material.getMaterial(slot3str);
                ItemStack slot3 = new ItemStack(slot3m, 1);
                p.getInventory().addItem(slot3);
                p.sendMessage(plugin.prefix + "アイテムを発行しました");
                plugin.onOpeningItemList.remove(p.getUniqueId());
                return;
            }
        }
    }
}
