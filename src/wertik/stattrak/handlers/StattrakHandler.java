package wertik.stattrak.handlers;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import wertik.stattrak.ConfigLoader;
import wertik.stattrak.Main;
import wertik.stattrak.nbt.NBTEditor;
import wertik.stattrak.nbt.NBTUtils;

import java.util.ArrayList;
import java.util.List;

public class StattrakHandler {

    private Main plugin;
    private ConfigLoader configLoader;

    public StattrakHandler() {
        plugin = Main.getInstance();
        configLoader = plugin.getConfigLoader();
    }

    public ItemStack applyStatTrak(ItemStack item) {

        // NBT
        item = NBTEditor.writeNBT(item, "kills", String.valueOf(0));

        // Lore
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        System.out.print(configLoader.getStatTrakLine());

        if (meta.hasLore()) {
            lore = meta.getLore();
            List<String> list = new ArrayList<>();
            list.add(configLoader.getStatTrakLine().replace("%kills%", String.valueOf(0)));
            Bukkit.broadcastMessage(configLoader.getStatTrakLine().replace("%kills%", String.valueOf(0)));
            list.addAll(lore);
            lore = list;
        } else
            lore.add(configLoader.getStatTrakLine().replace("%kills%", String.valueOf(0)));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack addKill(ItemStack item) {
        int kills = getKills(item);

        // NBT
        item = NBTEditor.writeNBT(item, "kills", String.valueOf(kills+1));

        // Lore
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();

        List<String> newLore = new ArrayList<>();

        for (String line : lore) {

            if (line.contains(configLoader.getStatTrakLine().replace("%kills%", ""))) {

                newLore.add(configLoader.getStatTrakLine().replace("%kills%", String.valueOf(kills+1)));
                continue;
            }

            newLore.add(line);
        }

        lore = newLore;

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    // true/false weapon stattrakable
    public boolean isStatTrakable(String type) {

        if (configLoader.getWeaponTypes().contains(type))
            return true;
        else
            return false;
    }

    public boolean hasStatTrak(ItemStack item) {
        return NBTEditor.hasNBTTag(item, "kills");
    }

    public int getKills(ItemStack item) {
        return Integer.valueOf(NBTUtils.strip(NBTEditor.getNBT(item, "kills")));
    }
}
