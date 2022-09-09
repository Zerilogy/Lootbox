package me.d2siado.dev.Utils;

import me.d2siado.dev.LootBox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class CC {
    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static List<String> translate(List<String> msg) {
        return msg.stream().map(CC::translate).collect(Collectors.toList());
    }

    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(msg));
    }

    public static void log(List<String> msg) {
        msg.forEach(msgs -> Bukkit.getConsoleSender().sendMessage(CC.translate(msgs)));
    }

    public static void logWith(String msg) {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&e[&b" + LootBox.getInstance().getName() + "&e]&r " + msg));
    }

    public static void logWith(List<String> msg) {
        msg.forEach(msgs -> Bukkit.getConsoleSender().sendMessage(CC.translate("&e[&b" + LootBox.getInstance().getName() + "&e]&r " + msgs)));
    }
}
