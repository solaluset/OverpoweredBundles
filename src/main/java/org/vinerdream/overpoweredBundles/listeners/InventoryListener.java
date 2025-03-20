package org.vinerdream.overpoweredBundles.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.vinerdream.overpoweredBundles.OverpoweredBundles;
import org.vinerdream.overpoweredBundles.items.CustomBundle;
import org.vinerdream.overpoweredBundles.utils.ItemStackUtils;

public class InventoryListener implements Listener {
    private final OverpoweredBundles plugin;

    public InventoryListener(OverpoweredBundles plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE) || event.getSlot() < 0) return;
        if (event.isShiftClick()) {
            if (event.isRightClick()) {
                for (CustomBundle customBundle : plugin.getCustomBundles()) {
                    if (customBundle.isBundle(event.getCurrentItem())) {
                        customBundle.rotateItems(event.getCurrentItem());
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            return;
        }
        if (event.isLeftClick()) {
            for (CustomBundle customBundle : plugin.getCustomBundles()) {
                if (customBundle.isBundle(event.getCursor()) && customBundle.isSuitableForBundle(event.getCurrentItem())) {
                    if (customBundle.addItem(event.getCursor(), event.getCurrentItem())) {
                        event.setCurrentItem(null);
                    }
                    event.setCancelled(true);
                    break;
                } else if (customBundle.isBundle(event.getCurrentItem()) && customBundle.isSuitableForBundle(event.getCursor())) {
                    if (customBundle.addItem(event.getCurrentItem(), event.getCursor())) {
                        event.getWhoClicked().setItemOnCursor(null);
                    }
                    event.setCancelled(true);
                    break;
                }
            }
        } else if (event.isRightClick()) {
            for (CustomBundle customBundle : plugin.getCustomBundles()) {
                if (customBundle.isBundle(event.getCursor()) && ItemStackUtils.isEmpty(event.getCurrentItem())) {
                    final ItemStack item = customBundle.removeItem(event.getCursor());
                    if (item != null) {
                        event.setCurrentItem(item);
                    }
                    event.setCancelled(true);
                } else if (customBundle.isBundle(event.getCurrentItem()) && ItemStackUtils.isEmpty(event.getCursor())) {
                    final ItemStack item = customBundle.removeItem(event.getCurrentItem());
                    if (item != null) {
                        event.getWhoClicked().setItemOnCursor(item);
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}
