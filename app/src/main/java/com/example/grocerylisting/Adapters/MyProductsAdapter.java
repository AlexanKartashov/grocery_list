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

import com.example.grocerylisting.ModelManagers.MyProductsDbManager;
import com.example.grocerylisting.Models.Product;
import com.example.grocerylisting.R;

import java.util.List;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ProductViewHolder> {

    Context context;
    List<Product> data;
    MyProductsDbManager myProductsDbManager;

    public MyProductsAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
        this.myProductsDbManager = new MyProductsDbManager(context);
    }

    @NonNull
    @Override
    public MyProductsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowRecipe = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent,false);
        return new MyProductsAdapter.ProductViewHolder(rowRecipe);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        holder.productTitle.setText(data.get(position).getProductName());
        holder.delProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProductsDbManager.deleteProduct(data.get(position));
                data.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Продукт успешно удален", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productTitle;
        Button delProductBtn;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.my_product_name);
            delProductBtn = itemView.findViewById(R.id.delete_my_product_btn);
        }
    }
}
