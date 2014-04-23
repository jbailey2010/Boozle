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
	    private int numDrinksRead = 0;
	    private static final String DRINK_FILE_NAME = "drinkDataShort.txt";
	    private static final String MATCH_FILE_NAME = "matchDataShort.txt";
	    private static final String PAIR_FILE_NAME = "pairDataShort.txt";
	 	  
	    public DrinkDatabaseHandler(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	        con = context; 
	    } 
	    
	    public void reCreateTables()
	    { 
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	deleteTablesIfExist(db);
	    	System.out.println("Deleted old tables");
	    	onCreate(db);
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
	        System.out.println("Created new tables");
	        readDrinks(db);
	        System.out.println("Drinks read");
	        readMatchings(db);
	        System.out.println("Matchings read");
	        readPairs(db);
	        System.out.println("Pairs read");
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
		 * Thumbs up the drink with given ID
		 * @param drinkID
		 */
		public void thumbsUpDrink(int drinkID)
		{
			SQLiteDatabase db = this.getWritableDatabase();
			String updateText = "UPDATE DRINKS " + "SET RATING = 1 " + "WHERE ID = " + drinkID;
			db.execSQL(updateText);
		}
		
		/**
		 * Sets the rating to no thumbs up nor thumbs down for the drink with the given id
		 * @param drinkID
		 */
		public void thumbsNullDrink(int drinkID)
		{
			SQLiteDatabase db = this.getWritableDatabase();
			String updateText = "UPDATE DRINKS " + "SET RATING = 0 " + "WHERE ID = " + drinkID;
			db.execSQL(updateText);
		}
		
		/**
		 * Thumbs down the drink with given ID
		 * @param drinkID
		 */
		public void thumbsDownDrink(int drinkID)
		{
			SQLiteDatabase db = this.getWritableDatabase();
			String updateText = "UPDATE DRINKS " + "SET RATING = -1 " + "WHERE ID = " + drinkID;
			db.execSQL(updateText);
		}
		
		public void setDrinkRating(int drinkID, Rating rating)
		{
			if (rating == Rating.THUMBSUP)
				thumbsUpDrink(drinkID);
			else if (rating == Rating.THUMBSDOWN)
				thumbsDownDrink(drinkID);
			else
				thumbsNullDrink(drinkID);
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
	    
	    public void addDrinkByVars(int id, String name, int rating, String instructions, SQLiteDatabase db)
	    {
	    	ContentValues values = new ContentValues();
	    	values.put("ID", id);
	    	values.put("NAME", name);
	    	values.put("RATING", rating);
	    	values.put("INSTRUCTIONS", instructions);
	    	db.insert("DRINKS", null, values);
	    }
	    
	    /**
	     * Adds an IngredientIDPair to the database.
	     * @param pair
	     */
	    public void addPair(IngredientIDPair pair, SQLiteDatabase db)
	    {
	    	ContentValues values = new ContentValues();
	    	values.put("ID", pair.id);
	    	values.put("NAME", pair.name);
	    	
	    	db.insert("INGREDIENTS", null, values);
	    }
	    
	    /**
	     * Adds a matching to the database.
	     * @param match
	     */
	    public void addMatching(Matching match, SQLiteDatabase db)
	    {
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
	    	System.out.println("Got all matchings from db");
			ArrayList<IngredientIDPair> allPairs = getAllPairs();
			System.out.println("Got all pairs from db");
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
			System.out.println("Got all drinks from db");
			return drinks;
	    }
	    
	    public ArrayList<Drink> getRelevantDrinksByName(ArrayList<String> terms) {
	    	SQLiteDatabase db = this.getWritableDatabase();
	    	ArrayList<Matching> allMatches = getAllMatchings();
	    	ArrayList<IngredientIDPair> allPairs = getAllPairs();
	    	
	    	String selectQuery = "SELECT * FROM DRINKS WHERE ";
	    	
	    	if (terms != null && terms.size() > 0) {
	    		selectQuery += "NAME LIKE '%" + terms.get(0) + "%'";
	    		terms.remove(0);
	    	}
	    	
	    	for (String term : terms) {
	    		selectQuery += " OR NAME LIKE '%" + term + "%'";
	    		
	    	}
	    	
	    	Cursor cursor = db.rawQuery(selectQuery, null);
	    	
	    	ArrayList<Drink> drinks = new ArrayList<Drink>();
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
			System.out.println("Got all drinks from db");
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
					currIngredient.setName(allPairs.get(currMatch.ingredientID).name);
					currIngredient.setQuantity(currMatch.quantity);
					currIngredient.setUnits(currMatch.units);
					ingList.add(currIngredient);
				}
				else if (currMatch.drinkID > drinkID)
				{
					break;
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
	    private void readDrinks(SQLiteDatabase db)
	    {
	    	if (db == null)
	    	{
	    		db = this.getWritableDatabase();
	    	}
	    	try {
	    		AssetManager assets = con.getResources().getAssets();
	    		InputStream is = assets.open(DRINK_FILE_NAME);
	    		BufferedReader br = null;

	    		String line;
	    		try {
	    			int linesRead = 0;
	    			br = new BufferedReader(new InputStreamReader(is));
	    			while ((line = br.readLine()) != null) 
	    			{
	    				linesRead++;
	    				if (!line.equals("--"))
	    				{
	    					System.out.println("Problem: not --");
	    				}
	    				int id; 
	    				String name;
	    				int rating;
	    				String instructions;
	    				id = Integer.parseInt(br.readLine());
	    				name = br.readLine();
	    				rating = stringToRatingToInt(br.readLine());
	    				instructions = br.readLine();
	    				addDrinkByVars(id, name, rating, instructions, db);
	    				numDrinksRead++;
	    				if (numDrinksRead % 1000 == 0)
	    					System.out.println("numDrinksRead: " + numDrinksRead);
	    			}
	    			System.out.println("Drink lines read: " + linesRead);

	    		} catch (IOException e) {
	    			e.printStackTrace();
	    			System.out.println(e);
	    		} finally {
	    			if (br != null) {
	    				try {
	    					br.close();
	    				} catch (IOException e) {
	    					e.printStackTrace();
	    				}
	    			}
	    		}
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	    
	    private void readMatchings(SQLiteDatabase db)
	    {
	    	if (db == null)
	    	{
	    		db = this.getWritableDatabase();
	    	}
	    	try {
	    		AssetManager assets = con.getResources().getAssets();
	    		InputStream is = assets.open(MATCH_FILE_NAME);
	    		BufferedReader br = null;
	    		//id
	    		//drink
	    		//ingredient
	    		//quantity
	    		//units
	    		String line;
	    		try {

	    			br = new BufferedReader(new InputStreamReader(is));
	    			while ((line = br.readLine()) != null) {
	    				if (!line.equals("--"))
	    				{
	    					System.out.println("Problem: not --");
	    				}
	    				int id = Integer.parseInt(br.readLine());
	    				int drinkID = Integer.parseInt(br.readLine());
	    				int ingredientID = Integer.parseInt(br.readLine());
	    				String quantity = br.readLine();
	    				if (quantity.equals("null"))
	    					quantity = "";
	    				String units = br.readLine();
	    				if (units.equals("null"))
	    					units = "";
	    				Matching match = new Matching(drinkID, ingredientID, id, quantity, units);
	    				addMatching(match, db);
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
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	    
	    private void readPairs(SQLiteDatabase db)
	    {
	    	if (db == null)
	    	{
	    		db = this.getWritableDatabase();
	    	}
	    	try {
	    		AssetManager assets = con.getResources().getAssets();
	    		InputStream is = assets.open(PAIR_FILE_NAME);
	    		BufferedReader br = null;
	    		//id
	    		//name
	    		String line;
	    		try {

	    			br = new BufferedReader(new InputStreamReader(is));
	    			while ((line = br.readLine()) != null) {
	    				if (!line.equals("--"))
	    				{
	    					System.out.println("Problem: not --");
	    				}
	    				int id = Integer.parseInt(br.readLine());
	    				String name = br.readLine();
	    				IngredientIDPair pair = new IngredientIDPair(id, name);
	    				addPair(pair, db);
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
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	    
	    private int stringToRatingToInt(String str)
	    {
	    	if (str.equals("THUMBSUP"))
	    	{
	    		return 1;
	    	}
	    	else if (str.equals("THUMBSDOWN"))
	    	{
	    		return -1;
	    	}
	    	else
	    	{
	    		return 0;
	    	}
	    }
}
