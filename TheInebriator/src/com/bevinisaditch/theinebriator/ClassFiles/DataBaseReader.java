package com.bevinisaditch.theinebriator.ClassFiles;

import java.util.ArrayList;
import java.sql.*;

public class DataBaseReader {
	
	static ArrayList<Drink> allDrinks = new ArrayList<Drink>();
	static ArrayList<Matching> allMatches = new ArrayList<Matching>();
	static ArrayList<IngredientIDPair> allPairs = new ArrayList<IngredientIDPair>();
	private static Connection c = null;

	private static void printDrink(Drink currDrink) {
		System.out.print("Drink #" + currDrink.getId());
		System.out.println("\t\t\tName: " + currDrink.getName());
		for (Ingredient currIngredient : currDrink.getIngredients())
		{
			System.out.println(currIngredient.toString());
		}
	}
	

	
	/**
	 * Gets all drinks from the database
	 * @return An array list with all of the drinks
	 */
	public static ArrayList<Drink> getAllDrinks()
	{
		allDrinks = new ArrayList<Drink>();
		allMatches = new ArrayList<Matching>();
		allPairs = new ArrayList<IngredientIDPair>();
		c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:drinksAndIngredientsFour.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs2 = stmt.executeQuery( "SELECT * FROM MATCHINGS");
			while (rs2.next())
			{
				int matchid = rs2.getInt("id");
				int drinkid = rs2.getInt("drinkid");
				int ingredientid = rs2.getInt("ingredientid");
				String quantity = rs2.getString("quantity");
				String units = rs2.getString("units");
				Matching currMatch = new Matching(drinkid, ingredientid, matchid, quantity, units);
				allMatches.add(currMatch);
			}
			stmt.close();
			stmt = c.createStatement();
			ResultSet rs3 = stmt.executeQuery("SELECT * FROM INGREDIENTS");
			while (rs3.next())
			{
				int id = rs3.getInt("id");
				String name = rs3.getString("name");
				IngredientIDPair pair = new IngredientIDPair(id, name);
				allPairs.add(pair);
			}
			stmt.close();
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM DRINKS;" );
			while ( rs.next() ) 
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Drink.Rating rating =  intToRating(rs.getInt("rating"));
				String instructions = rs.getString("instructions");
				//stmt.close();
				ArrayList<Ingredient> ingredients = getIngredientsForDrinkID(id);
				
				Drink currDrink = new Drink(name, rating, ingredients, instructions, id);
				//System.out.println("Adding " + currDrink);
				allDrinks.add(currDrink);
			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return allDrinks;
	}
	
	private static ArrayList<Ingredient> getIngredientsForDrinkID(int drinkID)
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
			}
		}
		return ingList;
	}
	
	private static Drink.Rating intToRating(int n)
	{
		if (n == -1)
			return Drink.Rating.THUMBSDOWN;
		else if (n == 0)
			return Drink.Rating.THUMBSNULL;
		else
			return Drink.Rating.THUMBSUP;
	}
}