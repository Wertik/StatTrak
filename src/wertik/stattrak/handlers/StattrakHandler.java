package wertik.stattrak.handlers;

import com.sun.istack.internal.NotNull;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import wertik.stattrak.ConfigLoader;
import wertik.stattrak.Main;
import wertik.stattrak.util.NBTEditor;
import wertik.stattrak.util.NBTUtils;
import wertik.stattrak.util.Utils;

public class StattrakHandler {

    private Main plugin;

    private ConfigLoader configLoader;

    public StattrakHandler() {
        plugin = Main.getInstance();

        configLoader = plugin.getConfigLoader();
    }

    /**
     * Remove stattrak lore and NBT data from an {@link @item}
     *
     * @return Item stack with stattrak lore and NBT removed
     * @item Item to remove stattrak from
     */

    public ItemStack removeStatTrak(@NotNull ItemStack item, String key) {
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
     * @return @item with changed NBT and lore data
     * @item Item you want to apply stattrak to
     * @key NBT data key
     * @value NBT data value
     */

    public ItemStack setStattrak(@NotNull ItemStack item, String key, int value, int oldValue) {
        // NBT
        item = NBTEditor.writeNBT(item, "stattrak_" + key, String.valueOf(value));

        // Item name
        ItemMeta meta = item.getItemMeta();

        if (meta.getDisplayName() == null)
            // Set to normalized name
            meta.setDisplayName(Utils.color(configLoader.getStatTrakLine().replace("%value%", configLoader.getValueFormat().replace("%value%", String.valueOf(value))).replace("%defaultName%", configLoader.getNormalizedName(item.getType().toString()))));
        else {
            String name = meta.getDisplayName();

            name = name.replace(" " + configLoader.getValueFormat().replace("%value%", String.valueOf(oldValue)), "");

            // Set this display name
            meta.setDisplayName(Utils.color(configLoader.getStatTrakLine().replace("%value%", configLoader.getValueFormat().replace("%value%", String.valueOf(value))).replace("%defaultName%", name)));
        }

        item.setItemMeta(meta);

        return item;
    }

    /**
     * Used to add a point to stattrak {@link @item} value
     *
     * @return Edited item stack
     * @item Item stack that has stattrak already applied to it
     * @key NBT data key to rewrite
     */

    public ItemStack addToStattrakValue(@NotNull ItemStack item, @NotNull String key) {
        int kills = getStattrakValue(item, key);
        return setStattrak(item, key, kills + 1, kills);
    }

    /**
     * Whether or not is an item {@link @type} stattrakable
     *
     * @return Boolean stattrakable
     * @type Material name to test
     */
    public boolean isStatTrakable(String type) {
        return configLoader.getWeaponTypes().contains(type);
    }

    public boolean isCountable(String type) {
        return configLoader.getEntityTypes().contains(type);
    }

    /**
     * Does {@link @item} have already stattrak on it
     *
     * @return boolean
     * @item Item to test
     */

    public boolean hasStatTrak(@NotNull ItemStack item) {
        return NBTEditor.hasNBTTag(item, "stattrak_kills");
    }

    /**
     * Returns value of an NBT data {@link @key}
     *
     * @return Integer value
     * @item Item stack to load from
     * @key NBT data key to look for
     */

    public int getStattrakValue(@NotNull ItemStack item, String key) {
        return Integer.valueOf(NBTUtils.strip(NBTEditor.getNBT(item, "stattrak_" + key)));
    }
}
