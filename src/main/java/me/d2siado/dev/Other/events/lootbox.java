package me.d2siado.dev.Other.events;

import me.d2siado.dev.Lootbox;
import me.d2siado.dev.Utils.CC;
import me.d2siado.dev.Utils.Plugin;
import me.d2siado.dev.Utils.Stacks;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class lootbox implements Listener {
    @EventHandler
    public void onOpen(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (e.getItem() != null && e.getItem().isSimilar(Stacks.getLootboxItem(1))) {
                e.getPlayer().openInventory(Stacks.getBoxInventory());
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CHEST_OPEN, 1.0F, 1.0F);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || e.getInventory() == null || e.getWhoClicked() == null
        || e.getCurrentItem() == null || (!(e.getWhoClicked() instanceof Player))) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(Stacks.getBoxInventory().getTitle())
        && e.getClickedInventory().equals(e.getInventory())) {
            e.setCancelled(true);
            if (e.getCurrentItem().isSimilar(Stacks.getRandomItem())) {
                ItemStack stack = Plugin.getRandomItem().clone();
                String reward = (stack.getItemMeta().getDisplayName() != null) ? stack.getItemMeta().getDisplayName() : stack.getType().name();
                p.sendMessage(CC.translate(Lootbox.getInstance().getConfig().getString("MESSAGES.RECEIVE").replace("%amount%", String.valueOf(stack.getAmount())).replace("%reward%", reward)));
                e.getInventory().setItem(e.getRawSlot(), stack);
                if (!Plugin.isFull(p)) {
                    p.getInventory().addItem(stack);
                    return;
                }
                p.getWorld().dropItem(p.getLocation(), stack);
            }
            p.updateInventory();
        }
        else if (e.getView().getTitle().equals(Stacks.getLootInventory(false).getTitle())) {
            e.setCancelled(true);
            p.updateInventory();
        }
    }

    @EventHandler
    public void onEdit(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }
        if (e.getView().getTitle().equals(Stacks.getLootInventory(true).getTitle())) {
            ItemStack[] content = e.getInventory().getContents();
            byte ib = 0;
            for (ItemStack stacks : content) {
                ++ib;
                if (stacks != null && !stacks.getType().equals(Material.AIR)) {
                    if (Lootbox.getInstance().getData().get("ITEMS." + ib) != null) {
                        ItemStack stack = Lootbox.getInstance().getData().getItemStack("ITEMS." + ib + ".ITEM").clone();
                        if (stack.isSimilar(stacks)) continue;
                    }
                    Lootbox.getInstance().getData().set("ITEMS." + ib + ".ITEM", stacks);
                    Lootbox.getInstance().getData().saveAll();
                } else {
                    Lootbox.getInstance().getData().set("ITEMS." + ib, null);
                    Lootbox.getInstance().getData().saveAll();
                }
            }
            ((Player) e.getPlayer()).sendMessage(CC.translate("&aYou sussesfuly edited the lootbox loot"));
        }
        else if (e.getView().getTitle().equals(Stacks.getBoxInventory().getTitle())) {
            Player p = (Player) e.getPlayer();
            for (int i = 0; i < e.getInventory().getSize(); i++) {
                if (e.getInventory().getItem(i).isSimilar(Stacks.getRandomItem())) {
                    ItemStack stack = Plugin.getRandomItem().clone();
                    String reward = (stack.getItemMeta().getDisplayName() != null) ? stack.getItemMeta().getDisplayName() : stack.getType().name();
                    p.sendMessage(CC.translate(Lootbox.getInstance().getConfig().getString("MESSAGES.RECEIVE").replace("%amount%", String.valueOf(stack.getAmount())).replace("%reward%", reward)));
                    if (!Plugin.isFull(p)) {
                        p.getInventory().addItem(stack);
                        p.updateInventory();
                    } else {
                        p.getWorld().dropItem(p.getLocation(), stack);
                    }
                }
            }
        }
    }
}
