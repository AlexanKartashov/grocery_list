package com.example.grocerylisting.ModelManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.grocerylisting.Models.IngrOfRecipe;

import java.util.ArrayList;
import java.util.List;

public class SelectedIngredientsDbManager extends SQLiteOpenHelper {

    public Context context;

    public static final String SELECTED_INGRS_TABLE = "SELECTED_INGRS_TABLE";
    public static final String RECIPE_KEY = "RECIPE_KEY";
    public static final String INGR_KEY = "INGR_KEY";
    public static final String INGR_NAME = "INGR_NAME";
    public static final String ID = "ID";

    public SelectedIngredientsDbManager(@Nullable Context context) {
        super(context, "selectedingr.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + SELECTED_INGRS_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                " " + RECIPE_KEY + " TEXT, " + " " + INGR_KEY + " TEXT, " + INGR_NAME + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ SELECTED_INGRS_TABLE);
        onCreate(db);
    }

    public boolean addIngredient(IngrOfRecipe ingr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(RECIPE_KEY, ingr.getRecipeKey());
        cv.put(INGR_KEY, ingr.getIngrKey());
        cv.put(INGR_NAME, ingr.getIngrName());

        long insert = db.insert(SELECTED_INGRS_TABLE, null, cv);
        db.close();
        if (insert == -1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean deleteAllIngredientsOfRecipe(String recipeKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+SELECTED_INGRS_TABLE + " WHERE "+ RECIPE_KEY + " = \"" + recipeKey + "\"";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return  false;
        }
    }

    public List<IngrOfRecipe> getAllSelectedIngredients() {
        List<IngrOfRecipe> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + SELECTED_INGRS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String recipeKey = cursor.getString(1);
                String ingrKey = cursor.getString(2);
                String ingrName = cursor.getString(3);
                IngrOfRecipe selectedIngr = new IngrOfRecipe(recipeKey,
                        ingrKey, ingrName);
                returnList.add(selectedIngr);
            } while (cursor.moveToNext());
        }
        else {
            Log.println(Log.DEBUG,"DEBUG", "No data in selected ingredients");
        }

        cursor.close();
        return returnList;
    }
}
