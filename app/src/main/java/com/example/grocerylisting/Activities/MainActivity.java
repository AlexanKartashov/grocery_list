package com.example.grocerylisting.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.example.grocerylisting.Fragments.FragmentMyProducts;
import com.example.grocerylisting.Fragments.FragmentRecipes;
import com.example.grocerylisting.Fragments.FragmentSelectedRecipes;
import com.example.grocerylisting.Fragments.FragmentShoppingList;
import com.example.grocerylisting.R;
import com.example.grocerylisting.Adapters.ViewPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentRecipes(),"Рецепты");
        adapter.AddFragment(new FragmentSelectedRecipes(),"Выбранные");
        adapter.AddFragment(new FragmentMyProducts(),"Продукты");
        adapter.AddFragment(new FragmentShoppingList(),"Список покупок");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


}
