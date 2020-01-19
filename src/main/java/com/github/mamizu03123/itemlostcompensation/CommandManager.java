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
import java.util.HashMap;
import java.util.List;

public class CommandManager implements CommandExecutor {

    ItemLostCompensation plugin;
    MySQLManager sql;

    public CommandManager(ItemLostCompensation plugin) {

        this.plugin = plugin;
        this.sql = new MySQLManager(plugin,"ItemLostCompensation");

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
                List<String> prelist = new ArrayList<>();
                try {

                    while(rs.next()) {

                        prelist.add(rs.getString("UUID"));

                    }

                } catch (SQLException e) {

                    p.sendMessage(plugin.prefix + "データベースエラーが発生しました");
                    e.printStackTrace();
                    return true;

                }

                if(prelist.isEmpty()) {

                    ItemStack yesbuttom = new ItemStack(Material.WOOL, 1, (byte) 5);
                    ItemMeta yesmeta = yesbuttom.getItemMeta();
                    yesmeta.setDisplayName("利用を開始する");
                    List<String> yeslore = new ArrayList<>();
                    yeslore.add("クリックで登録！");
                    yesmeta.setLore(yeslore);
                    yesbuttom.setItemMeta(yesmeta);

                    ItemStack nobuttom = new ItemStack(Material.WOOL, 1, (byte) 14);
                    ItemMeta nometa = nobuttom.getItemMeta();
                    nometa.setDisplayName("中止する");
                    List<String> nolore = new ArrayList<>();
                    nolore.add("登録を中止します");
                    nometa.setLore(nolore);
                    nobuttom.setItemMeta(nometa);

                    Inventory inv = Bukkit.createInventory(null, 27, plugin.prefix);

                    inv.setItem(11, yesbuttom);
                    inv.setItem(15, nobuttom);

                    plugin.onYNFirst.add(p.getUniqueId());

                    p.openInventory(inv);
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
            slot2meta.setDisplayName("スロット2");
            List<String> slot2lore = new ArrayList<>();
            slot2lore.add("現在スロット2は空いています");
            slot2meta.setLore(slot2lore);
            slot2.setItemMeta(slot2meta);


            ItemStack slot3 = new ItemStack(Material.PAPER, 1, (short) 0);
            ItemMeta slot3meta = slot1.getItemMeta();
            slot3meta.setDisplayName("スロット3");
            List<String> slot3lore = new ArrayList<>();
            slot3lore.add("現在スロット3は空いています");
            slot3meta.setLore(slot3lore);
            slot3.setItemMeta(slot3meta);
            Inventory inv = Bukkit.createInventory(null, 9, plugin.prefix);

            inv.setItem(0,slot1);
            inv.setItem(1,slot2);
            inv.setItem(2,slot3);

            plugin.onChoosingItem.add(p.getUniqueId());
            p.openInventory(inv);
            return true;

        }
        if(args.length == 1) {

            if(args[0].equalsIgnoreCase("saveslot1")) {

                ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE MCID = '"+p.getName()+"'");
                String slot1data = null;
                try {

                    slot1data = rs.getString("ITEMSLOT1");

                } catch(SQLException e) {

                    p.sendMessage(plugin.prefix + "データベースエラーが発生しました");
                    e.printStackTrace();
                    return true;

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

                ItemStack yesButtom = new ItemStack(Material.WOOL,1,(byte) 5);
                ItemMeta yesmeta = yesButtom.getItemMeta();
                yesmeta.setDisplayName("&r上書きする");
                yesButtom.setItemMeta(yesmeta);

                ItemStack noButtom = new ItemStack(Material.WOOL, 1, (byte) 14);
                ItemMeta nometa = noButtom.getItemMeta();
                nometa.setDisplayName("&r上書きしない");
                noButtom.setItemMeta(nometa);

                Inventory inv = Bukkit.createInventory(null, 27, plugin.prefix + "上書き選択");

                inv.setItem(11, yesButtom);
                inv.setItem(15, noButtom);

                HashMap<String, Inventory> pdata = new HashMap<>();
                pdata.put(inv, "SLOT1");
                plugin.onOverWriteSLOT.put(p.getUniqueId(), pdata);
                p.openInventory(inv);
                return true;

            }


            if(args[0].equalsIgnoreCase("saveslot2")) {

                ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE UUID = '"+p.getUniqueId()+"'");
                String slot2data = null;

                try {

                    slot2data = rs.getString("ITEMSLOT2");

                } catch (SQLException e) {

                    p.sendMessage(plugin.prefix + "データベースエラーが発生しました");
                    e.printStackTrace();
                    return true;

                }
                if(slot2data == null) {

                    TextComponent tc = new TextComponent();
                    tc.setText("ここをクリック！");
                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ilc saveslot2 regist"));
                    p.sendMessage(plugin.prefix + "登録したいアイテムを持って" + tc);
                    return true;

                }

                return true;

            }


            if(args[0].equalsIgnoreCase("saveslot3")) {

                ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE UUID = '" + p.getUniqueId() + "'");
                String slot3data = null;

                try {

                    slot3data = rs.getString("ITEMSLOT3");

                } catch (SQLException e) {

                    p.sendMessage(plugin.prefix + "データベースエラーが発生しました");
                    e.printStackTrace();
                    return true;
                }

                if (slot3data == null) {

                    TextComponent tc = new TextComponent();
                    tc.setText("ここをクリック");
                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ilc saveslot3 regist"));
                    p.sendMessage(plugin.prefix + "登録したいアイテムを持って" + tc);
                    return true;
                }

            }

        }

        if(args.length == 2) {

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

            }

            if(args[0].equalsIgnoreCase("saveslot2")) {

                if(args[1].equalsIgnoreCase("regist")) {

                    ItemStack handItem = p.getInventory().getItemInMainHand();

                    if(handItem == null) {

                        p.sendMessage(plugin.prefix + "アイテムがありません！");
                        return true;

                    }

                    String handItemstr = handItem.toString();
                    sql.execute("UPDATE ITEMSLOT2 SET '"+handItemstr+"'");
                    p.sendMessage(plugin.prefix + "アイテムが登録されました");
                    return true;

                }

            }

            if(args[0].equalsIgnoreCase("saveslot3")) {

                if(args[1].equalsIgnoreCase("regist")) {

                    ItemStack handItem = p.getInventory().getItemInMainHand();

                    if(handItem == null) {

                        p.sendMessage(plugin.prefix + "アイテムがありません！");
                        return true;

                    }

                    String handItemstr = handItem.toString();
                    sql.execute("UPDATE * FROM PLAYERDATA WHERE UUID = '"+p.getUniqueId()+"'");
                    p.sendMessage(plugin.prefix + "で＾他ベースエラーが発生しました");
                    return true;

                }

            }

            //args.length==2の場合の処理を追加するときはargs[0].equalsIgnoreCase("open")の処理を最後に回すこと

            if(args[0].equalsIgnoreCase("open")) {

                if(!p.hasPermission("ilc.op")) {

                    p.sendMessage(plugin.prefix + "あなたには権限がありません！");
                    return true;

                }

                    Player targetplayer = Bukkit.getPlayer(args[1]);

                    if(targetplayer == null) {

                        p.sendMessage(plugin.prefix + "そのプレイヤーは存在しません");
                        return true;

                    }

                    ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE UUID = '"+targetplayer.getUniqueId()+"'");

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
                        return true;

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

                    plugin.onOpeningItemList.put(p.getUniqueId(), targetplayer.getUniqueId());

                    p.openInventory(pinv);
                    return true;

            }

        }

        return true;

    }

}
