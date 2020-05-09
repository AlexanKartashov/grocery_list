package com.example.grocerylisting.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.grocerylisting.Models.IngrAndUom;
import com.example.grocerylisting.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EditIngrListAdapter extends BaseAdapter {
    Activity context;
    ArrayList<IngrAndUom> ingredients;
    private static LayoutInflater inflater = null;

    public EditIngrListAdapter(Activity context) {
        this.context = context;
        this.ingredients = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public EditIngrListAdapter(Activity context, ArrayList<IngrAndUom> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ingredients.size();
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
        itemView = (itemView == null) ? inflater.inflate(R.layout.edit_ingr_list_item,parent,false) : itemView;
        TextView ingrName = (TextView) itemView.findViewById(R.id.edit_list_ingrd);
        TextView uomName = (TextView) itemView.findViewById(R.id.edit_list_uom);
        Button dltIngr = (Button) itemView.findViewById(R.id.list_item_btn);
        dltIngr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients.remove(pos);
                notifyDataSetChanged();
            }
        });
        IngrAndUom ingredient = ingredients.get(position);
        ingrName.setText(ingredient.getIngredient());
        uomName.setText(ingredient.getUom());
        return itemView;
    }

    public ArrayList<IngrAndUom> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<IngrAndUom> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(TextView ingr, TextView uom) {
        this.ingredients.add(new IngrAndUom(ingr, uom));
        notifyDataSetChanged();
    }
}