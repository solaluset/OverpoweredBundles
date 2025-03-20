package org.vinerdream.overpoweredBundles.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.vinerdream.overpoweredBundles.OverpoweredBundles;

import java.util.*;

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
        meta.lore(getLore(items));
        bundle.setItemMeta(meta);
        return true;
    }

    @Override
    public ItemStack removeItem(ItemStack bundle) {
        final ItemMeta meta = bundle.getItemMeta();
        ItemStack[] items = ItemStack.deserializeItemsFromBytes(Objects.requireNonNull(
                meta.getPersistentDataContainer().get(BUNDLE_DATA_KEY, PersistentDataType.BYTE_ARRAY)
        ));
        if (items.length == 0) {
            return null;
        }
        final List<ItemStack> itemList = new ArrayList<>(Arrays.stream(items).toList());
        final ItemStack item = itemList.removeFirst();
        items = itemList.toArray(ItemStack[]::new);
        meta.getPersistentDataContainer().set(
                BUNDLE_DATA_KEY,
                PersistentDataType.BYTE_ARRAY,
                ItemStack.serializeItemsAsBytes(items)
        );
        meta.lore(getLore(items));
        bundle.setItemMeta(meta);
        return item;
    }

    private List<Component> getLore(ItemStack[] items) {
        final List<Component> components = new ArrayList<>();
        final List<Integer> counts = new ArrayList<>();
        for (ItemStack item : items) {
            if (!(item.getItemMeta() instanceof EnchantmentStorageMeta meta)) continue;
            Component component = Component.empty();
            final List<Map.Entry<Enchantment, Integer>> entries = new ArrayList<>(meta.getStoredEnchants().entrySet().stream().toList());
            final Map.Entry<Enchantment, Integer> lastEntry = entries.removeLast();
            for (Map.Entry<Enchantment, Integer> entry : entries) {
                component = component.append(Component.translatable(entry.getKey())).append(Component.text(" " + entry.getValue() + ", ")).decoration(TextDecoration.ITALIC, false);
            }
            component = component.append(Component.translatable(lastEntry.getKey())).append(Component.text(" " + lastEntry.getValue())).decoration(TextDecoration.ITALIC, false);
            final int index = components.indexOf(component);
            if (index == -1) {
                components.add(component);
                counts.add(1);
            } else {
                counts.add(index, counts.remove(index) + 1);
            }
        }
        final int maxLoreSize = OverpoweredBundles.INSTANCE.getConfig().getInt("max-lore-size");
        boolean hasMore = false;
        final List<Component> result = new ArrayList<>();
        for (int i = 0; i < components.size(); i++) {
            if (i == maxLoreSize) {
                hasMore = true;
                break;
            }
            if (counts.get(i) == 1) {
                result.add(components.get(i));
            } else {
                result.add(components.get(i).append(Component.text(" x" + counts.get(i))));
            }
        }
        if (hasMore) {
            result.add(Component.text(Objects.requireNonNull(
                    OverpoweredBundles.INSTANCE.getConfig().getString("messages.lore-more")
            ).replace("%count%", String.valueOf(components.size() - maxLoreSize))));
        }
        return result;
    }
}
