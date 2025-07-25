package org.vinerdream.overpoweredBundles.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.vinerdream.overpoweredBundles.OverpoweredBundles;

import java.util.*;
import java.util.function.Function;

public class CustomBundle {
    private final NamespacedKey dataKey;
    private final String itemTranslationKey;
    private final int capacity;
    private final NamespacedKey model;
    private final Function<ItemStack, Boolean> suitabilityChecker;
    private final Function<ItemStack, Component> loreMaker;

    public CustomBundle(
            NamespacedKey dataKey,
            String itemTranslationKey,
            int capacity,
            NamespacedKey model,
            Function<ItemStack, Boolean> suitabilityChecker,
            Function<ItemStack, Component> loreMaker
    ) {
        this.dataKey = dataKey;
        this.itemTranslationKey = itemTranslationKey;
        this.capacity = capacity;
        this.model = model;
        this.suitabilityChecker = suitabilityChecker;
        this.loreMaker = loreMaker;
    }

    public @NotNull ItemStack getEmptyBundle() {
        final ItemStack item = new ItemStack(Material.PAPER);
        final ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.itemName(Component.translatable(itemTranslationKey));
        meta.setItemModel(model);
        meta.getPersistentDataContainer().set(
                dataKey,
                PersistentDataType.BYTE_ARRAY,
                ItemStack.serializeItemsAsBytes(new ItemStack[0])
        );
        item.setItemMeta(meta);

        return item;
    }

    public boolean isBundle(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(dataKey);
    }

    public boolean isSuitableForBundle(ItemStack item) {
        return suitabilityChecker.apply(item);
    }

    public boolean addItem(ItemStack bundle, ItemStack item) {
        final ItemMeta meta = bundle.getItemMeta();
        ItemStack[] items = ItemStack.deserializeItemsFromBytes(Objects.requireNonNull(
                meta.getPersistentDataContainer().get(dataKey, PersistentDataType.BYTE_ARRAY)
        ));
        if (items.length >= capacity) {
            return false;
        }
        items = Arrays.copyOf(items, items.length + 1);
        items[items.length - 1] = item;
        meta.getPersistentDataContainer().set(dataKey, PersistentDataType.BYTE_ARRAY, ItemStack.serializeItemsAsBytes(items));
        meta.lore(getLore(items));
        bundle.setItemMeta(meta);
        return true;
    }

    public ItemStack removeItem(ItemStack bundle) {
        final ItemMeta meta = bundle.getItemMeta();
        ItemStack[] items = ItemStack.deserializeItemsFromBytes(Objects.requireNonNull(
                meta.getPersistentDataContainer().get(dataKey, PersistentDataType.BYTE_ARRAY)
        ));
        if (items.length == 0) {
            return null;
        }
        final List<ItemStack> itemList = new ArrayList<>(Arrays.stream(items).toList());
        final ItemStack item = itemList.removeFirst();
        items = itemList.toArray(ItemStack[]::new);
        meta.getPersistentDataContainer().set(
                dataKey,
                PersistentDataType.BYTE_ARRAY,
                ItemStack.serializeItemsAsBytes(items)
        );
        meta.lore(getLore(items));
        bundle.setItemMeta(meta);
        return item;
    }

    public void rotateItems(ItemStack bundle) {
        addItem(bundle, removeItem(bundle));
    }

    private List<Component> getLore(ItemStack[] items) {
        final List<Component> components = new ArrayList<>();
        final Map<Component, Integer> counts = new HashMap<>();
        for (ItemStack item : items) {
            final Component component = loreMaker.apply(item);
            if (component == null) continue;
            if (counts.containsKey(component)) {
                counts.put(component, counts.get(component) + 1);
            } else {
                components.add(component);
                counts.put(component, 1);
            }
        }

        final int maxLoreSize = OverpoweredBundles.INSTANCE.getConfig().getInt("max-lore-size");
        final boolean hasMore = components.size() > maxLoreSize;
        final List<Component> result = new ArrayList<>();
        final Iterable<Component> iterableStream = components.stream().limit(hasMore ? maxLoreSize - 1 : maxLoreSize)::iterator;
        for (Component component : iterableStream) {
            if (counts.get(component) == 1) {
                result.add(component);
            } else {
                result.add(component.append(Component.text(" x" + counts.get(component))));
            }
        }
        if (hasMore) {
            result.add(
                    Component.translatable("overpoweredbundles.lore-more-1")
                            .append(Component.text(components.size() - maxLoreSize + 1))
                            .append(Component.translatable("overpoweredbundles.lore-more-2"))
            );
        }
        return result;
    }
}
