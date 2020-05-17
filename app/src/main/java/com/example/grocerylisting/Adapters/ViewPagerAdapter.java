package com.example.grocerylisting.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.grocerylisting.Fragments.FragmentMyProducts;
import com.example.grocerylisting.Fragments.FragmentRecipes;
import com.example.grocerylisting.Fragments.FragmentSelectedRecipes;
import com.example.grocerylisting.Fragments.FragmentShoppingList;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public Context context;
    public FragmentSelectedRecipes selectedRecipes;
    public FragmentMyProducts myProducts;

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentListTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new FragmentRecipes(context);
            case 1:
                selectedRecipes = new FragmentSelectedRecipes(context);
                return selectedRecipes;
            case 2:
                myProducts = new FragmentMyProducts(context);
                return myProducts;
            case 3:
                return new FragmentShoppingList(context);
        }
        return null;
    }

    public FragmentSelectedRecipes getSelectedRecipes()
    {
        return selectedRecipes;
    }

    public FragmentMyProducts getMyProducts()
    {
        return myProducts;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0:
                return "Рецепты";
            case 1:
                return "Выбранные";
            case 2:
                return "Продукты";
            case 3:
                return "Список покупок";
        }
        return null;
    }
}
