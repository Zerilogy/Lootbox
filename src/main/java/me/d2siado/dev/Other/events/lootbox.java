package me.d2siado.dev.Other.events;

import me.d2siado.dev.LootBox;
import me.d2siado.dev.Utils.CC;
import me.d2siado.dev.Utils.Plugin;
import me.d2siado.dev.Utils.Stacks;
import org.bukkit.Effect;
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
                e.setCancelled(true);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(LootBox.getInstance().getConfig().getString("CONFIG.SOUNDS.OPEN")), 1.0F, 1.0F);
                for (int i = 0; i < LootBox.getInstance().getConfig().getInt("CONFIG.PARTICLES.AMOUNT"); i++) {
                    if (LootBox.getInstance().getConfig().getString("CONFIG.PARTICLES.OPEN").equalsIgnoreCase("None")) {
                        return;
                    }
                    e.getPlayer().playEffect(e.getPlayer().getLocation(), Effect.valueOf(LootBox.getInstance().getConfig().getString("CONFIG.PARTICLES.OPEN")), (short)0);
                }
                if (e.getItem().getAmount() > 1) {
                    e.getItem().setAmount(e.getItem().getAmount()-1);
                } else {
                    e.getPlayer().setItemInHand(null);
                }
                if (e.getPlayer().isSneaking() && LootBox.getInstance().getConfig().getBoolean("CONFIG.FAST-OPEN")) {
                    int repeats = (LootBox.getInstance().getConfig().getInt("CONFIG.AMOUNT")==1)?1:2;
                    for (int i = 0; i < repeats; i++) {
                        ItemStack stack = Plugin.getRandomItem().clone();
                        String reward = (stack.getItemMeta().getDisplayName() != null) ? stack.getItemMeta().getDisplayName() : stack.getType().name();
                        e.getPlayer().sendMessage(CC.translate(LootBox.getInstance().getConfig().getString("MESSAGES.RECEIVE").replace("%amount%", String.valueOf(stack.getAmount())).replace("%reward%", reward)));
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(LootBox.getInstance().getConfig().getString("CONFIG.SOUNDS.OPEN")), 1.0F, 1.0F);
                        if (!Plugin.isFull(e.getPlayer())) {
                            e.getPlayer().getInventory().addItem(stack);
                        } else {
                            e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), stack);
                        }
                    }
                    return;
                }
                e.getPlayer().openInventory(Stacks.getBoxInventory());
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
                for (int i = 0; i < LootBox.getInstance().getConfig().getInt("CONFIG.PARTICLES.AMOUNT"); i++) {
                    if (LootBox.getInstance().getConfig().getString("CONFIG.PARTICLES.REWARDS").equalsIgnoreCase("None")) {
                        return;
                    }
                    p.playEffect(p.getLocation(), Effect.valueOf(LootBox.getInstance().getConfig().getString("CONFIG.PARTICLES.REWARDS")), (short)0);
                }
                ItemStack stack = Plugin.getRandomItem().clone();
                String reward = (stack.getItemMeta().getDisplayName() != null) ? stack.getItemMeta().getDisplayName() : stack.getType().name();
                p.sendMessage(CC.translate(LootBox.getInstance().getConfig().getString("MESSAGES.RECEIVE").replace("%amount%", String.valueOf(stack.getAmount())).replace("%reward%", reward)));
                e.getInventory().setItem(e.getRawSlot(), stack);
                p.playSound(p.getLocation(), Sound.valueOf(LootBox.getInstance().getConfig().getString("CONFIG.SOUNDS.REWARDS")), 1.0F, 1.0F);
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
                    if (LootBox.getInstance().getData().get("ITEMS." + ib) != null) {
                        ItemStack stack = LootBox.getInstance().getData().getItemStack("ITEMS." + ib + ".ITEM").clone();
                        if (stack.equals(stacks)) continue;
                    }
                    LootBox.getInstance().getData().set("ITEMS." + ib + ".ITEM", stacks);
                    LootBox.getInstance().getData().saveAll();
                } else {
                    LootBox.getInstance().getData().set("ITEMS." + ib, null);
                    LootBox.getInstance().getData().saveAll();
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
                    p.sendMessage(CC.translate(LootBox.getInstance().getConfig().getString("MESSAGES.RECEIVE").replace("%amount%", String.valueOf(stack.getAmount())).replace("%reward%", reward)));
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
