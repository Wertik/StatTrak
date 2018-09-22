package wertik.stattrak;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {

    Main plugin = Main.getInstance();
    public static List<String> weapontypes;
    public static String type;
    public static String line;
    public static List<String> lore;

    public ConfigLoader() {
    }

    public void setStrings(CommandSender p) {

        this.lore = formatList(getStringList("stattrak-lore"));
        this.line = format(getString("stattrak-line"));
        this.type = getString("format");

        if (plugin.getConfig().getBoolean("Options.types-on-reload")) {
            p.sendMessage("§aLore " + lore);
            p.sendMessage("§aLine " + line);
            p.sendMessage("§aUsing type of  §f" + type);
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

    public List<String> formatList(List<String> list) {

        List<String> newlist = new ArrayList<>();

        for (String line : list) {
            newlist.add(format(line));
        }
        return newlist;
    }

    // true/false weapon stattrakable
    public boolean isStatTrakable(String type) {

        boolean stattrakable = false;

        if (getWeaponTypes().contains(type)) {
            stattrakable = true;
        }

        return stattrakable;
    }

    // Get weapon types
    public List<String> getWeaponTypes() {
        return weapontypes;
    }

    // Get StatTrak lore
    public List<String> getStatTrakLore() {
        return lore;
    }

    // Get StatTrak line
    public String getStatTrakLine() {
        return line;
    }

    // Switcher
    // true == lore
    // false == line
    public boolean getFormat() {
        if (type.equalsIgnoreCase("lore"))
            return true;
        else
            return false;
    }

    public String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    // Load the weapon types
    public void loadWeaponTypes() {

        this.plugin = Main.getInstance();

        ConsoleCommandSender console = plugin.getServer().getConsoleSender();

        weapontypes = new ArrayList<>();

        // Material list check
        for (String type : getStringList("stattrak-weapon-types")) {
            try {
                Material.valueOf(type);
                weapontypes.add(type);
                if (plugin.getConfig().getBoolean("Options.types-on-reload"))
                    console.sendMessage("§aSuccessfuly loaded weapon type §f" + type + "§a.");
            } catch (Exception e) {
                console.sendMessage("§cWeapon type §f" + type + " §cis not valid, skiping the usage of it.");
            }
        }
    }

    // Load the weapon types
    public void loadWeaponTypes(CommandSender p) {

        this.plugin = Main.getInstance();

        weapontypes = new ArrayList<>();

        // Material list check
        for (String type : getStringList("stattrak-weapon-types")) {
            try {
                Material.valueOf(type);
                weapontypes.add(type);
                if (plugin.getConfig().getBoolean("Options.types-on-reload"))
                    p.sendMessage("§aSuccessfuly loaded weapon type §f" + type + "§a.");
            } catch (Exception e) {
                p.sendMessage("§cWeapon type §f" + type + " §cis not valid, skiping the usage of it.");
            }
        }
    }
}
