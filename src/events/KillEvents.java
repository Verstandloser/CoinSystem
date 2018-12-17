package events;

import me.main.Main;
import me.main.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KillEvents implements Listener {

    public KillEvents(Main main) {
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e){
        Player p = e.getEntity();
        if(p.getKiller() instanceof Player){
            Player t = p.getKiller();
            addCoins(t.getName(), 2);
            e.setDeathMessage("§a§lSERVER §7» §c"+p.getName()+" §8wurde von §a"+t.getName()+" §8getötet!");
            t.sendMessage(Main.prefix+"§aDir wurden §92 §aCoins gutgeschrieben!");
        }else{
            e.setDeathMessage("§a§lSERVER §7» §c"+p.getName()+" §8ist gestorben!");
        }
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

    public void setCoins(String name, Integer coins){
        if(isInTable(name)){
            MySQL.update("DELETE FROM coinsystem WHERE Spielername='"+name+"'");
            MySQL.update("INSERT INTO coinsystem (Spielername, Coins) VALUES ('"+name+"','"+coins+"')");
        }else{
            MySQL.update("DELETE FROM coinsystem WHERE Spielername='"+name+"'");
            MySQL.update("INSERT INTO coinsystem (Spielername, Coins) VALUES ('"+name+"','"+coins+"')");
        }
    }
}
