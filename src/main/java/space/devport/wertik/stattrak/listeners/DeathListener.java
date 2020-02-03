package space.devport.wertik.stattrak.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import space.devport.wertik.stattrak.Main;
import space.devport.wertik.stattrak.handlers.StatTrakHandler;

public class DeathListener implements Listener {

    private StatTrakHandler stattrakHandler;

    public DeathListener() {
        stattrakHandler = Main.inst.getStattrakHandler();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null)
            return;

        if (!stattrakHandler.isCountable(e.getEntityType().toString().toUpperCase()))
            return;

        Player p = e.getEntity().getKiller();
        ItemStack weapon = p.getItemInHand();

        // Check for stattrak
        if (stattrakHandler.isStatTrakable(weapon.getType().toString()) && stattrakHandler.hasStatTrak(weapon))
            // Add one kill to stattrak
            p.setItemInHand(stattrakHandler.addToStatTrakValue(weapon));
    }
}

