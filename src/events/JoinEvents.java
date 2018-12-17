package events;

import me.main.Main;
import me.main.MySQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinEvents implements Listener {

    public JoinEvents(Main main) {
    }

    @EventHandler
    public void onRegisterInTable(PlayerLoginEvent e){
        if(!isInTable(e.getPlayer().getName())){
            MySQL.update("INSERT INTO coinsystem (Spielername, Coins) VALUES ('"+e.getPlayer().getName()+
                    "','"+0+"')");
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

}
