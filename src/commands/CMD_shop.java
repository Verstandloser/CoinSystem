package commands;

import me.main.Main;
import me.main.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CMD_shop implements CommandExecutor {
    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {

        if(cs instanceof Player){
            Player p = (Player) cs;
            if(hasCoins(p.getName(), 15)){
                removeCoins(p.getName(), 15);
                ItemStack diasw = new ItemStack(Material.DIAMOND_SWORD);
                ItemMeta diaim = diasw.getItemMeta();
                diaim.setDisplayName("§4OP-SWORD");

                List<String> list = new ArrayList<String>();
                list.add("§a ");
                list.add("§8§m--------------");
                list.add("§coriginal aus dem Shop!");
                list.add("§8§m--------------");
                list.add("§9 ");

                diaim.setLore(list);
                diaim.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
                diasw.setItemMeta(diaim);

                Inventory inv = Bukkit.createInventory(null, 9, "§aGreif zur Waffe!");
                inv.setItem(4, diasw);
                p.openInventory(inv);

            }else{
                p.sendMessage(Main.prefix+"§cDu benötigst §515 §cCoins, um das Inventar zu öffnen!");
            }
        }

        return true;
    }

    public Integer getCoins(String name){
        ResultSet rs = MySQL.getResult("SELECT * FROM coinsystem WHERE Spielername='"+name+"'");
        try {
            while (rs.next()){
                return rs.getInt("Coins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void removeCoins(String name, Integer coins){
        if(isInTable(name)){
            Integer i = getCoins(name);
            Integer newCoins = i-coins;
            MySQL.update("DELETE FROM coinsystem WHERE Spielername='"+name+"'");
            MySQL.update("INSERT INTO coinsystem (Spielername, Coins) VALUES ('"+name+"','"+newCoins+"')");
        }else{
            MySQL.update("INSERT INTO coinsystem (Spielername, Coins) VALUES ('"+name+"','"+0+"')");
        }
    }

    public void setCoins(String name, Integer coins){
        if(isInTable(name)){
            MySQL.update("DELETE FROM coinsystem WHERE Spielername='"+name+"'");
            MySQL.update("INSERT INTO coinsystem (Spielername, Coins) VALUES ('"+name+"','"+coins+"')");
        }else{
            MySQL.update("DELETE FROM coinsystem WHERE Spielername='"+name+"'");
            MySQL.update("INSERT INTO coinsystem (Spielername, Coins) VALUES ('"+name+"','"+coins+"')");
        }
    }

    public boolean isInTable(String name){
        ResultSet rs = MySQL.getResult("SELECT * FROM coinsystem WHERE Spielername='"+name+"'");
        try {
            while (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasCoins(String name, Integer coins){
        if(getCoins(name)>=coins){
            return true;
        }else{
            return false;
        }
    }
}
