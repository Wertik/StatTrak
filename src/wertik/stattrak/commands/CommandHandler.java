package wertik.stattrak.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import wertik.stattrak.ConfigLoader;
import wertik.stattrak.Main;
import wertik.stattrak.Utils;

public class CommandHandler implements CommandExecutor {

    private Utils utils = new Utils();
    private ConfigLoader cload = new ConfigLoader();
    private Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("pvpc.stattrak.reload")) {
                    plugin.reloadConfig();
                    cload.loadWeaponTypes(sender);
                    cload.setStrings(sender);
                    sender.sendMessage(cload.getFormattedMessage("reloaded"));
                    return true;
                }
            }
        }

        if (sender instanceof Player) {

            Player p = (Player) sender;

            ItemStack weapon = p.getEquipment().getItemInMainHand();

            if (cload.isStatTrakable(weapon.getType().toString())) {

                p.sendMessage(cload.getFormattedMessage("stattrak-applied"));
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), utils.recreateItem(weapon));

            } else
                p.sendMessage(cload.getFormattedMessage("not-stattrakable"));
        }
        return false;
    }
}
