package com.example.recetario.Utilities;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.recetario.models.Recipe;

import java.util.ArrayList;

public class RecetarioDatabase extends SQLiteOpenHelper{
    private static class DatabaseHelper implements BaseColumns {
        public static final String RECIPE_TABLE_NAME = "recipes";
        public static final String COLUMN_NAME_RECIPE_NAME = "recipeName";
        public static final String COLUMN_NAME_RECIPE_DESCRIPTION = "recipeDescription";
        public static final String COLUMN_NAME_RECIPE_INGREDIENTS = "recipeIngredients";
        public static final String COLUMN_NAME_RECIPE_INSTRUCTIONS = "recipeInstructions";
    }

    // Database name
    private static final String DATABASE_NAME = "recetario.db";

    // Database version
    private static final int DATABASE_VERSION = 2;

    // Constructor
    public RecetarioDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        // Create a table to hold recipes
        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + DatabaseHelper.RECIPE_TABLE_NAME + " (" +
                DatabaseHelper.COLUMN_NAME_RECIPE_NAME + " TEXT PRIMARY KEY, " +
                DatabaseHelper.COLUMN_NAME_RECIPE_DESCRIPTION + " TEXT NOT NULL, " +
                DatabaseHelper.COLUMN_NAME_RECIPE_INGREDIENTS + " TEXT NOT NULL, " +
                DatabaseHelper.COLUMN_NAME_RECIPE_INSTRUCTIONS + " TEXT NOT NULL ) " ;

        // Execute the SQL statements
        db.execSQL(SQL_CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.RECIPE_TABLE_NAME);
        onCreate(db);
    }

    public boolean addRecipe(String recipeName, String recipeDescription, String recipeIngredients, String recipeInstructions) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues contentValues = new android.content.ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME_RECIPE_NAME, recipeName);
        contentValues.put(DatabaseHelper.COLUMN_NAME_RECIPE_DESCRIPTION, recipeDescription);
        contentValues.put(DatabaseHelper.COLUMN_NAME_RECIPE_INGREDIENTS, recipeIngredients);
        contentValues.put(DatabaseHelper.COLUMN_NAME_RECIPE_INSTRUCTIONS, recipeInstructions);
        long result = db.insert(DatabaseHelper.RECIPE_TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        android.database.Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.RECIPE_TABLE_NAME, null);
        while (cursor.moveToNext()) {
            String recipeName = cursor.getString(0);
            String recipeDescription = cursor.getString(1);
            String recipeIngredients = cursor.getString(2);
            String recipeInstructions = cursor.getString(3);
            Recipe recipe = new Recipe(recipeName, recipeDescription, recipeIngredients, recipeInstructions);
            recipes.add(recipe);
        }
        cursor.close();
        return recipes;
    }
}
