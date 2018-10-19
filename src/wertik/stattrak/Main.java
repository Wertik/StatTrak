package wertik.stattrak;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import sun.security.krb5.Config;
import wertik.stattrak.commands.CommandHandler;
import wertik.stattrak.listeners.DeathListener;

import java.io.File;

public class Main extends JavaPlugin {

    private static Main instance;
    private ConfigLoader configLoader;
    private Utils utils;

    public static Main getInstance() {
        return Main.instance;
    }

    @Override
    public void onEnable() {

        ConsoleCommandSender console = getServer().getConsoleSender();

        instance = this;
        utils = new Utils();
        configLoader = new ConfigLoader();

        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getCommand("stattrak").setExecutor(new CommandHandler());

        console.sendMessage("§5Enabling StatTrak by Wertik1206, take care of your children, it's gonna explode!");

        // YAMLS

        configLoader.setYamls();
        configLoader.loadWeaponTypes();
        configLoader.setStrings(console);
    }

    @Override
    public void onDisable() {
        // Eh? Oh I know!
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage("§5Disabling StatTrak, you're safe now.");
    }

    public ConfigLoader getConfigLoader() {return configLoader;}

    public Utils getUtils() {
        return utils;
    }
}
