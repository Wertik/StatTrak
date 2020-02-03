package space.devport.wertik.stattrak;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import space.devport.wertik.stattrak.commands.StatTrakCommand;
import space.devport.wertik.stattrak.handlers.StatTrakHandler;
import space.devport.wertik.stattrak.listeners.DeathListener;
import space.devport.wertik.stattrak.listeners.RenameListener;
import space.devport.wertik.stattrak.util.ConsoleOutput;

public class Main extends JavaPlugin {

    public static Main inst;

    @Getter
    private ConfigLoader configLoader;

    @Getter
    private StatTrakHandler stattrakHandler;

    public ConsoleOutput cO;

    @Override
    public void onEnable() {
        inst = this;

        cO = new ConsoleOutput(this);

        configLoader = new ConfigLoader();
        stattrakHandler = new StatTrakHandler();

        cO.info("Classes loaded");

        // YAMLS

        configLoader.loadYamls();

        configLoader.setStrings(getServer().getConsoleSender());

        configLoader.loadWeaponTypes();
        configLoader.loadEntityTypes();

        configLoader.loadNormalizedNames();

        cO.info("Data loaded..");

        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new RenameListener(), this);

        getCommand("stattrak").setExecutor(new StatTrakCommand());

        cO.info("-----------------------");
        cO.info("Done.");
    }
}
