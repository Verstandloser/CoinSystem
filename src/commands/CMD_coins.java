package commands;

import me.main.Main;
import me.main.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CMD_coins implements CommandExecutor {
    public boolean onCommand(CommandSender cs, Command command, String label, String[] args) {

        if(cs instanceof Player){
            Player p = (Player) cs;
            if(args.length==0){
                p.sendMessage(Main.prefix+"§7Du hast §6"+getCoins(p.getName())+" §7Coins");
            }else if(args.length==1){
                if(args[0].equalsIgnoreCase("help")){
                    if(p.hasPermission("system.coins.admin")){
                        p.sendMessage("§6§m-----------------§r §6CoinSystem §6§m-----------------§r \n " +
                                "§e/coins §6- §aZeigt dir deine Coins an \n " +
                                "§e/coins <Spieler> §6- §aZeigt dir die Coins von einem Spieler an \n " +
                                "§e/coins add <Spieler> <Anzahl> §6- §aFügt einem Spieler Coins hinzu \n " +
                                "§e/coins remove <Spieler> <Anzahl> §6- §aZieht einem Spieler Coins ab \n " +
                                "§e/coins set <Spieler> <Anzahl> §6- §aSetzt die Coins von einem Spieler auf einen bestimmten Wert \n " +
                                "§e/coins delete <Spieler> §6- §aEntfernt einen Nutzer aus der Datenbank! \n " +
                                "§6§m-----------------§r §6CoinSystem §6§m-----------------");
                    }else{
                        p.sendMessage("§6§m-----------------§r §6CoinSystem §6§m-----------------§r \n " +
                                "§e/coins §6- §aZeigt dir deine Coins an \n " +
                                "§e/coins <Spieler> §6- §aZeigt dir die Coins von einem Spieler an \n " +
                                "§6§m-----------------§r §6CoinSystem §6§m-----------------§r");
                    }
                }else{
                    if(isInTable(args[0])){
                        p.sendMessage(Main.prefix+"§7Der Spieler hat §6"+getCoins(args[0])+" §7Coins");
                    }else{
                        p.sendMessage(Main.prefix+"§cDer Spieler konnte nicht gefunden werden!");
                    }
                }
            }else if(args.length==2){
                if(args[0].equalsIgnoreCase("delete")){
                    if(p.hasPermission("system.coins.admin")){
                        if(isInTable(args[1])){
                            deletePlayer(args[1]);
                            p.sendMessage(Main.prefix+"§7Du hast den Spieler erfolgreich aus der Datenbank §centfernt!");
                        }else{
                            p.sendMessage(Main.prefix+"§cDer Spieler ist nicht in der Datenbank eingetragen!");
                        }
                    }else{
                        p.sendMessage(Main.prefix+"§7Du hast §6"+getCoins(p.getName())+" §7Coins");
                    }
                }else{
                    if(p.hasPermission("system.coins.delete")){
                        p.sendMessage("§6§m-----------------§r §6CoinSystem §6§m-----------------§r \n " +
                                "§e/coins §6- §aZeigt dir deine Coins an \n " +
                                "§e/coins <Spieler> §6- §aZeigt dir die Coins von einem Spieler an \n " +
                                "§e/coins add <Spieler> <Anzahl> §6- §aFügt einem Spieler Coins hinzu \n " +
                                "§e/coins remove <Spieler> <Anzahl> §6- §aZieht einem Spieler Coins ab \n " +
                                "§e/coins set <Spieler> <Anzahl> §6- §aSetzt die Coins von einem Spieler auf einen bestimmten Wert \n " +
                                "§e/coins delete <Spieler> §6- §aEntfernt einen Nutzer aus der Datenbank! \n " +
                                "§6§m-----------------§r §6CoinSystem §6§m-----------------");
                    }else{
                        p.sendMessage(Main.prefix+"§7Du hast §6"+getCoins(args[0])+" §7Coins");
                    }
                }
            }else if(args.length==3){
                if(p.hasPermission("system.coins.admin")){
                    if(args[0].equalsIgnoreCase("add")){
                        if(isInTable(args[1])){
                            if(isInt(args[2])){
                                addCoins(args[1], Integer.valueOf(args[2]));
                                p.sendMessage(Main.prefix+"§7Du hast dem Spieler §b"+args[1]+" §a"+args[2]+" §7Coins §agegeben");
                            }else{
                                p.sendMessage(Main.prefix+"§cNutze: §f/coins add <Spieler> <§4ZAHL§f>");
                            }
                        }else{
                            p.sendMessage(Main.prefix+"§cDer Spieler existiert nicht!");
                        }
                    }else if(args[0].equalsIgnoreCase("remove")){
                        if(isInTable(args[1])){
                            if(isInt(args[2])){
                                removeCoins(args[1], Integer.valueOf(args[2]));
                                p.sendMessage(Main.prefix+"§7Du hast dem Spieler §b"+args[1]+" §a"+args[2]+" §7Coins §cabgezogen");
                            }else{
                                p.sendMessage(Main.prefix+"§cNutze: §f/coins remove <Spieler> <§4ZAHL§f>");
                            }
                        }else{
                            p.sendMessage(Main.prefix+"§cDer Spieler existiert nicht!");
                        }
                    }else if(args[0].equalsIgnoreCase("set")){
                        if(isInTable(args[1])){
                            if(isInt(args[2])){
                                setCoins(args[1], Integer.valueOf(args[2]));
                                p.sendMessage(Main.prefix+"§7Du hast dem Spieler §b"+args[1]+" §a"+args[2]+" §7Coins §egesetzt");
                            }else{
                                p.sendMessage(Main.prefix+"§cNutze: §f/coins set <Spieler> <§4ZAHL§f>");
                            }
                        }else{
                            p.sendMessage(Main.prefix+"§cDer Spieler existiert nicht!");
                        }
                    }else{
                        p.sendMessage("§6§m-----------------§r §6CoinSystem §6§m-----------------§r \n " +
                                "§e/coins §6- §aZeigt dir deine Coins an \n " +
                                "§e/coins <Spieler> §6- §aZeigt dir die Coins von einem Spieler an \n " +
                                "§e/coins add <Spieler> <Anzahl> §6- §aFügt einem Spieler Coins hinzu \n " +
                                "§e/coins remove <Spieler> <Anzahl> §6- §aZieht einem Spieler Coins ab \n " +
                                "§e/coins set <Spieler> <Anzahl> §6- §aSetzt die Coins von einem Spieler auf einen bestimmten Wert \n " +
                                "§e/coins delete <Spieler> §6- §aEntfernt einen Nutzer aus der Datenbank! \n " +
                                "§6§m-----------------§r §6CoinSystem §6§m-----------------");
                    }
                }else{
                    p.sendMessage(Main.prefix+"§7Du hast §6"+getCoins(p.getName())+" §7Coins");
                }
            }else{
                if(p.hasPermission("system.coins.admin")){
                    p.sendMessage("§6§m-----------------§r §6CoinSystem §6§m-----------------§r \n " +
                            "§e/coins §6- §aZeigt dir deine Coins an \n " +
                            "§e/coins <Spieler> §6- §aZeigt dir die Coins von einem Spieler an \n " +
                            "§e/coins add <Spieler> <Anzahl> §6- §aFügt einem Spieler Coins hinzu \n " +
                            "§e/coins remove <Spieler> <Anzahl> §6- §aZieht einem Spieler Coins ab \n " +
                            "§e/coins set <Spieler> <Anzahl> §6- §aSetzt die Coins von einem Spieler auf einen bestimmten Wert \n " +
                            "§e/coins delete <Spieler> §6- §aEntfernt einen Nutzer aus der Datenbank! \n " +
                            "§6§m-----------------§r §6CoinSystem §6§m-----------------");
                }else{
                    p.sendMessage("§6§m-----------------§r §6CoinSystem §6§m-----------------§r \n " +
                            "§e/coins §6- §aZeigt dir deine Coins an \n " +
                            "§e/coins <Spieler> §6- §aZeigt dir die Coins von einem Spieler an \n " +
                            "§6§m-----------------§r §6CoinSystem §6§m-----------------§r");
                }
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

    public void deletePlayer(String name){
        MySQL.update("DELETE FROM coinsystem WHERE Spielername='"+name+"'");
    }

    public boolean isInt(String string){
        try {
            Integer.parseInt(string);
        }catch (NumberFormatException nfe){
            return false;
        }
        return true;
    }
}
