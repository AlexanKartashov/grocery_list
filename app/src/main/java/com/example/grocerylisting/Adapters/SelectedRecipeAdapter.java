package com.example.grocerylisting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylisting.ModelManagers.SelectedIngredientsDbManager;
import com.example.grocerylisting.ModelManagers.SelectedRecipeDbManager;
import com.example.grocerylisting.Models.SelectedRecipe;
import com.example.grocerylisting.R;

import java.util.List;

public class SelectedRecipeAdapter extends RecyclerView.Adapter<SelectedRecipeAdapter.SelViewHolder> {

    Context context;
    List<SelectedRecipe> data;
    SelectedRecipeDbManager selectedRecipeDbManager;
    SelectedIngredientsDbManager selectedIngredientsDbManager;

    public SelectedRecipeAdapter(Context context, List<SelectedRecipe> data) {
        this.context = context;
        this.data = data;
        this.selectedRecipeDbManager = new SelectedRecipeDbManager(context);
        this.selectedIngredientsDbManager = new SelectedIngredientsDbManager(context);
    }

    @NonNull
    @Override
    public SelectedRecipeAdapter.SelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowRecipe = LayoutInflater.from(context).inflate(R.layout.selected_recipe_list_item, parent,false);
        return new SelectedRecipeAdapter.SelViewHolder(rowRecipe);
    }

    @Override
    public void onBindViewHolder(@NonNull SelViewHolder holder, final int position) {
        holder.recTitle.setText(data.get(position).getRecipeName());
        holder.unselectRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRecipeDbManager.unselectRecipe(data.get(position));
                selectedIngredientsDbManager.deleteAllIngredientsOfRecipe(data.get(position).getRecipeKey());
                data.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Рецепт успешно удален из выбранного", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SelViewHolder extends RecyclerView.ViewHolder{

        TextView recTitle;
        Button unselectRecipeBtn;

        public SelViewHolder(@NonNull View itemView) {
            super(itemView);
            recTitle = itemView.findViewById(R.id.selected_recipe_name);
            unselectRecipeBtn = itemView.findViewById(R.id.delete_selected_recipe_btn);
        }
    }
}
