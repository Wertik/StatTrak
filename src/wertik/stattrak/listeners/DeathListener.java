package wertik.stattrak.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import wertik.stattrak.ConfigLoader;
import wertik.stattrak.Main;
import wertik.stattrak.Utils;

public class DeathListener implements Listener {

    private ConfigLoader configLoader;
    private Utils utils;

    public DeathListener() {
        configLoader = Main.getInstance().getConfigLoader();
        utils = Main.getInstance().getUtils();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {

        if (e.getEntity().getType().equals(EntityType.PLAYER)) {

            if (e.getEntity().getKiller() instanceof Player) {

                Player p = e.getEntity().getKiller();

                ItemStack weapon = p.getInventory().getItemInMainHand();

                // Weapon check, no need for lore if it's not stattrakable
                if (!configLoader.isStatTrakable(weapon.getType().toString()))
                    return;

                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), utils.recreateItem(weapon));
            }
        }
    }
}

