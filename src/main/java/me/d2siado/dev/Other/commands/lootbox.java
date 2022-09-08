package me.d2siado.dev.Other.commands;

import me.d2siado.dev.Lootbox;
import me.d2siado.dev.Utils.CC;
import me.d2siado.dev.Utils.Plugin;
import me.d2siado.dev.Utils.Stacks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class lootbox implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] strings) {
        final String command = cmd.getName();
        if (!(strings.length >= 1)) {
            Arrays.asList(
                    ("")
                    , ("&6&l" + Lootbox.getInstance().getName())
                    , ("  &e[*] &f/" + command + " give")
                    , ("  &e[*] &f/" + command + " loot")
                    , ("  &e[*] &f/" + command + " about")
                    , ("  &e[*] &f/" + command + " reload")
                    , ("")).forEach(m -> sender.sendMessage(CC.translate(m)));
            return true;
        } else if ("give".equalsIgnoreCase(strings[0])) {
            if (!(sender.hasPermission(Lootbox.getInstance().getName() + ".give") || sender.hasPermission(Lootbox.getInstance().getName() + ".*"))) {
                sender.sendMessage(CC.translate("&cYou not have permissions to use this subcommand..."));
                return true;
            }
            if (strings.length != 3) {
                sender.sendMessage(CC.translate("&cUse: /" + command + " give {player} {amount}"));
                return true;
            }
            final String playerarg = strings[1];
            Player target = Bukkit.getPlayer(playerarg);
            if (target == null) {
                sender.sendMessage(CC.translate("&c" + playerarg + " player doesnt exists..."));
                return true;
            } else if (!target.isOnline()) {
                sender.sendMessage(CC.translate("&c" + target.getName() + " inst online..."));
                return true;
            }
            if (!Plugin.isInt(strings[2])) {
                sender.sendMessage(CC.translate("&cEnter a valid item amount..."));
                return true;
            }
            final int amount = Integer.parseInt(strings[2]);
            if (Plugin.isFull(target)) {
                target.getWorld().dropItem(target.getLocation(), Stacks.getLootboxItem(amount));
                return true;
            }
            target.getInventory().addItem(Stacks.getLootboxItem(amount));
        } else if ("loot".equalsIgnoreCase(strings[0])) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate("&cOnly players can run this subcommand..."));
                return true;
            }
            ((Player) sender).openInventory(Stacks.getLootInventory((sender.hasPermission(Lootbox.getInstance().getName() + ".loot") || sender.hasPermission(Lootbox.getInstance().getName() + ".*"))));
        } else if ("about".equalsIgnoreCase(strings[0])) {
            Arrays.asList(("")
                    , ("&6&l" + Lootbox.getInstance().getName())
                    , ("  &eVersion:&f " + Lootbox.getInstance().getDescription().getVersion())
                    , ("  &eLast Update:&f " + Lootbox.getInstance().getLastUpdate())
                    , ("  &eAuthor:&f " + Lootbox.getInstance().getDescription().getAuthors().toString())
                    , ("")).forEach(m -> sender.sendMessage(CC.translate(m)));
        } else if ("reload".equalsIgnoreCase(strings[0])) {
            if (!(sender.hasPermission(Lootbox.getInstance().getName() + ".reload") || sender.hasPermission(Lootbox.getInstance().getName() + ".*"))) {
                sender.sendMessage(CC.translate("&cYou not have permissions to use this subcommand..."));
                return true;
            }
            try {
                reload();
            } catch (Error e) {
                sender.sendMessage(CC.translate("&cLootbox Plugin was found a error in the config files"));
                return true;
            }
            sender.sendMessage(CC.translate("&aLootbox Plugin was reloaded sussesfuly"));
        } else {
            sender.sendMessage(CC.translate("&cThe subcommand " + strings[0] + " was not founded, please use /" + command));
        }
        return false;
    }

    private void reload() {
        Lootbox.getInstance().getConfig().reload();
        Lootbox.getInstance().getData().reload();
    }
}
