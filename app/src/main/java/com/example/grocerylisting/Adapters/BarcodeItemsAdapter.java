package com.example.grocerylisting.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.grocerylisting.Models.CheckboxName;
import com.example.grocerylisting.R;

import java.util.ArrayList;
import java.util.List;

public class BarcodeItemsAdapter extends BaseAdapter {

    Activity context;
    List<CheckboxName> names;
    public String finalItemName;

    private static LayoutInflater inflater = null;

    public BarcodeItemsAdapter(Activity context) {
        this.context = context;
        this.names = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BarcodeItemsAdapter(Activity context, List<CheckboxName> names) {
        this.context = context;
        this.names = names;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void clear() {
        this.names.clear();
        notifyDataSetChanged();
    }

    public void addName(String name, Boolean checked) {
        this.names.add(new CheckboxName(name, checked));
        notifyDataSetChanged();
    }

    public void notifyAdapt(List<CheckboxName> itemNameBr) {
        names.clear();
        names.addAll(itemNameBr);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getFinalItemName() {
        finalItemName = "";
        for (CheckboxName name: names) {
            if (name.getChecked()) {
                finalItemName = finalItemName+ name.getName() + " ";
            }
        }
        finalItemName = finalItemName.trim();
        return toCamelCase(finalItemName);
    }

    private String toCamelCase(String text) {
        StringBuilder sb = new StringBuilder();
        text = text.toLowerCase();
        sb.append( text.substring(0,1).toUpperCase() );
        sb.append( text.substring(1) );
        return sb.toString();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.item_barcode_select,parent,false) : itemView;
        TextView itemName = (TextView) itemView.findViewById(R.id.text_barcode_item);
        itemName.setText(names.get(pos).getName());
        CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.checkBox_barcode_item);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names.get(pos).setChecked(!(names.get(pos).getChecked()));
            }
        });

        return itemView;
    }

}
