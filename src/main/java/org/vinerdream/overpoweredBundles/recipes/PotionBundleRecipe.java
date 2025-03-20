package org.vinerdream.overpoweredBundles.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.vinerdream.overpoweredBundles.OverpoweredBundles;
import org.vinerdream.overpoweredBundles.items.CustomBundle;

public class PotionBundleRecipe {
    public PotionBundleRecipe(OverpoweredBundles plugin, CustomBundle bundle) {
        final ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "potion_bundle_recipe"), bundle.getEmptyBundle());
        recipe.shape(
                " A ",
                "ABA",
                " A "
        );
        recipe.setIngredient('A', Material.GLASS_BOTTLE);
        recipe.setIngredient('B', Material.BUNDLE);

        Bukkit.addRecipe(recipe);
    }
}
