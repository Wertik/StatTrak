package wertik.stattrak.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import wertik.stattrak.ConfigLoader;
import wertik.stattrak.Main;
import wertik.stattrak.handlers.StattrakHandler;

public class CommandHandler implements CommandExecutor {

    private ConfigLoader configLoader;
    private Main plugin;
    private StattrakHandler stattrakHandler;

    public CommandHandler() {
        plugin = Main.getInstance();
        configLoader = plugin.getConfigLoader();
        stattrakHandler = plugin.getStattrakHandler();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            ItemStack weapon = p.getEquipment().getItemInMainHand();

            if (args.length == 0) {
                if (p.hasPermission("stattrak.apply") || p.isOp()) {
                    if (stattrakHandler.isStatTrakable(weapon.getType().toString())) {

                        // IF already stattrak-ed
                        if (!stattrakHandler.hasStatTrak(weapon)) {
                            p.sendMessage(configLoader.getFormattedMessage("stattrak-applied"));
                            // Apply stattrak for the first time
                            p.getInventory().setItemInMainHand(stattrakHandler.setStattrak(p.getInventory().getItemInMainHand(), "kills", 0));

                        // IF NOT
                        } else {
                            // Save kills
                            int kills = stattrakHandler.getStattrakValue(weapon, "kills");
                            // Remove old stattrak lore
                            ItemStack item = stattrakHandler.removeStatTrak(p.getInventory().getItemInMainHand());
                            // Reapply with a kills value
                            p.getInventory().setItemInMainHand(stattrakHandler.setStattrak(item, "kills", kills));
                            p.sendMessage(configLoader.getFormattedMessage("stattrak-reapplied"));
                        }
                    } else
                        p.sendMessage(configLoader.getFormattedMessage("not-stattrakable"));
                } else
                    p.sendMessage(configLoader.getFormattedMessage("no-permission"));

            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reset")) {
                    if (p.hasPermission("stattrak.apply") || p.isOp()) {
                        if (stattrakHandler.isStatTrakable(weapon.getType().toString())) {
                            // Remove stattrak
                            ItemStack item = stattrakHandler.removeStatTrak(p.getInventory().getItemInMainHand());
                            // Reapply
                            p.getInventory().setItemInMainHand(stattrakHandler.setStattrak(item, "kills", 0));
                            p.sendMessage(configLoader.getFormattedMessage("stattrak-reset"));
                        } else
                            p.sendMessage(configLoader.getFormattedMessage("not-stattrakable"));
                    } else
                        p.sendMessage(configLoader.getFormattedMessage("no-permission"));

                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("stattrak.reload")) {
                        plugin.reloadConfig();
                        configLoader.loadWeaponTypes(sender);
                        configLoader.setStrings(sender);
                        sender.sendMessage(configLoader.getFormattedMessage("reloaded"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    p.sendMessage("§8§m----§r §eStatTrak Help Page §8§m----§r");
                    p.sendMessage("§e/stattrak help §8- §7This..");
                    p.sendMessage("§e/stattrak reset §8- §7Resets your stattrak stat.");
                    if (p.hasPermission("stattrak.reload") || p.isOp())
                        p.sendMessage("§e/stattrak reload §8- §7Reloads plugin configuration.");
                    p.sendMessage("§e/stattrak §8- §7Applies stattrak to your item.");
                    p.sendMessage("§e/stattrak remove §8- §7Removes stattrak from your item.");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    p.getInventory().setItemInMainHand(stattrakHandler.removeStatTrak(weapon));
                    p.sendMessage(configLoader.getFormattedMessage("stattrak-removed"));
                }
            } else {
                p.sendMessage("§8§m----§r §eStatTrak Help Page §8§m----§r");
                p.sendMessage("§e/stattrak help §8- §7This..");
                p.sendMessage("§e/stattrak reset §8- §7Resets your stattrak stat.");
                if (p.hasPermission("stattrak.reload") || p.isOp())
                    p.sendMessage("§e/stattrak reload §8- §7Reloads plugin configuration.");
                p.sendMessage("§e/stattrak §8- §7Applies stattrak to your item.");
                p.sendMessage("§e/stattrak remove §8- §7Removes stattrak from your item.");
            }
        }

        return false;
    }
}
