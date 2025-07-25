package org.vinerdream.overpoweredBundles;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.vinerdream.overpoweredBundles.items.CustomBundle;
import org.vinerdream.overpoweredBundles.listeners.CraftListener;
import org.vinerdream.overpoweredBundles.listeners.InventoryListener;
import org.vinerdream.overpoweredBundles.recipes.EnchantedBookBundleRecipe;
import org.vinerdream.overpoweredBundles.recipes.PotionBundleRecipe;

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

        if (getConfig().getBoolean("bundles.enchanted-book.enabled")) {
            final CustomBundle enchantedBookBundle = new CustomBundle(
                    new NamespacedKey(this, "book-bundle-data"),
                    "item.overpoweredbundles.enchanted-book-bundle",
                    getConfig().getInt("bundles.enchanted-book.capacity"),
                    new NamespacedKey(this, "enchanted-book-bundle"),
                    item -> item != null && item.getType().equals(Material.ENCHANTED_BOOK),
                    item -> {
                        if (!(item.getItemMeta() instanceof EnchantmentStorageMeta meta)) return null;
                        if (meta.customName() != null) return meta.customName();
                        Component component = Component.empty();
                        final List<Map.Entry<Enchantment, Integer>> entries = new ArrayList<>(meta.getStoredEnchants().entrySet().stream().toList());
                        final Map.Entry<Enchantment, Integer> lastEntry = entries.removeLast();
                        for (Map.Entry<Enchantment, Integer> entry : entries) {
                            component = component.append(Component.translatable(entry.getKey())).append(Component.text(" " + entry.getValue() + ", "));
                        }
                        return component.append(Component.translatable(lastEntry.getKey())).append(Component.text(" " + lastEntry.getValue())).decoration(TextDecoration.ITALIC, false);
                    }
            );
            new EnchantedBookBundleRecipe(this, enchantedBookBundle);
            customBundles.add(enchantedBookBundle);
        }

        if (getConfig().getBoolean("bundles.potion.enabled")) {
            final CustomBundle potionBundle = new CustomBundle(
                    new NamespacedKey(this, "potion-bundle-data"),
                    "item.overpoweredbundles.potion-bundle",
                    getConfig().getInt("bundles.potion.capacity"),
                    new NamespacedKey(this, "potion-bundle"),
                    item -> item != null && item.getItemMeta() instanceof PotionMeta,
                    item -> {
                        if (!(item.getItemMeta() instanceof PotionMeta meta)) return null;
                        if (meta.customName() != null) return meta.customName();
                        if (meta.getBasePotionType() == null) return Component.translatable(
                                "overpoweredbundles.unknown-potion"
                        ).decoration(TextDecoration.ITALIC, false);
                        final List<PotionEffect> effects = new ArrayList<>(meta.getBasePotionType().getPotionEffects());
                        final PotionEffect lastEffect = effects.removeLast();
                        Component component = Component.empty();
                        for (PotionEffect effect : effects) {
                            component = component.append(Component.translatable(effect.getType())).append(Component.text(" " + (effect.getAmplifier() + 1) + ", "));
                        }
                        return component.append(Component.translatable(lastEffect.getType())).append(Component.text(" " + (lastEffect.getAmplifier() + 1))).decoration(TextDecoration.ITALIC, false);
                    }
            );
            new PotionBundleRecipe(this, potionBundle);
            customBundles.add(potionBundle);
        }

        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CraftListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public List<CustomBundle> getCustomBundles() {
        return customBundles;
    }
}
