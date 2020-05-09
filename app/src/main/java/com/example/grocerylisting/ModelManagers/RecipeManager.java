package com.example.grocerylisting.ModelManagers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.grocerylisting.Activities.MainActivity;
import com.example.grocerylisting.Models.Recipe;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecipeManager {

    public RecipeManager() {
    }

    public DatabaseReference getRecipeKey()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Recipes").push();
        return ref;
    }

    public Task<Void> addRecipeToFirebase(Recipe newRecipe, DatabaseReference ref)
    {
        String key = ref.getKey();
        newRecipe.setKey(key);

        return ref.setValue(newRecipe);
    }

}
