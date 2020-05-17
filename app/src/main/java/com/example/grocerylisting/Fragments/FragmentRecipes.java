package com.example.grocerylisting.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylisting.Adapters.RecipeInListAdapter;
import com.example.grocerylisting.ModelManagers.IngredientManager;
import com.example.grocerylisting.ModelManagers.RecipeManager;
import com.example.grocerylisting.Models.Recipe;
import com.example.grocerylisting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentRecipes extends Fragment {

    public Context context;
    View view;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Recipe> recipeList;

    RecipeManager recipeManager;
    IngredientManager ingredientManager;
    boolean ingrCheck = false;
    boolean recCheck = false;


    RecyclerView recyclerView;
    RecipeInListAdapter recipeAdapter;

    public FragmentRecipes(Context context) {
        this.recipeManager = new RecipeManager();
        this.ingredientManager = new IngredientManager();
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager lin = new LinearLayoutManager(getActivity());
        lin.setStackFromEnd(true);
        lin.setReverseLayout(true);
        view = inflater.inflate(R.layout.recipes_fragment,container,false);
        recyclerView =  view.findViewById(R.id.recipeList);
        recyclerView.setLayoutManager(lin);
        recyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Recipes");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList = new ArrayList<>();
                for (DataSnapshot recipesnap: dataSnapshot.getChildren()) {
                    Recipe recipe = recipesnap.getValue(Recipe.class);
                    recipeList.add(recipe);
                }

                recipeAdapter = new RecipeInListAdapter(getActivity(), recipeList);
                recyclerView.setAdapter(recipeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
