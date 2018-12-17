package wertik.stattrak;

import org.bukkit.plugin.java.JavaPlugin;
import wertik.stattrak.commands.CommandHandler;
import wertik.stattrak.handlers.StattrakHandler;
import wertik.stattrak.listeners.DeathListener;

public class Main extends JavaPlugin {

    private static Main instance;
    private ConfigLoader configLoader;
    private StattrakHandler stattrakHandler;

    public static Main getInstance() {
        return Main.instance;
    }

    @Override
    public void onEnable() {

        info("§5Enabling StatTrak by Wertik1206, take care of your children, it's gonna explode!");
        info("§f-----------------------");

        instance = this;
        configLoader = new ConfigLoader();
        stattrakHandler = new StattrakHandler();

        info("§aClasses loaded");

        // CE support
        if (getServer().getPluginManager().getPlugin("CrazyEnchantments") != null)
            info("§aCE support enabled");

        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getCommand("stattrak").setExecutor(new CommandHandler());

        info("§aListeners hooked");

        // YAMLS

        configLoader.loadYamls();
        configLoader.setStrings(getServer().getConsoleSender());
        configLoader.loadWeaponTypes();

        info("§aData loaded");

        info("§f-----------------------");
        info("§5Done...");
    }

    @Override
    public void onDisable() {
        info("§5Disabling StatTrak, you're safe now.");
        info("§f----------------");
        info("§cChecking sandwich storage");
        info("§f----------------");
        info("§5Done...");
    }

    private void info(String msg) {
        getServer().getConsoleSender().sendMessage("§f[StatTrak] " + msg);
    }

    public ConfigLoader getConfigLoader() {return configLoader;}

    public StattrakHandler getStattrakHandler() {
        return stattrakHandler;
    }
}
