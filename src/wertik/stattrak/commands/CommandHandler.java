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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            ItemStack weapon = p.getEquipment().getItemInMainHand();

            if (args.length == 0) {
                if (p.hasPermission("stattrak.apply") || p.isOp()) {
                    if (stattrakHandler.isStatTrakable(weapon.getType().toString())) {
                        if (!stattrakHandler.hasStatTrak(weapon)) {
                            p.sendMessage(configLoader.getFormattedMessage("stattrak-applied"));
                            p.getInventory().setItem(p.getInventory().getHeldItemSlot(), stattrakHandler.setStattrak(weapon, 0));

                        } else {
                            int kills = stattrakHandler.getKills(weapon);
                            ItemStack item = stattrakHandler.removeStatTrak(p.getInventory().getItemInMainHand());
                            p.getInventory().setItemInMainHand(stattrakHandler.setStattrak(item, kills));
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
                            ItemStack item = stattrakHandler.removeStatTrak(p.getInventory().getItemInMainHand());
                            p.getInventory().setItemInMainHand(stattrakHandler.setStattrak(item, 0));
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
                    p.sendMessage("§e/stattrak help §8-- §7This..");
                    p.sendMessage("§e/stattrak reset §8-- §7Resets your stattrak stat.");
                    if (p.hasPermission("stattrak.reload") || p.isOp())
                        p.sendMessage("§e/stattrak reload §8-- §7Reloads plugin configuration.");
                    p.sendMessage("§e/stattrak §8-- §7Applies stattrak to your item.");
                    p.sendMessage("§e/stattrak remove §8-- §7Removes stattrak from your item.");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    p.getInventory().setItemInMainHand(stattrakHandler.removeStatTrak(weapon));
                    p.sendMessage(configLoader.getFormattedMessage("stattrak-removed"));
                }
            } else {
                p.sendMessage("§8§m----§r §eStatTrak Help Page §8§m----§r");
                p.sendMessage("§e/stattrak help §8-- §7This..");
                p.sendMessage("§e/stattrak reset §8-- §7Resets your stattrak stat.");
                if (p.hasPermission("stattrak.reload") || p.isOp())
                    p.sendMessage("§e/stattrak reload §8-- §7Reloads plugin configuration.");
                p.sendMessage("§e/stattrak §8-- §7Applies stattrak to your item.");
            }
        }

        return false;
    }
}
