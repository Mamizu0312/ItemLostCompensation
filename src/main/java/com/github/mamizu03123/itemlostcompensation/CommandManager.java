package com.github.mamizu03123.itemlostcompensation;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {
    JavaPlugin plugin;
    MySQLManager sql;
    public CommandManager(JavaPlugin plugin, MySQLManager sql) {
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
        if(args[0].equalsIgnoreCase("saveslot1")) {
            ResultSet rs = sql.query("SELECT * FROM PLAYERDATA WHERE MCID = '"+p.getName()+"'");
            if(!(rs == null)); //TODO:すでに登録されている場合の処理
            p.sendMessage("スロット1の情報を上書きしますか？"); //TODO:はい/いいえで答えられるようにする

            try {

            }catch (SQLException e) {
                p.sendMessage(plugin.description + "例外エラーが発生しました。");
                e.printStackTrace();
            }

        }
        return true;
    }
}
