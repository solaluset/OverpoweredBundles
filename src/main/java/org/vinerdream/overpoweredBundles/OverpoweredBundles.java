package org.vinerdream.overpoweredBundles;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.vinerdream.overpoweredBundles.items.CustomBundle;
import org.vinerdream.overpoweredBundles.listeners.InventoryListener;
import org.vinerdream.overpoweredBundles.recipes.EnchantedBookBundleRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class OverpoweredBundles extends JavaPlugin {
    public static OverpoweredBundles INSTANCE;
    private final List<CustomBundle> customBundles = new ArrayList<>();

    public OverpoweredBundles() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        final CustomBundle enchantedBookBundle = new CustomBundle(
                new NamespacedKey(this, "book-bundle-data"),
                getConfig().getRichMessage("messages.enchanted-book-bundle"),
                getConfig().getInt("enchanted-book-bundle-capacity"),
                item -> item != null && item.getType().equals(Material.ENCHANTED_BOOK),
                item -> {
                    if (!(item.getItemMeta() instanceof EnchantmentStorageMeta meta)) return null;
                    Component component = Component.empty();
                    final List<Map.Entry<Enchantment, Integer>> entries = new ArrayList<>(meta.getStoredEnchants().entrySet().stream().toList());
                    final Map.Entry<Enchantment, Integer> lastEntry = entries.removeLast();
                    for (Map.Entry<Enchantment, Integer> entry : entries) {
                        component = component.append(Component.translatable(entry.getKey())).append(Component.text(" " + entry.getValue() + ", ")).decoration(TextDecoration.ITALIC, false);
                    }
                    return component.append(Component.translatable(lastEntry.getKey())).append(Component.text(" " + lastEntry.getValue())).decoration(TextDecoration.ITALIC, false);
                }
        );
        new EnchantedBookBundleRecipe(this, enchantedBookBundle);
        customBundles.add(enchantedBookBundle);

        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public List<CustomBundle> getCustomBundles() {
        return customBundles;
    }
}
