package wertik.stattrak;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import wertik.stattrak.commands.CommandHandler;
import wertik.stattrak.listeners.DeathListener;

import java.io.File;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return Main.instance;
    }

    private static void setInstance(Main instance) {
        Main.instance = instance;
    }

    @Override
    public void onEnable() {

        ConsoleCommandSender console = getServer().getConsoleSender();

        setInstance(this);

        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getCommand("stattrak").setExecutor(new CommandHandler());

        console.sendMessage("§5Enabling StatTrak by Wertik1206, take care of your children, it's gonna explode!");

        // YAMLS

        File configfile = new File(getDataFolder() + "/config.yml");
        FileConfiguration config = getConfig();

        if (!configfile.exists()) {

            config.options().copyDefaults(true);

            saveConfig();
        }

        ConfigLoader cload = new ConfigLoader();

        cload.loadWeaponTypes();
        cload.setStrings(console);
    }

    @Override
    public void onDisable() {
        // Eh? Oh I know!
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage("§5Disabling StatTrak, you're safe now.");
    }
}
