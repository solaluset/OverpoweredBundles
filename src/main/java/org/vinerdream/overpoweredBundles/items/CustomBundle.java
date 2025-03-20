package org.vinerdream.overpoweredBundles.items;

import org.bukkit.inventory.ItemStack;

public abstract class CustomBundle {
    public abstract ItemStack getEmptyBundle();

    public abstract boolean isBundle(ItemStack item);

    public abstract boolean isSuitableForBundle(ItemStack item);

    public abstract boolean addItem(ItemStack bundle, ItemStack item);

    public abstract ItemStack removeItem(ItemStack bundle);

    public abstract void rotateItems(ItemStack bundle);
}
