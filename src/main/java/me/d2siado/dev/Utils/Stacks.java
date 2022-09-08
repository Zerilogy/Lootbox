package me.d2siado.dev.Utils;

import me.d2siado.dev.Lootbox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Stacks {
    public static ItemStack getLootboxItem(int amount) {
        ItemStack item = new ItemStack(Material.valueOf(Lootbox.getInstance().getConfig().getString("ICON.MATERIAL")), amount, (byte) Lootbox.getInstance().getConfig().getInt("ICON.MATERIAL-DATA"));
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(CC.translate(Lootbox.getInstance().getConfig().getString("ICON.DISPLAY-NAME")));
        meta.setLore(CC.translate(Lootbox.getInstance().getConfig().getStringList("ICON.LORE")));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getRandomItem() {
        ItemStack stack = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&aRandom Reward")); meta.setLore(CC.translate(Arrays.asList("&7» Click to recieve a random reward")));
        stack.setItemMeta(meta);
        return stack;
    }

    public static Inventory getBoxInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, CC.translate("&6Package Open"));
        for (int i = 0; i < inventory.getSize(); i++) {
            Random random = new Random();
            ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE, 1,
                    (Lootbox.getInstance().getConfig().getIntegerList("CONFIG.PANES.TYPES").get(random.nextInt(Lootbox.getInstance().getConfig().getIntegerList("CONFIG.PANES.TYPES").size()))).shortValue());
            ItemMeta meta = stack.getItemMeta();
            if (Lootbox.getInstance().getConfig().getBoolean("CONFIG.PANES.GLOW")) {
                Glow glow = new Glow(stack.getType().getId());
                meta.addEnchant(glow, 1, true);
            }
            meta.setDisplayName(CC.translate("&6"));
            stack.setItemMeta(meta);
            inventory.setItem(i, stack);
        }

        List<Integer> slots = (Lootbox.getInstance().getConfig().getInt("CONFIG.AMOUNT")==1)?Arrays.asList(13):Arrays.asList(12,14);
        for (Integer slot : slots) {
            inventory.setItem(slot, getRandomItem());
        }
        return inventory;
    }

    public static Inventory getLootInventory(Boolean status) {
        final String name = "&6Package Loot" + (status ? " (Editor)" : "");
        Inventory inventory = Bukkit.createInventory(null, 54, CC.translate(name));
        Lootbox.getInstance().getData().getConfigurationSection("ITEMS").getKeys(false).forEach((item) -> {
            int slot = Integer.parseInt(item) - 1;
            ItemStack stack = Lootbox.getInstance().getData().getItemStack("ITEMS." + item + ".ITEM").clone();
            inventory.setItem(slot, stack);
        });
        return inventory;
    }
}
