package org.vinerdream.overpoweredBundles.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.vinerdream.overpoweredBundles.OverpoweredBundles;
import org.vinerdream.overpoweredBundles.items.CustomBundle;

public class CraftListener implements Listener {
    private final OverpoweredBundles plugin;

    public CraftListener(OverpoweredBundles plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        for (CustomBundle customBundle : plugin.getCustomBundles()) {
            if (customBundle.isBundle(event.getInventory().getResult())) {
                for (ItemStack item : event.getInventory().getMatrix()) {
                    if (item != null && item.getItemMeta() instanceof BundleMeta meta && meta.hasItems()) {
                        event.getInventory().setResult(null);
                        break;
                    }
                }
            }
        }
    }
}
