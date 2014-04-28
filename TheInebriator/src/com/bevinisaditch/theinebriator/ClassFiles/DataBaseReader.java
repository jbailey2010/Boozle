package com.bevinisaditch.theinebriator.ClassFiles;


import java.util.ArrayList;
import java.sql.*;

public class DataBaseReader {

	static ArrayList<Drink> allDrinks = new ArrayList<Drink>();
	static ArrayList<Matching> allMatches = new ArrayList<Matching>();
	static ArrayList<IngredientIDPair> allPairs = new ArrayList<IngredientIDPair>();
	private static Connection c = null;
	//"org.sqlite.JDBC"
	//"SQLite.JDBCDRIVER"
	private static final String DRIVER = "org.sqlite.JDBC";

	private static void printDrink(Drink currDrink) {
		System.out.print("Drink #" + currDrink.getId());
		System.out.println("\t\t\tName: " + currDrink.getName());
		for (Ingredient currIngredient : currDrink.getIngredients())
		{
			System.out.println(currIngredient.toString());
		}
	}

	public static ArrayList<Ingredient> getIngredients(int drinkId) {
		return null;
		/** TODO
		 * return an arraylist of ingredients
		 */
	}

	public static Drink getDrink(int id)
	{
		String command = "SELECT * FROM DRINKS WHERE ID="+id;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName(DRIVER);
			c = DriverManager.getConnection("jdbc:sqlite:drinks.db");
			c.setAutoCommit(false);
			//System.out.println("Opened database successfully");
			stmt = c.createStatement();
			ResultSet results = stmt.executeQuery(command);
			Drink retDrink = new Drink(results.getString("NAME"));
			retDrink.setInstructions(results.getString("INSTRUCTIONS"));
			retDrink.setRating(intToRating(results.getInt("RATING")));
			retDrink.setIngredients(getIngredients(results.getInt("ID")));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
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
			Class.forName(DRIVER);
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
			
			/*for (Matching match : allMatches)
			{
				System.out.println("Match: " + match.drinkID + ", " + match.ingredientID);
			}
			for (IngredientIDPair pr : allPairs)
			{
				System.out.println("Pair: " + pr.id + ", " + pr.name);
			}*/
			
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

	public static void toggleThumbs(int drink_id, Drink.Rating thumb) {
		String command = "UPDATE DRINKS SET RATING="+ratingToInt(thumb)+" WHERE ID=" + drink_id;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName(DRIVER);
			c = DriverManager.getConnection("jdbc:sqlite:drinks.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
			stmt.executeUpdate(command);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}
	/**
	 *
	 * @param n
	 * @return
	 */
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
				ingList.add(currIngredient);
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
	/**
	 *
	 * @param thumb
	 * @return
	 */
	private static int ratingToInt(Drink.Rating thumb) {
		switch(thumb) {
			case THUMBSDOWN:
				return -1;
			case THUMBSUP:
				return 1;
			default:
				return 0;
		}

	}
	/**
	 *
	 * @param name
	 * @param instructions
	 * @return
	 */
	public static int idFromNameAndInst(String name, String instructions) {
		String command = "SELECT ID FROM DRINKS WHERE NAME='"+name+"', INSTRUCTIONS='"+instructions;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName(DRIVER);
			c = DriverManager.getConnection("jdbc:sqlite:drinks.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
			ResultSet results = stmt.executeQuery(command);
				return results.getInt("ID");
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
