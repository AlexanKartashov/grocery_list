package com.example.grocerylisting.Models;

public class RecipeWithIngr {

    String recipeKey;
    String ingrKey;
    String uomKey;

    public RecipeWithIngr(String recipeKey, String ingrKey, String uomKey) {
        this.recipeKey = recipeKey;
        this.ingrKey = ingrKey;
        this.uomKey = uomKey;
    }

    public String getRecipeKey() {
        return recipeKey;
    }

    public void setRecipeKey(String recipeKey) {
        this.recipeKey = recipeKey;
    }

    public String getIngrKey() {
        return ingrKey;
    }

    public void setIngrKey(String ingrKey) {
        this.ingrKey = ingrKey;
    }

    public String getUomKey() {
        return uomKey;
    }

    public void setUomKey(String uomKey) {
        this.uomKey = uomKey;
    }
}
