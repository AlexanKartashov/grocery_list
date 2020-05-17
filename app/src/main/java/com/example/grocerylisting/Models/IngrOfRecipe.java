package com.example.grocerylisting.Models;

public class IngrOfRecipe {

    String recipeKey;
    String ingrKey;
    String ingrName;

    public IngrOfRecipe(String recipeKey, String ingrKey, String ingrName) {
        this.recipeKey = recipeKey;
        this.ingrKey = ingrKey;
        this.ingrName = ingrName;
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

    public String getIngrName() {
        return ingrName;
    }

    public void setIngrName(String ingrName) {
        this.ingrName = ingrName;
    }
}
