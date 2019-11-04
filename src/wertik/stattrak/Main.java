package wertik.stattrak;

import org.bukkit.plugin.java.JavaPlugin;
import wertik.stattrak.commands.CommandHandler;
import wertik.stattrak.handlers.StattrakHandler;
import wertik.stattrak.listeners.DeathListener;
import wertik.stattrak.listeners.RenameListener;
import wertik.stattrak.util.ConsoleOutput;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return Main.instance;
    }

    private ConfigLoader configLoader;
    private StattrakHandler stattrakHandler;

    public ConsoleOutput cO;

    @Override
    public void onEnable() {
        instance = this;

        cO = new ConsoleOutput(this);

        configLoader = new ConfigLoader();
        stattrakHandler = new StattrakHandler();

        cO.info("Classes loaded");

        // YAMLS

        configLoader.loadYamls();

        configLoader.setStrings(getServer().getConsoleSender());

        configLoader.loadWeaponTypes();
        configLoader.loadEntityTypes();

        configLoader.loadNormalizedNames();

        cO.info("Data loaded");

        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new RenameListener(), this);

        getCommand("stattrak").setExecutor(new CommandHandler());

        cO.info("§f-----------------------");
        cO.info("§5Done...");
    }

    public ConfigLoader getConfigLoader() {return configLoader;}

    public StattrakHandler getStattrakHandler() {
        return stattrakHandler;
    }
}
