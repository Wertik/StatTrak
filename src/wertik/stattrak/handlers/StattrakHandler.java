package wertik.stattrak.handlers;

import com.sun.istack.internal.NotNull;
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

    /**
     * Remove stattrak lore and NBT data from an {@link @item}
     * @item Item to remove stattrak from
     *
     * @return Item stack with stattrak lore and NBT removed
     * */

    public ItemStack removeStatTrak(@NotNull ItemStack item) {
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

    /**
    * Sets StatTrak lore and NBT data for {@link @item}
    * @item Item you want to apply stattrak to
    * @key NBT data key
    * @value NBT data value
    *
    * @return @item with changed NBT and lore data
    * */

    public ItemStack setStattrak(@NotNull ItemStack item, String key, int value) {
        // NBT
        item = NBTEditor.writeNBT(item, "stattrak_"+key, String.valueOf(value));

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

            list.add(configLoader.getStatTrakLine().replace("%value%", String.valueOf(value)));
            list.addAll(lore);
            lore = list;
        } else
            lore.add(configLoader.getStatTrakLine().replace("%value%", String.valueOf(value)));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    /**
    * Used to add a point to stattrak {@link @item} value
    * @item Item stack that has stattrak already applied to it
    * @key NBT data key to rewrite
    *
    * @return Edited item stack
    * */

    public ItemStack addToStattrakValue(@NotNull ItemStack item, @NotNull String key) {
        int value = getStattrakValue(item, key);

        // NBT
        item = NBTEditor.writeNBT(item, "stattrak_"+key, String.valueOf(value + 1));

        // Lore
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();

        List<String> newLore = new ArrayList<>();

        for (String line : lore) {

            if (line.contains(configLoader.getStatTrakLine().replace("%value%", ""))) {
                newLore.add(configLoader.getStatTrakLine().replace("%value%", String.valueOf(value + 1)));
                continue;
            }

            newLore.add(line);
        }

        lore = newLore;

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    /**
     * Whether or not is an item {@link @type} stattrakable
     * @type Material name to test
     *
     * @return Boolean stattrakable
     * */
    public boolean isStatTrakable(String type) {
        if (configLoader.getWeaponTypes().contains(type))
            return true;
        else
            return false;
    }

    /**
     * Does {@link @item} have already stattrak on it
     * @item Item to test
     *
     * @return boolean
     * */

    public boolean hasStatTrak(@NotNull ItemStack item) {
        return NBTEditor.hasNBTTag(item, "kills");
    }

    /**
     * Returns value of an NBT data {@link @key}
     * @item Item stack to load from
     * @key NBT data key to look for
     *
     * @return Integer value
     * */

    public int getStattrakValue(@NotNull ItemStack item, String key) {
        return Integer.valueOf(NBTUtils.strip(NBTEditor.getNBT(item, "stattrak_"+key)));
    }
}
