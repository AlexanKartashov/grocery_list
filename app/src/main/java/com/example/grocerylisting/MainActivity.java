package com.example.grocerylisting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tableLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = (TabLayout) findViewById(R.id.tablayout_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarid);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentRecipes(),"Рецепты");
        adapter.AddFragment(new FragmentSelectedRecipes(),"Выбранные рецепты");
        adapter.AddFragment(new FragmentMyProducts(),"Мои продукты");
        adapter.AddFragment(new FragmentShoppingList(),"Список покупок");
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

    }
}
