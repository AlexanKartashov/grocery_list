package com.example.grocerylisting.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.grocerylisting.Activities.RecipeDetailActivity;
import com.example.grocerylisting.Models.Ingredient;
import com.example.grocerylisting.Models.Uom;
import com.example.grocerylisting.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailIngrListAdapter extends BaseAdapter {

    Activity context;
    public ArrayList<Ingredient> ingredients;
    public ArrayList<Uom> uoms;

    private static LayoutInflater inflater = null;

    public RecipeDetailIngrListAdapter(Activity context, ArrayList<Ingredient> mIngredients,
                                       ArrayList<Uom> mUoms) {
        this.context = context;
        this.ingredients = mIngredients;
        this.uoms = mUoms;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //return ingredients.size();
        return (ingredients.size()<=uoms.size())? ingredients.size() : uoms.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.recdet_ingr_list_item,parent,false) : itemView;
        TextView ingrName = (TextView) itemView.findViewById(R.id.recdet_ingr);
        TextView uomName = (TextView) itemView.findViewById(R.id.recdet_uom);

        ingrName.setText(ingredients.get(position).getIngrName());
        uomName.setText(uoms.get(position).getUomName());
        return itemView;
    }
}
