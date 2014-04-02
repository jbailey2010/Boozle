package com.bevinisaditch.theinebriator.Database;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.bevinisaditch.theinebriator.ClassFiles.Drink.Rating;
import com.bevinisaditch.theinebriator.ClassFiles.*;
import com.socialize.util.JSONParser;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class DrinkDatabaseHandler extends SQLiteOpenHelper 
{
		 
	    // All Static variables
	    // Database Version
	    private static final int DATABASE_VERSION = 1;
	 
	    // Database Name
	    private static final String DATABASE_NAME = "DrinksAndIngredients";
	    
	    private Context con;
	 	 
	    public DrinkDatabaseHandler(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        con = context;
	    }
	 
	    // Creating Tables
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	deleteTablesIfExist(db);
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
	        readJSONFiles();
	    }

	    /**
	     * Reads JSON files and populates database
	     */
		private void readJSONFiles() {
			
			
	        //2. Convert JSON to Java object
	        try {
	        	JSONParser parser = new JSONParser();
	        	JSONArray a = null;
	        	String drinks = readFile("drinks.json");
	        	System.out.println("drinks read");
	        	
	        	a = (JSONArray) parser.parseArray(drinks);
	        	System.out.println("drinks parsed");
	        	for (int i = 0; i < a.length(); i++)
	        	{
	        		JSONObject currDrink = a.getJSONObject(i);
	        		Drink drink = new Drink(currDrink.getString("name"), stringToRating(currDrink.getString("rating")), new ArrayList<Ingredient>(), currDrink.getString("instructions"),  currDrink.getInt("id"));
	        		addDrinkWithoutIngredients(drink);
	        	}
	        	String matches = readFile("matches.json");
	        	System.out.println("matches read");
	        	a = (JSONArray) parser.parseArray(matches);
	        	System.out.println("matches parsed");
	        	for (int i = 0; i < a.length(); i++)
	        	{
	        		JSONObject currMatching = a.getJSONObject(i);
	        		Matching match = new Matching(currMatching.getInt("drinkID"), currMatching.getInt("ingredientID"), currMatching.getInt("matchID"), currMatching.getString("quantity"), currMatching.getString("units"));
	        		addMatching(match);
	        	}
	        	String pairs = readFile("pairs.json");
	        	a = (JSONArray) parser.parseArray(pairs);
	        	System.out.println("pairs parsed");
	        	for (int i = 0; i < a.length(); i++)
	        	{
	        		JSONObject currPair = a.getJSONObject(i);
	        		IngredientIDPair pr = new IngredientIDPair(currPair.getInt("id"), currPair.getString("name"));
	        		addPair(pr);
	        	}
	        }
	        catch (Exception e)
	        {
	        	e.printStackTrace();
	        	System.out.println(e);
	        }
		}
	 
	    // Upgrading database
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // Drop older table if existed
	        deleteTablesIfExist(db);
	 
	        // Create tables again
	        onCreate(db);
	    }

		private void deleteTablesIfExist(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS DRINKS");
	        db.execSQL("DROP TABLE IF EXISTS INGREDIENTS");
	        db.execSQL("DROP TABLE IF EXISTS MATCHINGS");
		}
	    
	    /**
	     * Adds a drink to the database, ignoring its ingredients.
	     * @param drink
	     */
	    public void addDrinkWithoutIngredients(Drink drink)
	    {
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	 
	        ContentValues values = new ContentValues();
	        values.put("ID", drink.getId()); 
	        values.put("NAME", drink.getName());
	        values.put("RATING", ratingToInt(drink.getRating()));
	        values.put("INSTRUCTIONS", drink.getInstructions());
	     
	        // Inserting Row
	        db.insert("DRINKS", null, values);
	    }
	    
	    /**
	     * Adds an IngredientIDPair to the database.
	     * @param pair
	     */
	    public void addPair(IngredientIDPair pair)
	    {
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	ContentValues values = new ContentValues();
	    	values.put("ID", pair.id);
	    	values.put("NAME", pair.name);
	    	
	    	db.insert("INGREDIENTS", null, values);
	    }
	    
	    /**
	     * Adds a matching to the database.
	     * @param match
	     */
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
	    
	    private Rating stringToRating(String str)
	    {
	    	if (str.equals("THUMBSNULL"))
	    		return Rating.THUMBSNULL;
	    	else if (str.equals("THUMBSUP"))
	    		return Rating.THUMBSUP;
	    	else if (str.equals("THUMBSDOWN"))
	    		return Rating.THUMBSDOWN;
	    	else
	    		return Rating.THUMBSNULL;
	    }
	    
	    /**
	     * Gets all matchings in the database
	     * @return
	     */
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
	    
	    /**
	     * Gets all IngredientIDPairs in the database
	     * @return
	     */
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
	    
	    /**
	     * Gets all drinks in the database with ingredients
	     * @return
	     */
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
	    
	    /**
	     * Gets all ingredients for a given drink ID
	     * @param drinkID
	     * @param allMatches
	     * @param allPairs
	     * @return
	     */
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
	    
	    /**
	     * Converts an int to a rating
	     * @param i
	     * @return
	     */
	    private Rating intToRating(int i)
	    {
	    	if (i == 1)
	    		return Rating.THUMBSUP;
	    	else if (i == -1)
	    		return Rating.THUMBSDOWN;
	    	else
	    		return Rating.THUMBSNULL;
	    }
	    
	    /**
	     * 
	     * @param filename path to the file
	     * @return A string containing the content of the file found at filename
	     */
	    private String readFile(String filename)
	    {
	    	String ret = null;
	    	try {
	    		AssetManager assets = con.getResources().getAssets();
	    		InputStream is = assets.open(filename);
	    		BufferedReader br = null;
	    		StringBuilder sb = new StringBuilder();

	    		String line;
	    		try {

	    			br = new BufferedReader(new InputStreamReader(is));
	    			while ((line = br.readLine()) != null) {
	    				sb.append(line);
	    			}

	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		} finally {
	    			if (br != null) {
	    				try {
	    					br.close();
	    				} catch (IOException e) {
	    					e.printStackTrace();
	    				}
	    			}
	    		}
	    		ret = sb.toString();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    	return ret;
	    }
}
