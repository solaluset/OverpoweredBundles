package org.vinerdream.overpoweredBundles.listeners;

import org.bukkit.block.Crafter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.vinerdream.overpoweredBundles.OverpoweredBundles;
import org.vinerdream.overpoweredBundles.items.CustomBundle;

import java.util.Arrays;

public class CraftListener implements Listener {
    private final OverpoweredBundles plugin;

    public CraftListener(OverpoweredBundles plugin) {
        this.plugin = plugin;
    }

    private boolean checkCancellation(ItemStack[] ingredients, ItemStack result) {
        for (CustomBundle customBundle : plugin.getCustomBundles()) {
            if (customBundle.isBundle(result)) {
                for (ItemStack item : ingredients) {
                    if (item != null && item.getItemMeta() instanceof BundleMeta meta && meta.hasItems()) {
                        return true;
                    }
                }
            }
            if (Arrays.stream(ingredients).anyMatch(customBundle::isBundle)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (checkCancellation(event.getInventory().getMatrix(), event.getInventory().getResult())) {
            event.getInventory().setResult(null);
        }
    }

    @EventHandler
    public void onCrafterCraft(CrafterCraftEvent event) {
        final Crafter crafter = (Crafter) event.getBlock().getState();
        if (checkCancellation(crafter.getInventory().getContents(), event.getResult())) {
            event.setCancelled(true);
        }
    }
}
