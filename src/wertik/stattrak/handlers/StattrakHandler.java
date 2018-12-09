package wertik.stattrak.handlers;

import me.badbones69.crazyenchantments.api.CEnchantments;
import me.badbones69.crazyenchantments.api.CrazyEnchantments;
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

    public ItemStack removeStatTrak(ItemStack item) {
        // NBT
        item = NBTEditor.removeNBT(item, "kills");

        // Lore
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();

        List<String> newLore = new ArrayList<>();

        for (String line : lore) {
            if (line.contains(configLoader.getStatTrakLine().replace("%kills%", "")))
                continue;

            newLore.add(line);
        }

        lore = newLore;

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public ItemStack setStattrak(ItemStack item, int kills) {
        // NBT
        item = NBTEditor.writeNBT(item, "kills", String.valueOf(kills));

        // Lore
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        List<String> enchantments = new ArrayList<>();

        if (meta.hasLore()) {
            List<String> oldLore = meta.getLore();
            List<String> list = new ArrayList<>();
            lore = meta.getLore();

            if (Main.getInstance().getServer().getPluginManager().getPlugin("CrazyEnchantments").isEnabled()) {
                for (String line : oldLore) {
                    for (CEnchantments en : CrazyEnchantments.getInstance().getEnchantments()) {
                        if (line.startsWith(en.getEnchantmentColor() + en.getCustomName())) {
                            if (!enchantments.contains(line)) {
                                enchantments.add(line);
                                lore.remove(line);
                            }
                        }
                    }
                }

                list.addAll(enchantments);
            }

            list.add(configLoader.getStatTrakLine().replace("%kills%", String.valueOf(kills)));
            list.addAll(lore);
            lore = list;
        } else
            lore.add(configLoader.getStatTrakLine().replace("%kills%", String.valueOf(kills)));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack addKill(ItemStack item) {
        int kills = getKills(item);

        // NBT
        item = NBTEditor.writeNBT(item, "kills", String.valueOf(kills + 1));

        // Lore
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();

        List<String> newLore = new ArrayList<>();

        for (String line : lore) {

            if (line.contains(configLoader.getStatTrakLine().replace("%kills%", ""))) {

                newLore.add(configLoader.getStatTrakLine().replace("%kills%", String.valueOf(kills + 1)));
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
