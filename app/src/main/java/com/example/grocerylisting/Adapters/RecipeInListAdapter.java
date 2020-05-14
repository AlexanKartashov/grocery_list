package com.example.grocerylisting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grocerylisting.Activities.RecipeDetailActivity;
import com.example.grocerylisting.Models.Recipe;
import com.example.grocerylisting.R;

import java.util.List;

public class RecipeInListAdapter  extends RecyclerView.Adapter<RecipeInListAdapter.MyViewHolder> {

    Context context;
    List<Recipe> data;

    public RecipeInListAdapter(Context context, List<Recipe> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowRecipe = LayoutInflater.from(context).inflate(R.layout.row_recipe_item, parent,false);
        return new MyViewHolder(rowRecipe);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recTitle.setText(data.get(position).getTile());
        holder.recAuthor.setText("Автор: "+data.get(position).getAuthor());
        Glide.with(context).load(data.get(position).getPicture()).into(holder.recImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView recTitle;
        TextView recAuthor;
        ImageView recImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recTitle = itemView.findViewById(R.id.recipeTitleInList);
            recAuthor = itemView.findViewById(R.id.authorInList);
            recImage = itemView.findViewById(R.id.recipeImageInList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent recipeDetailActivity = new Intent(context, RecipeDetailActivity.class);
                    int position = getAdapterPosition();

                    recipeDetailActivity.putExtra("title",data.get(position).getTile());
                    recipeDetailActivity.putExtra("author",data.get(position).getAuthor());
                    recipeDetailActivity.putExtra("instruction",data.get(position).getInstruction());
                    recipeDetailActivity.putExtra("recipeImage",data.get(position).getPicture());
                    recipeDetailActivity.putExtra("recipeKey",data.get(position).getKey());
                    context.startActivity(recipeDetailActivity);
                }
            });
        }
    }
}
