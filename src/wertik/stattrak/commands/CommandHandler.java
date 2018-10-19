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

    private Utils utils;
    private ConfigLoader configLoader;
    private Main plugin;

    public CommandHandler() {
        plugin = Main.getInstance();
        configLoader = plugin.getConfigLoader();
        utils = plugin.getUtils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("pvpc.stattrak.reload")) {
                    plugin.reloadConfig();
                    configLoader.loadWeaponTypes(sender);
                    configLoader.setStrings(sender);
                    sender.sendMessage(configLoader.getFormattedMessage("reloaded"));
                    return true;
                }
            }
        }

        if (sender instanceof Player) {

            Player p = (Player) sender;

            ItemStack weapon = p.getEquipment().getItemInMainHand();

            if (p.hasPermission("stattrak.apply") || p.isOp()) {

                if (configLoader.isStatTrakable(weapon.getType().toString())) {

                    p.sendMessage(configLoader.getFormattedMessage("stattrak-applied"));
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(), utils.recreateItem(weapon));

                } else
                    p.sendMessage(configLoader.getFormattedMessage("not-stattrakable"));
            } else
                p.sendMessage(configLoader.getFormattedMessage("no-permission"));
        }
        return false;
    }
}
