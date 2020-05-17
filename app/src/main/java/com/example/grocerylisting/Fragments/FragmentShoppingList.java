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

import com.example.grocerylisting.Adapters.ShoppingListAdapter;
import com.example.grocerylisting.ModelManagers.MyProductsDbManager;
import com.example.grocerylisting.ModelManagers.SelectedIngredientsDbManager;
import com.example.grocerylisting.ModelManagers.ShoppingListDbManager;
import com.example.grocerylisting.Models.IngrOfRecipe;
import com.example.grocerylisting.Models.Product;
import com.example.grocerylisting.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentShoppingList extends Fragment {

    View view;
    public Context context;
    RecyclerView recyclerView;
    ImageView manageShoppingListBtn;
    ShoppingListDbManager shoppingListDbManager;
    SelectedIngredientsDbManager ingredientsDbManager;
    MyProductsDbManager productsDbManager;
    ShoppingListAdapter shoppingListAdapter;

    List<Product> productList;

    boolean shopListGenerated;

    public FragmentShoppingList(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager lin = new LinearLayoutManager(getActivity());
        lin.setStackFromEnd(true);
        lin.setReverseLayout(true);
        view = inflater.inflate(R.layout.shoplist_fragment,container,false);
        recyclerView = view.findViewById(R.id.shop_list);
        recyclerView.setLayoutManager(lin);
        recyclerView.setHasFixedSize(true);
        shoppingListDbManager  = new ShoppingListDbManager(context);
        productList = shoppingListDbManager.getListOfProducts();
        shoppingListAdapter = new ShoppingListAdapter(getActivity(), productList, this);
        recyclerView.setAdapter(shoppingListAdapter);

        manageShoppingListBtn = view.findViewById(R.id.gen_shop_list_btn);
        if (productList.size()>0) {
            shopListGenerated = true;
            manageShoppingListBtn.setImageResource(R.drawable.clear_list);
        }
        else {
            shopListGenerated = false;
            manageShoppingListBtn.setImageResource(R.drawable.generate_list);
        }


        manageShoppingListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!shopListGenerated) {
                    ingredientsDbManager = new SelectedIngredientsDbManager(context);
                    List<IngrOfRecipe> selectedIngredients = ingredientsDbManager.getAllSelectedIngredients();
                    List<Product> ingredients = new ArrayList<>();
                    for (IngrOfRecipe ingr : selectedIngredients) {
                        ingredients.add(new Product(ingr.getIngrName()));
                    }
                    productsDbManager = new MyProductsDbManager(context);
                    List<Product> myProducts = productsDbManager.getListOfProducts();

                    productList.clear();
                    for (Product pr : ingredients) {
                        if (!(myProducts.contains(pr))) {
                            productList.add(pr);
                            shoppingListDbManager.addProduct(pr);
                        }
                    }
                    shoppingListAdapter.notifyDataSetChanged();
                    manageShoppingListBtn.setImageResource(R.drawable.clear_list);
                    shopListGenerated = true;
                }
                else {
                    productList.clear();
                    shoppingListAdapter.notifyDataSetChanged();
                    manageShoppingListBtn.setImageResource(R.drawable.generate_list);
                    shopListGenerated = false;
                }
            }
        });

        return view;
    }

    public void resetGenButton() {
        manageShoppingListBtn.setImageResource(R.drawable.generate_list);
    }
}
