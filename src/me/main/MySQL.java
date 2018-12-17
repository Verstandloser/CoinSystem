package me.main;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {


    public static Connection con;

    public static void connect(){

        try {
            File file = new File("plugins/PlayInfinity", "config.yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            String host = cfg.getString("host");
            String database = cfg.getString("database");
            String username = cfg.getString("user");
            String pass = cfg.getString("password");
            con = DriverManager.getConnection("jdbc:mysql://"+host+":3306"+"/"+database, username, pass);
            Bukkit.getConsoleSender().sendMessage("§f[§eMySQL§f] §aVerbunden");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§f[§eMySQL§f] §cFehler beim Verbinden");
        }
    }

    public static void close(){
        if(!isConnected()){
            try {
                con.close();
                Bukkit.getConsoleSender().sendMessage("§f[§eMySQL§f] §cVerbindung geschlossen");
            } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage("§f[§eMySQL§f] §cFehler beim schließen der Verbindung!");

            }

        }
    }

    public static boolean isConnected(){
        return con != null;
    }

    public static void createTable(){


        if(isConnected()){
            try {

                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS coinsystem (Spielername VARCHAR(100), " +
                        "Coins INTEGER(100))");

                Bukkit.getConsoleSender().sendMessage("§f[§eMySQL§f] §6Tabellen erstellt, falls noch nicht vorhanden");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void update(String qry){
        if(isConnected()){
            try {
                con.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet getResult(String qry){
        if(isConnected()){
            try {
                return con.createStatement().executeQuery(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
}
