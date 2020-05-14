package com.example.grocerylisting.Models;

import android.widget.TextView;

public class IngrAndUom {

    String ingredient;
    String uom;

    public IngrAndUom() {
    }

    public IngrAndUom(String ingredient, String uom) {
        this.ingredient = ingredient;
        this.uom = uom;
    }

    public IngrAndUom(TextView ingredient, TextView uom) {
        this.ingredient = ingredient.getText().toString();
        this.uom = uom.getText().toString();
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
}
