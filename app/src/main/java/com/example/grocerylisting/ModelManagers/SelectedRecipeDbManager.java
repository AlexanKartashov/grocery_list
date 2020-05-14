package com.example.grocerylisting.ModelManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.grocerylisting.Models.SelectedRecipe;

import java.util.ArrayList;
import java.util.List;

public class SelectedRecipeDbManager extends SQLiteOpenHelper {

    public static final String SELECTED_RECIPES_TABLE = "SELECTED_RECIPES_TABLE";
    public static final String RECIPE_KEY = "RECIPE_KEY";
    public static final String RECIPE_NAME = "RECIPE_NAME";
    public static final String ID = "ID";

    public SelectedRecipeDbManager(@Nullable Context context) {
        super(context, "selectedrecipes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + SELECTED_RECIPES_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                " " + RECIPE_KEY + " TEXT, " + RECIPE_NAME + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ SELECTED_RECIPES_TABLE);
        onCreate(db);
    }

    public boolean addSelectedRecipe(SelectedRecipe selectedRecipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(RECIPE_KEY, selectedRecipe.getRecipeKey());
        cv.put(RECIPE_NAME, selectedRecipe.getRecipeName());

        long insert = db.insert(SELECTED_RECIPES_TABLE, null, cv);
        db.close();
        if (insert == -1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public List<SelectedRecipe> getListOfSelectRecipes() {
        List<SelectedRecipe> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + SELECTED_RECIPES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String recipeKey = cursor.getString(1);
                String recipeName = cursor.getString(2);
                SelectedRecipe selectedRecipe = new SelectedRecipe(recipeKey,recipeName);
                returnList.add(selectedRecipe);
            } while (cursor.moveToNext());
        }
        else {
            Log.println(Log.DEBUG,"DEBUG", "No data in recipes");
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public boolean unselectRecipe(SelectedRecipe selectedRecipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+SELECTED_RECIPES_TABLE + " WHERE "+ RECIPE_KEY + " = \"" + selectedRecipe.getRecipeKey() +"\"";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return  false;
        }
    }
}
