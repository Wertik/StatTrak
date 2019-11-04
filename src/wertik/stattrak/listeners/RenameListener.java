package wertik.stattrak.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import wertik.stattrak.Main;
import wertik.stattrak.util.NBTEditor;

public class RenameListener implements Listener {

    private Main plugin;

    public RenameListener() {
        plugin = Main.getInstance();
    }

    @EventHandler
    public void onRename(InventoryClickEvent e) {
        if (!e.getInventory().getType().equals(InventoryType.ANVIL))
            return;

        if (e.getSlot() != 2)
            return;

        if (e.getInventory().getItem(2) == null)
            return;

        if (e.getInventory().getItem(2).getType().equals(Material.AIR))
            return;

        if (!NBTEditor.hasNBTTag(e.getInventory().getItem(2), "stattrak_kills"))
            return;

        ItemStack item = e.getInventory().getItem(2);

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            int kills = plugin.getStattrakHandler().getStattrakValue(item, "kills");

            String del = plugin.getConfigLoader().getString("value-format").replace("%value%", String.valueOf(kills)).replace("&", "");

            meta.setDisplayName(meta.getDisplayName().replace(" " + del, ""));
            meta.setDisplayName(meta.getDisplayName().replace(del, ""));

            item.setItemMeta(meta);

            item = plugin.getStattrakHandler().setStattrak(item, "kills", plugin.getStattrakHandler().getStattrakValue(item, "kills"), plugin.getStattrakHandler().getStattrakValue(item, "kills"));

            e.setCurrentItem(item);
        }
    }
}