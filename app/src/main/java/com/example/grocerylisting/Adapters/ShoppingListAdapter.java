package com.example.grocerylisting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylisting.Fragments.FragmentShoppingList;
import com.example.grocerylisting.ModelManagers.ShoppingListDbManager;
import com.example.grocerylisting.Models.Product;
import com.example.grocerylisting.R;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShopListViewHolder> {

    Context context;
    List<Product> data;
    ShoppingListDbManager shoppingListDbManager;
    FragmentShoppingList frag;

    public ShoppingListAdapter(Context context, List<Product> data, FragmentShoppingList frag) {
        this.context = context;
        this.data = data;
        this.shoppingListDbManager = new ShoppingListDbManager(context);
        this.frag = frag;
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ShopListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowRecipe = LayoutInflater.from(context).inflate(R.layout.shopping_list_item, parent,false);
        return new ShoppingListAdapter.ShopListViewHolder(rowRecipe);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ShopListViewHolder holder, final int position) {
        holder.shoplistItemTitle.setText(data.get(position).getProductName());
        holder.shoplistItemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingListDbManager.deleteProduct(data.get(position));
                data.remove(position);
                notifyDataSetChanged();
                if (data.size()==0) {
                    frag.resetGenButton();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ShopListViewHolder extends RecyclerView.ViewHolder {

        TextView shoplistItemTitle;

        public ShopListViewHolder(@NonNull View itemView) {
            super(itemView);
            shoplistItemTitle = itemView.findViewById(R.id.shop_list_item);
        }
    }
}
