package com.example.grocerylisting.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylisting.Adapters.SelectedRecipeAdapter;
import com.example.grocerylisting.ModelManagers.SelectedRecipeDbManager;
import com.example.grocerylisting.Models.SelectedRecipe;
import com.example.grocerylisting.R;

import java.util.List;

public class FragmentSelectedRecipes extends Fragment {

    View view;
    RecyclerView recyclerView;
    SelectedRecipeAdapter selectedRecipeAdapter;
    SelectedRecipeDbManager selectedRecipeDbManager;

    List<SelectedRecipe> selectedRecipeList;

    public FragmentSelectedRecipes() {

    }

    @Override
    public void onResume() {
        super.onResume();
        selectedRecipeAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(selectedRecipeAdapter);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager lin = new LinearLayoutManager(getActivity());
        lin.setStackFromEnd(true);
        lin.setReverseLayout(true);
        view = inflater.inflate(R.layout.selectedrecipes_fragment,container,false);
        recyclerView = view.findViewById(R.id.selected_recipes_list);
        recyclerView.setLayoutManager(lin);
        recyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedRecipeDbManager  = new SelectedRecipeDbManager(getActivity());
        selectedRecipeList = selectedRecipeDbManager.getListOfSelectRecipes();

        selectedRecipeAdapter = new SelectedRecipeAdapter(getActivity(), selectedRecipeList);
        recyclerView.setAdapter(selectedRecipeAdapter);
    }

}
