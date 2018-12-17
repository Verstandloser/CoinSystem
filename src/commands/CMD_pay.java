package commands;

import me.main.Main;
import me.main.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CMD_pay implements CommandExecutor {
    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {

        if(cs instanceof Player){
            Player p = (Player) cs;
            if(args.length==2){
                if(isInt(args[1])){
                    if(isInTable(args[0])){
                        Integer i = Integer.valueOf(args[1]);
                        if(hasCoins(p.getName(), i)){
                            if(!p.getName().equalsIgnoreCase(args[0])){
                                removeCoins(p.getName(), i);
                                addCoins(args[0], i);
                                Player t = Bukkit.getPlayer(args[0]);
                                p.sendMessage(Main.prefix+"§7Du hast §a"+i+" §7Coins an §e"+args[0]+ " §7gegeben!");
                                if(t!=null) {
                                    t.sendMessage(Main.prefix+"§7Du hast §a"+i+" §7Coins von §e"+p.getName()+ " §7erhalten!");
                                    t.playSound(t.getLocation(), Sound.LEVEL_UP, 3, 2);
                                }
                            }else{
                                p.sendMessage(Main.prefix+"§cDu kannst dir nicht selbst Coins geben!");
                            }
                        }else{
                            p.sendMessage(Main.prefix+"§cDu hast nicht genug Coins!");
                        }
                    }else{
                        p.sendMessage(Main.prefix+"§cDer Spieler konnte nicht gefunden werden!");
                    }
                }else{
                    p.sendMessage(Main.prefix+"§cNutze: §f/pay <Spieler> <§4ZAHL§f>");
                }
            }else{
                p.sendMessage(Main.prefix+"§cNutze: §f/pay <Spieler> <Zahl>");
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

    public void addCoins(String name, Integer coins){
        if(isInTable(name)){
            Integer i = getCoins(name);
            Integer newCoins = i+coins;
            MySQL.update("DELETE FROM coinsystem WHERE Spielername='"+name+"'");
            MySQL.update("INSERT INTO coinsystem (Spielername, Coins) VALUES ('"+name+"','"+newCoins+"')");
        }else{
            setCoins(name, coins);
        }
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

    public boolean isInt(String string){
        try {
            Integer.parseInt(string);
        }catch (NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public boolean hasCoins(String name, Integer coins){
        if(getCoins(name)>=coins){
            return true;
        }else{
            return false;
        }
    }
}
