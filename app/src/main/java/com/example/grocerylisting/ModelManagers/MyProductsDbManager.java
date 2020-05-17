package com.example.grocerylisting.ModelManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.grocerylisting.Models.Product;

import java.util.ArrayList;
import java.util.List;

public class MyProductsDbManager extends SQLiteOpenHelper {

    public Context context;

    public static final String MY_PRODUCTS_TABLE = "MY_PRODUCTS_TABLE";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String ID = "ID";

    public MyProductsDbManager(@Nullable Context context) {
        super(context, "myproducts.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + MY_PRODUCTS_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                " " + PRODUCT_NAME + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ MY_PRODUCTS_TABLE);
        onCreate(db);
    }

    public boolean addProduct(Product newProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRODUCT_NAME, newProduct.getProductName());

        long insert = db.insert(MY_PRODUCTS_TABLE, null, cv);
        db.close();
        if (insert == -1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+MY_PRODUCTS_TABLE + " WHERE "+ PRODUCT_NAME + " = \"" + product.getProductName() +"\"";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return  false;
        }
    }

    public List<Product> getListOfProducts() {
        List<Product> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + MY_PRODUCTS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String productName = cursor.getString(1);
                Product selectedRecipe = new Product(productName);
                returnList.add(selectedRecipe);
            } while (cursor.moveToNext());
        }
        else {
            Log.println(Log.DEBUG,"DEBUG", "No data in products");
        }

        cursor.close();
        return returnList;
    }
}
