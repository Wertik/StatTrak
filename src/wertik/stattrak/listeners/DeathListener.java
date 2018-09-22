package wertik.stattrak.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import wertik.stattrak.ConfigLoader;
import wertik.stattrak.Utils;

public class DeathListener implements Listener {

    ConfigLoader cload = new ConfigLoader();
    Utils utils = new Utils();

    public DeathListener() {
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {

        if (e.getEntity().getType().equals(EntityType.PLAYER)) {

            if (e.getEntity().getKiller() instanceof Player) {

                Player p = e.getEntity().getKiller();

                ItemStack weapon = p.getInventory().getItemInMainHand();

                // Weapon check, no need for lore if it's not stattrakable
                if (!cload.isStatTrakable(weapon.getType().toString()))
                    return;

                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), utils.recreateItem(weapon));
            }
        }
    }
}

