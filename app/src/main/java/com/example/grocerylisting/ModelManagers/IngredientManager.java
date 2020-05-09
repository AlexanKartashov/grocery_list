package com.example.grocerylisting.ModelManagers;

import androidx.annotation.NonNull;

import com.example.grocerylisting.Models.IngrAndUom;
import com.example.grocerylisting.Models.Ingredient;
import com.example.grocerylisting.Models.Recipe;
import com.example.grocerylisting.Models.RecipeWithIngr;
import com.example.grocerylisting.Models.Uom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class IngredientManager {

    boolean check = true;

    public boolean addIngredients(ArrayList<IngrAndUom> ingrAndUomList,DatabaseReference refRecipe)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        for (IngrAndUom ingrAndUom:
             ingrAndUomList) {
            DatabaseReference refIngr = db.getReference("Ingredient").push();
            DatabaseReference refUom = db.getReference("Uom").push();
            DatabaseReference refIngrUom = db.getReference("IngredientsUom").push();

            String keyIngr = refIngr.getKey();
            Ingredient newIngredient = new Ingredient(ingrAndUom.getIngredient());
            newIngredient.setIngrKey(keyIngr);

            String keyUom = refUom.getKey();
            Uom newUom = new Uom(ingrAndUom.getUom());
            newUom.setUomKey(keyUom);

            refUom.setValue(newUom).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    check = false;
                }
            });
            refIngr.setValue(newIngredient).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    check = false;
                }
            });


            String keyIngrUom = refIngrUom.getKey();
            RecipeWithIngr newIngrUom = new RecipeWithIngr(refRecipe.getKey(),
                    keyIngr, keyUom);
            refIngrUom.setValue(newIngrUom).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    check = false;
                }
            });


            if (!check)
                return false;
        }

        return check;
    }
}
