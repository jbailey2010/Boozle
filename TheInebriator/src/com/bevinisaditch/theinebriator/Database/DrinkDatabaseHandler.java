package com.bevinisaditch.theinebriator.Database;

import java.util.ArrayList;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Drink.Rating;
import com.bevinisaditch.theinebriator.ClassFiles.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DrinkDatabaseHandler extends SQLiteOpenHelper 
{
		 
	    // All Static variables
	    // Database Version
	    private static final int DATABASE_VERSION = 1;
	 
	    // Database Name
	    private static final String DATABASE_NAME = "DrinksAndIngredients";
	 	 
	    public DrinkDatabaseHandler(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
	 
	    // Creating Tables
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        String CREATE_DRINKS_TABLE = "CREATE TABLE DRINKS" +
	        		"(ID INT PRIMARY KEY       NOT NULL," +
					"NAME           TEXT       NOT NULL," +
					"RATING         INT        NOT NULL," +
					"INSTRUCTIONS   TEXT       NOT NULL)" ;
	        db.execSQL(CREATE_DRINKS_TABLE);
	        String CREATE_PAIRS_TABLE = "CREATE TABLE INGREDIENTS" +
					"(ID INT PRIMARY KEY      NOT NULL," +
					"NAME           TEXT      NOT NULL)";
	        db.execSQL(CREATE_PAIRS_TABLE);
	        String CREATE_MATCHINGS_TABLE = "CREATE TABLE MATCHINGS" +
					"(ID INT PRIMARY KEY       NOT NULL," +
					"DRINKID        INT        NOT NULL," +
					"INGREDIENTID   INT        NOT NULL," +
					"QUANTITY       TEXT," +
					"UNITS          TEXT)";
	        db.execSQL(CREATE_MATCHINGS_TABLE);
	    }
	 
	    // Upgrading database
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // Drop older table if existed
	        db.execSQL("DROP TABLE IF EXISTS DRINKS");
	        db.execSQL("DROP TABLE IF EXISTS INGREDIENTS");
	        db.execSQL("DROP TABLE IF EXISTS MATCHINGS");
	 
	        // Create tables again
	        onCreate(db);
	    }
	    
	    public void addDrinkWithoutIngredients(Drink drink)
	    {
	    	if (!drink.getIngredients().isEmpty())
	    	{
	    		return;
	    	}
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	 
	        ContentValues values = new ContentValues();
	        values.put("ID", drink.getId()); 
	        values.put("NAME", drink.getName());
	        values.put("RATING", ratingToInt(drink.getRating()));
	        values.put("INSTRUCTIONS", drink.getInstructions());
	     
	        // Inserting Row
	        db.insert("DRINKS", null, values);
	        db.close(); // Closing database connection
	    }
	    
	    public void addPair(IngredientIDPair pair)
	    {
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	ContentValues values = new ContentValues();
	    	values.put("ID", pair.id);
	    	values.put("NAME", pair.name);
	    	
	    	db.insert("INGREDIENTS", null, values);
	    }
	    
	    public void addMatching(Matching match)
	    {
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	ContentValues values = new ContentValues();
	    	values.put("ID", match.matchID);
	    	values.put("DRINKID", match.drinkID);
	    	values.put("INGREDIENTID", match.ingredientID);
	    	values.put("QUANTITY", match.quantity);
	    	values.put("UNITS", match.units);
	    	db.insert("MATCHINGS", null, values);
	    }

	    private int ratingToInt(Rating rating)
	    {
	    	if (rating == Rating.THUMBSNULL)
	    		return 0;
	    	else if (rating == Rating.THUMBSUP)
	    		return 1;
	    	else
	    		return -1;
	    }
	    
	    public ArrayList<Matching> getAllMatchings()
	    {
	    	ArrayList<Matching> matchings = new ArrayList<Matching>();
	        // Select All Query
	        String selectQuery = "SELECT * FROM MATCHINGS";
	     
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	     
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	                Matching matching = new Matching();
	                matching.matchID = cursor.getInt(0);
	                matching.drinkID = cursor.getInt(1);
	                matching.ingredientID = cursor.getInt(2);
	                matching.quantity = cursor.getString(3);
	                matching.units = cursor.getString(4);
	                matchings.add(matching);
	            } while (cursor.moveToNext());
	        }
	     
	        return matchings;
	    }
	    
	    public ArrayList<IngredientIDPair> getAllPairs()
	    {
	    	ArrayList<IngredientIDPair> pairs = new ArrayList<IngredientIDPair>();
	        // Select All Query
	        String selectQuery = "SELECT * FROM INGREDIENTS";
	     
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	     
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	IngredientIDPair pair = new IngredientIDPair(cursor.getInt(0), cursor.getString(1));
	                pairs.add(pair);
	            } while (cursor.moveToNext());
	        }
	     
	        return pairs;
	    }
	    
	    public ArrayList<Drink> getAllDrinks()
	    {
	    	ArrayList<Drink> drinks = new ArrayList<Drink>();
	    	ArrayList<Matching> allMatches = getAllMatchings();
			ArrayList<IngredientIDPair> allPairs = getAllPairs();
			String selectQuery = "SELECT * FROM DRINKS;" ;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			
			if (cursor.moveToFirst())
			{
				do
				{
					int id = cursor.getInt(0);
					String name = cursor.getString(1);
					Drink.Rating rating =  intToRating(cursor.getInt(2));
					String instructions = cursor.getString(3);
					//stmt.close();
					ArrayList<Ingredient> ingredients = getIngredientsForDrinkID(id, allMatches, allPairs);

					Drink currDrink = new Drink(name, rating, ingredients, instructions, id);
					//System.out.println("Adding " + currDrink);
					drinks.add(currDrink);
				} while (cursor.moveToNext());
			}
			return drinks;
	    }
	    
	    private static ArrayList<Ingredient> getIngredientsForDrinkID(int drinkID, ArrayList<Matching> allMatches, ArrayList<IngredientIDPair> allPairs)
		{
			ArrayList<Ingredient> ingList = new ArrayList<Ingredient>();
			for (Matching currMatch : allMatches)
			{
				if (currMatch.drinkID == drinkID)
				{
					Ingredient currIngredient = new Ingredient();
					int ingredientID = currMatch.ingredientID;
					for (IngredientIDPair pair : allPairs)
					{
						if (pair.id == ingredientID)
						{
							currIngredient.setName(pair.name);
						}
					}
					currIngredient.setQuantity(currMatch.quantity);
					currIngredient.setUnits(currMatch.units);
					ingList.add(currIngredient);
				}
			}
			return ingList;
		}
	    
	    private Rating intToRating(int i)
	    {
	    	if (i == 1)
	    		return Rating.THUMBSUP;
	    	else if (i == -1)
	    		return Rating.THUMBSDOWN;
	    	else
	    		return Rating.THUMBSNULL;
	    }
}
