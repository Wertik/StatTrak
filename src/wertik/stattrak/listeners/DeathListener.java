package wertik.stattrak.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import wertik.stattrak.Main;
import wertik.stattrak.handlers.StattrakHandler;

public class DeathListener implements Listener {

    private StattrakHandler stattrakHandler;

    public DeathListener() {
        stattrakHandler = Main.getInstance().getStattrakHandler();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {

        if (e.getEntity().getKiller() instanceof Player) {

            if (stattrakHandler.isCountable(e.getEntityType().toString().toUpperCase())) {

                Player p = e.getEntity().getKiller();
                ItemStack weapon = p.getItemInHand();

                // Check for stattrak
                if (stattrakHandler.isStatTrakable(weapon.getType().toString()) && stattrakHandler.hasStatTrak(weapon))
                    // Add one kill to stattrak
                    p.setItemInHand(stattrakHandler.addToStattrakValue(weapon, "kills"));
            }
        }
    }
}

