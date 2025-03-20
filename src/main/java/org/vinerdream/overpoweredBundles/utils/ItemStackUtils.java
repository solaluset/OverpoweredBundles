package org.vinerdream.overpoweredBundles.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtils {
    public static boolean isEmpty(ItemStack item) {
        return item == null || item.getType().equals(Material.AIR);
    }
}
