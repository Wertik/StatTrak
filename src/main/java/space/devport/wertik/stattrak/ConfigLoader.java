package space.devport.wertik.stattrak;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigLoader {

    private Main plugin;

    @Getter
    public List<String> weaponTypes;

    @Getter
    public List<String> entityTypes;

    @Getter
    private String line;

    public HashMap<String, String> normalizedNames = new HashMap<>();

    public ConfigLoader() {
        plugin = Main.inst;
    }

    public void setStrings(CommandSender p) {
        this.line = format(getString("stattrak-line"));

        if (plugin.getConfig().getBoolean("Options.types-on-reload"))
            p.sendMessage("§aLine " + line);
    }

    public void loadYamls() {
        // CF
        File configFile = new File(plugin.getDataFolder() + "/config.yml");

        if (!configFile.exists()) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
            plugin.getServer().getConsoleSender().sendMessage("§f[StatTrak] §aGenerated default §f" + configFile.getName());
        }
    }

    // String getters
    public List<String> getStringList(String name) {
        return plugin.getConfig().getStringList("Options." + name);
    }

    public String getString(String name) {
        return plugin.getConfig().getString("Options." + name);
    }

    public String getFormattedMessage(String name) {
        return format(plugin.getConfig().getString("Strings." + name).replace("%prefix%", plugin.getConfig().getString("Strings.prefix")));
    }

    public String getValueFormat() {
        return format(getString("value-format"));
    }

    public String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    // Load the weapon types
    public void loadWeaponTypes() {

        ConsoleCommandSender console = plugin.getServer().getConsoleSender();

        weaponTypes = new ArrayList<>();

        // Material list check
        for (String type : getStringList("stattrak-weapon-types")) {
            try {
                Material.valueOf(type.toUpperCase());
                weaponTypes.add(type.toUpperCase());
                if (plugin.getConfig().getBoolean("Options.types-on-reload"))
                    console.sendMessage("§aSuccessfully loaded weapon type §f" + type + "§a.");
            } catch (Exception e) {
                console.sendMessage("§cWeapon type §f" + type + " §cis not valid, skiping the usage of it.");
            }
        }
    }

    public void loadNormalizedNames() {
        for (String name : plugin.getConfig().getConfigurationSection("normalized-names").getKeys(false))
            normalizedNames.put(name, plugin.getConfig().getString("normalized-names." + name));
    }

    public String getNormalizedName(String type) {
        return normalizedNames.getOrDefault(type.toUpperCase(), type);
    }

    public void loadEntityTypes() {
        ConsoleCommandSender console = plugin.getServer().getConsoleSender();

        entityTypes = new ArrayList<>();

        // Material list check
        for (String type : getStringList("stattrak-entity-types")) {
            try {
                EntityType.valueOf(type.toUpperCase());
                entityTypes.add(type.toUpperCase());
                if (plugin.getConfig().getBoolean("Options.types-on-reload"))
                    console.sendMessage("§aSuccessfully loaded entity type §f" + type + "§a.");
            } catch (Exception e) {
                console.sendMessage("§cEntity type §f" + type + " §cis not valid, skiping the usage of it.");
            }
        }
    }

    public void loadEntityTypes(CommandSender s) {
        entityTypes = new ArrayList<>();

        // Material list check
        for (String type : getStringList("stattrak-entity-types")) {
            try {
                EntityType.valueOf(type.toUpperCase());
                entityTypes.add(type.toUpperCase());
                if (plugin.getConfig().getBoolean("Options.types-on-reload"))
                    s.sendMessage("§aSuccessfully loaded entity type §f" + type + "§a.");
            } catch (Exception e) {
                s.sendMessage("§cEntity type §f" + type + " §cis not valid, skiping the usage of it.");
            }
        }
    }

    // Load the weapon types
    public void loadWeaponTypes(CommandSender p) {

        weaponTypes = new ArrayList<>();

        // Material list check
        for (String type : getStringList("stattrak-weapon-types")) {
            try {
                Material.valueOf(type);
                weaponTypes.add(type);
                if (plugin.getConfig().getBoolean("Options.types-on-reload"))
                    p.sendMessage("§aSuccessfuly loaded weapon type §f" + type + "§a.");
            } catch (Exception e) {
                p.sendMessage("§cWeapon type §f" + type + " §cis not valid, skiping the usage of it.");
            }
        }
    }
}
