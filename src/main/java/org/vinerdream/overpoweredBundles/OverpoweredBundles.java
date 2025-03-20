package org.vinerdream.overpoweredBundles;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.vinerdream.overpoweredBundles.items.CustomBundle;
import org.vinerdream.overpoweredBundles.items.EnchantedBookBundle;
import org.vinerdream.overpoweredBundles.listeners.InventoryListener;
import org.vinerdream.overpoweredBundles.recipes.EnchantedBookBundleRecipe;

import java.util.ArrayList;
import java.util.List;

public final class OverpoweredBundles extends JavaPlugin {
    public static OverpoweredBundles INSTANCE;
    private final List<CustomBundle> customBundles = new ArrayList<>();

    public OverpoweredBundles() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        final EnchantedBookBundle enchantedBookBundle = new EnchantedBookBundle();
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
