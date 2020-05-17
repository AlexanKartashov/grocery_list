package com.example.grocerylisting.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylisting.Adapters.MyProductsAdapter;
import com.example.grocerylisting.ModelManagers.MyProductsDbManager;
import com.example.grocerylisting.Models.Product;
import com.example.grocerylisting.R;

import java.util.List;

public class FragmentMyProducts extends Fragment {

    View view;
    public Context context;
    RecyclerView recyclerView;
    MyProductsAdapter myProductsAdapter;
    MyProductsDbManager myProductsDbManager;

    List<Product> productList;


    public FragmentMyProducts(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager lin = new LinearLayoutManager(getActivity());
        lin.setStackFromEnd(true);
        lin.setReverseLayout(true);
        view = inflater.inflate(R.layout.myproducts_fragment,container,false);
        recyclerView = view.findViewById(R.id.my_products_list);
        recyclerView.setLayoutManager(lin);
        recyclerView.setHasFixedSize(true);
        myProductsDbManager  = new MyProductsDbManager(context);
        productList = myProductsDbManager.getListOfProducts();

        myProductsAdapter = new MyProductsAdapter(getActivity(), productList);
        recyclerView.setAdapter(myProductsAdapter);
        return view;
    }

    public void updateListOfProducts() {

        productList.clear();
        myProductsDbManager  = new MyProductsDbManager(context);
        List<Product> newSelectedRecipes = myProductsDbManager.getListOfProducts();
        productList.addAll(newSelectedRecipes);
        myProductsAdapter.notifyDataSetChanged();
    }

}
