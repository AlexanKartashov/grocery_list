package com.example.grocerylisting.Models;

public class Ingredient {

    String ingrName;
    String ingrKey;

    public Ingredient(String ingrName) {
        this.ingrName = ingrName;
    }

    public String getIngrName() {
        return ingrName;
    }

    public void setIngrName(String ingrName) {
        this.ingrName = ingrName;
    }

    public String getIngrKey() {
        return ingrKey;
    }

    public void setIngrKey(String ingrKey) {
        this.ingrKey = ingrKey;
    }
}
