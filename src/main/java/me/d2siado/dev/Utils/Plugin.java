package me.d2siado.dev.Utils;

import com.google.common.collect.Lists;
import me.d2siado.dev.LootBox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class Plugin {
    public static boolean isInt(String number) {
        if (number == null) return false;
        try {
            int i = Integer.parseInt(number);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isFull(Player p) {
        return p.getInventory().firstEmpty() == -1;
    }
    public static boolean isFull(HumanEntity p) {
        return p.getInventory().firstEmpty() == -1;
    }

    public static void registerListeners(List<Listener> listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, LootBox.getInstance());
        }
    }

    public static ItemStack getRandomItem() {
        Random random = new Random();
        List<String> items = Lists.newArrayList();
        if (LootBox.getInstance().getData().getConfigurationSection("ITEMS") == null
        || LootBox.getInstance().getData().getConfigurationSection("ITEMS").getKeys(false).isEmpty()) {
            ItemStack stack = new ItemStack(Material.PAPER, 1);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(CC.translate("&cI dont find any reward"));
            stack.setItemMeta(meta);
            return stack;
        }
        LootBox.getInstance().getData().getConfigurationSection("ITEMS").getKeys(false).forEach((s) -> {
            ItemStack stack = LootBox.getInstance().getData().getItemStack("ITEMS." + s + ".ITEM").clone();
            if (stack != null && !stack.getType().equals(Material.AIR) && !stack.getType().equals(Material.STAINED_GLASS_PANE)) {
                items.add(s);
            }
        });
        return LootBox.getInstance().getData().getItemStack("ITEMS." + items.get(random.nextInt(items.size())) + ".ITEM");
    }
}
