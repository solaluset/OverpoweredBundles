package org.vinerdream.overpoweredBundles.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.vinerdream.overpoweredBundles.OverpoweredBundles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EnchantedBookBundle extends CustomBundle {
    private static final NamespacedKey BUNDLE_DATA_KEY = new NamespacedKey(OverpoweredBundles.INSTANCE, "book-bundle-data");

    @Override
    public ItemStack getEmptyBundle() {
        final ItemStack item = new ItemStack(Material.PAPER);
        final ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.itemName(OverpoweredBundles.INSTANCE.getConfig().getRichMessage("messages.enchanted-book-bundle"));
        meta.getPersistentDataContainer().set(
                BUNDLE_DATA_KEY,
                PersistentDataType.BYTE_ARRAY,
                ItemStack.serializeItemsAsBytes(new ItemStack[0])
        );
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean isBundle(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(BUNDLE_DATA_KEY);
    }

    @Override
    public boolean isSuitableForBundle(ItemStack item) {
        return item != null && item.getType().equals(Material.ENCHANTED_BOOK);
    }

    @Override
    public boolean addItem(ItemStack bundle, ItemStack item) {
        final ItemMeta meta = bundle.getItemMeta();
        ItemStack[] items = ItemStack.deserializeItemsFromBytes(Objects.requireNonNull(
                meta.getPersistentDataContainer().get(BUNDLE_DATA_KEY, PersistentDataType.BYTE_ARRAY)
        ));
        if (items.length >= OverpoweredBundles.INSTANCE.getConfig().getInt("enchanted-book-bundle-capacity")) {
            return false;
        }
        items = Arrays.copyOf(items, items.length + 1);
        items[items.length - 1] = item;
        meta.getPersistentDataContainer().set(BUNDLE_DATA_KEY, PersistentDataType.BYTE_ARRAY, ItemStack.serializeItemsAsBytes(items));
        bundle.setItemMeta(meta);
        return true;
    }

    @Override
    public ItemStack removeItem(ItemStack bundle) {
        final ItemMeta meta = bundle.getItemMeta();
        final ItemStack[] items = ItemStack.deserializeItemsFromBytes(Objects.requireNonNull(
                meta.getPersistentDataContainer().get(BUNDLE_DATA_KEY, PersistentDataType.BYTE_ARRAY)
        ));
        if (items.length == 0) {
            return null;
        }
        final List<ItemStack> itemList = new ArrayList<>(Arrays.stream(items).toList());
        final ItemStack item = itemList.removeFirst();
        meta.getPersistentDataContainer().set(
                BUNDLE_DATA_KEY,
                PersistentDataType.BYTE_ARRAY,
                ItemStack.serializeItemsAsBytes(itemList.toArray(ItemStack[]::new))
        );
        bundle.setItemMeta(meta);
        return item;
    }
}
