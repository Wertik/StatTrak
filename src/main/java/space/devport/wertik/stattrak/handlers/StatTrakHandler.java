package space.devport.wertik.stattrak.handlers;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import space.devport.wertik.stattrak.ConfigLoader;
import space.devport.wertik.stattrak.Main;
import space.devport.wertik.stattrak.util.NBTEditor;
import space.devport.wertik.stattrak.util.NBTUtils;
import space.devport.wertik.stattrak.util.Utils;

public class StatTrakHandler {

    private ConfigLoader configLoader;

    public StatTrakHandler() {
        configLoader = Main.inst.getConfigLoader();
    }

    /**
     * Remove StatTrak lore and NBT data from an {@link @item}
     *
     * @param item Item to remove StatTrak from.
     * @return Item stack with StatTrak lore and NBT removed.
     */
    public ItemStack removeStatTrak(ItemStack item) {
        // NBT
        item = NBTEditor.removeNBT(item, "stattrak_kills");

        // Lore
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE + item.getType().toString().toUpperCase());
        item.setItemMeta(itemMeta);

        return item;
    }

    /**
     * Sets StatTrak lore and NBT data for {@link @item}
     *
     * @param item     Item you want to apply StatTrak to.
     * @param value    New StatTrak kill value.
     * @param oldValue Old StatTrak kill value.
     * @return Item with changed NBT and lore.
     */
    public ItemStack setStatTrak(ItemStack item, int value, int oldValue) {
        // Change NBT
        item = NBTEditor.writeNBT(item, "stattrak_kills", String.valueOf(value));

        ItemMeta meta = item.getItemMeta();

        // Change item name
        if (meta.getDisplayName() == null)
            // Set to normalized name
            meta.setDisplayName(Utils.color(configLoader.getLine().replace("%value%", configLoader.getValueFormat().replace("%value%", String.valueOf(value))).replace("%defaultName%", configLoader.getNormalizedName(item.getType().toString()))));
        else {
            String name = meta.getDisplayName();

            name = name.replace(" " + configLoader.getValueFormat().replace("%value%", String.valueOf(oldValue)), "");

            // Set this display name
            meta.setDisplayName(Utils.color(configLoader.getLine().replace("%value%", configLoader.getValueFormat().replace("%value%", String.valueOf(value))).replace("%defaultName%", name)));
        }

        item.setItemMeta(meta);

        return item;
    }

    /**
     * Used to add a point to StatTrak {@link @item} value.
     *
     * @param item Item stack that has StatTrak already applied to it.
     * @return Edited item stack.
     */
    public ItemStack addToStatTrakValue(ItemStack item) {
        int kills = getStatTrakValue(item);
        return setStatTrak(item, kills + 1, kills);
    }

    /**
     * Whether or not is an item {@link @type} StatTrakable.
     *
     * @param type Material name to test.
     * @return boolean Whether or not the item is StatTrakable.
     */
    public boolean isStatTrakable(String type) {
        return configLoader.getWeaponTypes().contains(type);
    }

    public boolean isCountable(String type) {
        return configLoader.getEntityTypes().contains(type);
    }

    /**
     * Does {@link @item} have already stattrak on it?
     *
     * @param item Item to test.
     * @return Whether item has StatTrak or not.
     */
    public boolean hasStatTrak(ItemStack item) {
        return NBTEditor.hasNBTTag(item, "stattrak_kills");
    }

    /**
     * Returns value of an NBT data {@link @key}
     *
     * @param item Item stack to load from
     * @return Integer value
     */
    public int getStatTrakValue(ItemStack item) {
        return Integer.parseInt(NBTUtils.strip(NBTEditor.getNBT(item, "stattrak_kills")));
    }
}