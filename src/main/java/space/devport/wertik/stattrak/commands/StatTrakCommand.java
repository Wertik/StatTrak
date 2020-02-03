package space.devport.wertik.stattrak.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.wertik.stattrak.ConfigLoader;
import space.devport.wertik.stattrak.Main;
import space.devport.wertik.stattrak.handlers.StatTrakHandler;

public class StatTrakCommand implements CommandExecutor {

    private Main plugin;

    private ConfigLoader configLoader;
    private StatTrakHandler stattrakHandler;

    public StatTrakCommand() {
        plugin = Main.inst;

        configLoader = plugin.getConfigLoader();
        stattrakHandler = plugin.getStattrakHandler();
    }

    private void help(CommandSender s, String label) {
        s.sendMessage("§8§m--------§e StatTrak §7v.§f" + plugin.getDescription().getVersion() + " §8§m--------§r" +
                "\n§e/" + label + " reload §8- §7Reload the plugin." +
                "\n§e/" + label + " §8- §7Apply StatTrak on your item." +
                "\n§e/" + label + " help §8- §7This." +
                "\n§e/" + label + " reset §8- §7Reset your StatTrak kills." +
                "\n§e/" + label + " remove §8- §7Remove StatTrak from your item." +
                "\n§e/" + label + " info §8- §7Weapon and entity info.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            ItemStack weapon = p.getItemInHand();

            // Apply
            if (args.length == 0) {
                if (!p.hasPermission("stattrak.apply") && !p.isOp()) {
                    p.sendMessage(configLoader.getFormattedMessage("no-permission"));
                    return true;
                }

                if (!stattrakHandler.isStatTrakable(weapon.getType().toString())) {
                    p.sendMessage(configLoader.getFormattedMessage("not-stattrakable"));
                    return true;
                }

                if (stattrakHandler.hasStatTrak(weapon)) {
                    ItemStack item = p.getInventory().getItemInHand();

                    // Save kills
                    int kills = stattrakHandler.getStatTrakValue(weapon);

                    // Reapply with a kills value
                    p.setItemInHand(stattrakHandler.setStatTrak(item, kills, kills));

                    p.sendMessage(configLoader.getFormattedMessage("stattrak-reapplied"));
                } else {
                    // Apply stattrak for the first time
                    p.setItemInHand(stattrakHandler.setStatTrak(p.getItemInHand(), 0, 0));
                    p.sendMessage(configLoader.getFormattedMessage("stattrak-applied"));
                }

                return true;
            }

            switch (args[0].toLowerCase()) {
                case "reload":
                    if (sender.hasPermission("stattrak.reload")) {
                        plugin.reloadConfig();

                        configLoader.loadWeaponTypes(sender);
                        configLoader.loadEntityTypes(sender);
                        configLoader.setStrings(sender);

                        sender.sendMessage(configLoader.getFormattedMessage("reloaded"));

                        return true;
                    }
                    break;
                case "reset":
                    if (p.hasPermission("stattrak.apply") || p.isOp()) {
                        if (stattrakHandler.isStatTrakable(weapon.getType().toString())) {
                            // Remove stattrak
                            ItemStack item = stattrakHandler.removeStatTrak(p.getInventory().getItemInHand());
                            // Reapply
                            p.getInventory().setItemInHand(stattrakHandler.setStatTrak(item, 0, 0));
                            p.sendMessage(configLoader.getFormattedMessage("stattrak-reset"));
                        } else
                            p.sendMessage(configLoader.getFormattedMessage("not-stattrakable"));
                    } else
                        p.sendMessage(configLoader.getFormattedMessage("no-permission"));
                    break;
                case "info":
                    p.sendMessage("§8§m--------§r §eStatTrak Info Page §8§m--------§r");
                    p.sendMessage("§aWeapon types:");
                    for (String type : configLoader.getWeaponTypes())
                        p.sendMessage("§8 - §e" + type);
                    p.sendMessage("§aEntity types:");
                    for (String type : configLoader.getEntityTypes())
                        p.sendMessage("§8 - §e" + type);
                    break;
                case "remove":
                    p.getInventory().setItemInHand(stattrakHandler.removeStatTrak(weapon));
                    p.sendMessage(configLoader.getFormattedMessage("stattrak-removed"));
                    break;
                case "help":
                default:
                    help(sender, label);
            }
        }

        return false;
    }
}
