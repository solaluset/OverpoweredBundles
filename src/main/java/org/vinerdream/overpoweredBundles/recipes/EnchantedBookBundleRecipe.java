package org.vinerdream.overpoweredBundles.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.vinerdream.overpoweredBundles.OverpoweredBundles;
import org.vinerdream.overpoweredBundles.items.EnchantedBookBundle;

public class EnchantedBookBundleRecipe {
    public EnchantedBookBundleRecipe(OverpoweredBundles plugin, EnchantedBookBundle bundle) {
        final ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "enchanted_book_bundle_recipe"), bundle.getEmptyBundle());
        recipe.shape(
                " A ",
                "ABA",
                " A "
        );
        recipe.setIngredient('A', Material.BOOK);
        recipe.setIngredient('B', Material.BUNDLE);

        Bukkit.addRecipe(recipe);
    }
}
