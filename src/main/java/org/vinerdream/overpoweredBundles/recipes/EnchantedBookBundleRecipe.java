package org.vinerdream.overpoweredBundles.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.vinerdream.overpoweredBundles.OverpoweredBundles;
import org.vinerdream.overpoweredBundles.items.CustomBundle;

public class EnchantedBookBundleRecipe {
    public EnchantedBookBundleRecipe(OverpoweredBundles plugin, CustomBundle bundle) {
        final ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "enchanted_book_bundle_recipe"), bundle.getEmptyBundle());
        recipe.shape(
                " A ",
                "ABA",
                " A "
        );
        recipe.setIngredient('A', Material.BOOK);
        recipe.setIngredient('B', new RecipeChoice.MaterialChoice(Tag.ITEMS_BUNDLES));

        Bukkit.addRecipe(recipe);
    }
}
