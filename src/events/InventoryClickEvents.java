package events;

import me.main.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEvents implements Listener {

    public InventoryClickEvents(Main main) {
    }

    @EventHandler
    public void onShop(InventoryClickEvent e){
        if(e.getInventory().getName().equalsIgnoreCase("§aGreif zur Waffe!")){
            e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
            e.getView().close();
            e.getWhoClicked().sendMessage(Main.prefix+"§b§lViel Spaß");
        }
    }

}
