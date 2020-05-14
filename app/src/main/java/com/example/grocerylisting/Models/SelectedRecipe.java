package com.example.grocerylisting.Models;

public class SelectedRecipe {

    private String recipeKey;
    private String recipeName;

    public SelectedRecipe() {
    }

    public SelectedRecipe(String recipeKey, String recipeName) {
        this.recipeKey = recipeKey;
        this.recipeName = recipeName;
    }

    public String getRecipeKey() {
        return recipeKey;
    }

    public void setRecipeKey(String recipeKey) {
        this.recipeKey = recipeKey;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
}
