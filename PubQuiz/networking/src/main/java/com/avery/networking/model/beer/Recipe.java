
package com.avery.networking.model.beer;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private List<RecipeItem> recipeItems = new ArrayList<RecipeItem>();

    /**
     * @return The recipeItems
     */
    public List<RecipeItem> getRecipeItems() {
        return recipeItems;
    }

    /**
     * @param recipeItems The recipe_items
     */
    public void setRecipeItems(List<RecipeItem> recipeItems) {
        this.recipeItems = recipeItems;
    }

}
