package me.main;

import commands.*;
import events.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    public static String prefix = "§f»§6Coins§f« ";

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(prefix+"§aPlugin aktiviert!");

        registerCommands();
        registerListeners();
        createFile();

        MySQL.connect();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(prefix+"§cPlugin deaktiviert!");
        MySQL.close();
    }

    private void createFile(){
        File file = new File("plugins/PlayInfinity", "config.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()){
            cfg.set("host", "localhost");
            cfg.set("database", "Datenbank");
            cfg.set("user", "root");
            cfg.set("password", "PASSWORT");
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerCommands(){
        getCommand("coins").setExecutor(new CMD_coins());
        getCommand("pay").setExecutor(new CMD_pay());
        getCommand("shop").setExecutor(new CMD_shop());
    }

    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new JoinEvents(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickEvents(this), this);
        Bukkit.getPluginManager().registerEvents(new KillEvents(this), this);
    }
}
