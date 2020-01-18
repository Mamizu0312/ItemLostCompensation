package com.github.mamizu03123.itemlostcompensation;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.chat.TextComponent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {
    ItemLostCompensation plugin;
    MySQLManager sql;
    public CommandManager(ItemLostCompensation plugin, MySQLManager sql) {
        this.plugin = plugin;
        this.sql = sql;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if(!p.hasPermission("ilc.use")) {
            p.sendMessage(plugin.prefix + "あなたには権限がありません");
        }

        if(args.length == 0) {
            //TODO:SQLで登録しているかチェック、もし登録されていなかったら利用開始の是非を問う
                ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE UUID = '"+p.getUniqueId()+"'");
                if(rs == null) {
                    plugin.onYNFirst.add(p.getUniqueId());
                    ItemStack yes = new ItemStack(Material.WOOL, 1, (byte) 5);
                    ItemMeta yesmeta = yes.getItemMeta();
                    yesmeta.setDisplayName("利用を開始する");
                    List<String> yeslore = new ArrayList<>();
                    yeslore.add("クリックで登録！");
                    yesmeta.setLore(yeslore);
                    yes.setItemMeta(yesmeta);

                    ItemStack no = new ItemStack(Material.WOOL, 1, (byte) 14);
                    ItemMeta nometa = no.getItemMeta();
                    nometa.setDisplayName("中止する");
                    List<String> nolore = new ArrayList<>();
                    nolore.add("登録を中止します");
                    nometa.setLore(nolore);
                    no.setItemMeta(nometa);

                    Inventory pinv = p.getInventory();
                    pinv.setItem(11, yes);
                    pinv.setItem(15, no);
                    p.openInventory(pinv);
                    return true;
                }
            ItemStack slot1 = new ItemStack(Material.PAPER, 1, (short) 0);
            ItemMeta slot1meta = slot1.getItemMeta();
            slot1meta.setDisplayName("スロット1");
            List<String> slot1lore = new ArrayList<>();
            slot1lore.add("現在スロット1は空いています");
            slot1meta.setLore(slot1lore);
            slot1.setItemMeta(slot1meta);

            ItemStack slot2 = new ItemStack(Material.PAPER, 1, (short) 0);
            ItemMeta slot2meta = slot1.getItemMeta();
            slot1meta.setDisplayName("スロット1");
            List<String> slot2lore = new ArrayList<>();
            slot2lore.add("現在スロット2は空いています");
            slot1meta.setLore(slot2lore);
            slot1.setItemMeta(slot2meta);

            ItemStack slot3 = new ItemStack(Material.PAPER, 1, (short) 0);
            ItemMeta slot3meta = slot1.getItemMeta();
            slot1meta.setDisplayName("スロット1");
            List<String> slot3lore = new ArrayList<>();
            slot3lore.add("現在スロット3は空いています");
            slot1meta.setLore(slot3lore);
            slot1.setItemMeta(slot3meta);
        }

        if(args[0].equalsIgnoreCase("open")) {
            if(!p.hasPermission("ilc.op")) {
                p.sendMessage(plugin.prefix + "あなたには権限がありません！");
                return true;
            }
            if(args.length == 2) {
                Player aimplayer = Bukkit.getPlayer(args[1]);
                if(aimplayer == null) {
                    p.sendMessage(plugin.prefix + "そのプレイヤーは存在しません");
                    return true;
                }
                ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE UUID = '"+aimplayer.getUniqueId()+"'");
                if(rs == null) {
                    p.sendMessage(plugin.prefix + "そのプレイヤーはILCに登録していません。");
                    return true;
                }
                String slot1str = null;
                String slot2str = null;
                String slot3str = null;
                ItemStack slot1;
                ItemStack slot2;
                ItemStack slot3;

                try {
                    slot1str = rs.getString("ITEMSLOT1");
                    slot2str = rs.getString("ITEMSLOT2");
                    slot3str = rs.getString("ITEMSLOT3");
                } catch(SQLException e) {
                    p.sendMessage(plugin.prefix + "データベースエラーが発生しました");
                    e.printStackTrace();
                }
                if(slot1str == null) {
                    slot1 = new ItemStack(Material.PAPER, 1);
                    ItemMeta slot1meta = slot1.getItemMeta();
                    List<String> slot1lore = new ArrayList<>();
                    slot1lore.add("空き");
                    slot1meta.setLore(slot1lore);
                    slot1meta.setDisplayName("スロット1");
                    slot1.setItemMeta(slot1meta);
                } else {
                    Material slot1m = Material.matchMaterial(slot1str);
                    slot1 = new ItemStack(slot1m, 1);
                }
                if(slot2str == null) {
                    slot2 = new ItemStack(Material.PAPER, 1);
                    ItemMeta slot2meta = slot2.getItemMeta();
                    List<String> slot2lore = new ArrayList<>();
                    slot2lore.add("空き");
                    slot2meta.setLore(slot2lore);
                    slot2meta.setDisplayName("スロット2");
                    slot2.setItemMeta(slot2meta);
                } else {
                    Material slot2m = Material.matchMaterial(slot2str);
                    slot2 = new ItemStack(slot2m, 2);
                }
                if(slot3str == null) {
                    slot3 = new ItemStack(Material.PAPER, 1);
                    ItemMeta slot3meta = slot3.getItemMeta();
                    List<String> slot3lore = new ArrayList<>();
                    slot3lore.add("空き");
                    slot3meta.setLore(slot3lore);
                    slot3meta.setDisplayName("スロット3");
                    slot3.setItemMeta(slot3meta);
                } else {
                    Material slot3m = Material.matchMaterial(slot3str);
                    slot3 = new ItemStack(slot3m, 1);
                }
                Inventory pinv = p.getInventory();
                pinv.setItem(0, slot1);
                pinv.setItem(1, slot2);
                pinv.setItem(2, slot3);
                plugin.onOpeningItemList.put(p.getUniqueId(), aimplayer.getUniqueId());
                p.openInventory(pinv);
                return true;
            }

        }

        if(args[0].equalsIgnoreCase("saveslot1")) {
            if(args[1].equalsIgnoreCase("regist")) {
                ItemStack handItem = p.getInventory().getItemInMainHand();
                if(handItem == null) {
                    p.sendMessage(plugin.prefix + "アイテムがありません！");
                    return true;
                }
                //TODO:アイテムがあった場合
                String handItemstr = handItem.toString();
                sql.execute("UPDATE ITEMSLOT1 SET '"+handItemstr + "'");
                p.sendMessage(plugin.prefix + "アイテムが登録されました！");
                return true;
            }
            ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE MCID = '"+p.getName()+"'");
            String slot1data = null;
            try {
                slot1data = rs.getString("ITEMSLOT1");
            } catch(SQLException e) {
                p.sendMessage(plugin.prefix + "データベースエラーが発生しました");
                e.printStackTrace();
            }
            if(slot1data == null) {
                //TODO:なにも登録されてない処理
                TextComponent tc = new TextComponent();
                tc.setText("ここをクリック！");
                tc.setBold(true);
                tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ilc saveslot1 regist"));
                p.sendMessage(plugin.prefix + "登録したいアイテムを持って"+tc);

                return true;
            }
            //TODO:なんか登録されている処理
        }
        if(args[0].equalsIgnoreCase("saveslot2")) {
            if(args[1].equalsIgnoreCase("regist")) {
                ItemStack handItem = p.getInventory().getItemInMainHand();
                if(handItem == null) {
                    p.sendMessage(plugin.prefix + "アイテムがありません！");
                    return true;
                }
                String handItemstr = handItem.toString();
                sql.execute()
            }
            ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE UUID = '"+p.getUniqueId()+"'");
            String slot2data = null;
            try {
                slot2data = rs.getString("ITEMSLOT2");
            } catch (SQLException e) {
                p.sendMessage(plugin.prefix + "データベースエラーが発生しました");
                e.printStackTrace();
            }
            if(slot2data == null) {
                TextComponent tc = new TextComponent();
                tc.setText("ここをクリック！");
                tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ilc saveslot2 regist"));
                p.sendMessage(plugin.prefix + "登録したいアイテムを持って" + tc);
                return true;
            }
        }


        return true;
    }

    public void registerPlayer(Player p) {
        sql.execute("INSERT INTO PLAYERDATA VALUE ('"+p.getName()+"' , '"+p.getUniqueId()+"', NULL, NULL, NULL");
    }
}
