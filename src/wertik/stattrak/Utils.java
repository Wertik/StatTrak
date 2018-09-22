package wertik.stattrak;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    ConfigLoader cload = new ConfigLoader();

    public Utils() {
    }

    // Item re-creation
    public ItemStack recreateItem(ItemStack item) {

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        // LORE FORMAT
        if (lore != null) {
            if (isStatTrak(lore)) {
                lore = replaceLore(lore);
            } else
                lore = insertLore(lore, 0);

        } else
            lore = insertLore(new ArrayList<>(), 0);

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    // Return if the lore contains StatTrak lore
    public boolean isStatTrak(List<String> lore) {

        boolean stattrak = false;

        // NOW DECIDE!
        for (String line : lore) {
            if (line.contains(getKillsLine())) {
                stattrak = true;
            }
        }

        return stattrak;
    }

    // Return the killsline
    // ex. "Kills: "
    public String getKillsLine() {

        // The line without the placeholder itself
        String killsline = "";

        if (cload.getFormat()) {

            // StatTrak lore with a placeholder, not replaced
            // - "---- ST ----"
            // - "%kills%"
            // - "---- ST ----"
            List<String> stattraklore = cload.getStatTrakLore();

            for (String line : stattraklore) {
                if (line.contains("%kills%")) {
                    // Kills:
                    killsline = line.replace("%kills%", "");
                }
            }
        } else
            killsline = cload.getStatTrakLine().replace("%kills%", "");

        return killsline;
    }

    // Return kills line, with the placeholder
    // ex. "Kills: %kills%"
    public String getPlaceholderLine() {

        // The line with the placeholder
        String placeholderline = "";

        if (cload.getFormat()) {

            // StatTrak lore with a placeholder, not replaced
            // - "---- ST ----"
            // - "%kills%"
            // - "---- ST ----"
            List<String> stattraklore = cload.getStatTrakLore();

            for (String line : stattraklore) {
                if (line.contains("%kills%")) {
                    // Kills: %kills%
                    placeholderline = line;
                }
            }
        } else
            placeholderline = cload.getStatTrakLine();

        return placeholderline;
    }

    // If the item is stattrak, replace the kills lore
    public List<String> replaceLore(List<String> lore) {

        // Find the kills line and replace it with actual kills
        for (String line : lore) {
            if (line.contains(getKillsLine())) {
                lore.set(lore.indexOf(line), getPlaceholderLine().replace("%kills%", String.valueOf(getKills(lore) + 1)));
            }
        }

        return lore;
    }

    // Add a lore at the start
    public List<String> insertLore(List<String> lore, int kills) {

        List<String> newlore = new ArrayList<>();

        if (cload.getFormat()) {

            for (String line : cload.getStatTrakLore()) {
                if (line.contains("%kills%"))
                    line = line.replace("%kills%", String.valueOf(kills));
                newlore.add(line);
            }
        } else
            newlore.add(getPlaceholderLine().replace("%kills%", String.valueOf(kills)));

        for (String line : lore) {
            newlore.add(line);
        }

        return newlore;
    }

    // Get kills from an existing stattrak lore
    public int getKills(List<String> lore) {

        int kills = -1;

        for (String line : lore) {
            if (line.contains(getKillsLine())) {
                // Get kills
                kills = Integer.valueOf(line.replace(getKillsLine(), ""));
            }
        }

        return kills;
    }
}
