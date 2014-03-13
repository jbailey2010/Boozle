package com.bevinisaditch.theinebriator.ClassFiles;

import java.util.ArrayList;
import java.sql.*;

public class DataBaseReader {
	
	public static void main(String[] args)
	{
		for (Drink currDrink : getAllDrinks())
		{
			System.out.print("Drink #" + currDrink.getId());
			System.out.println("\t\t\tName: " + currDrink.getName());
			for (Ingredient currIngredient : currDrink.getIngredients())
			{
				System.out.println(currIngredient.toString());
			}
		}
	}
	
	public static ArrayList<Drink> getAllDrinks()
	{
		ArrayList<Drink> allDrinks = new ArrayList<Drink>();
		Connection c = null;
		Statement stmt = null;
		try {
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			c = DriverManager.getConnection("jdbc:sqlite:drinks.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM DRINKS;" );
			while ( rs.next() ) 
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Drink.Rating rating =  intToRating(rs.getInt("rating"));
				String instructions = rs.getString("instructions");
				ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
				for (int i = 0; i < 20; i++)
				{
					if (rs.getString(5 + (3 * i)) != null)
					{
						ingredients.add(new Ingredient(rs.getString(5 + 3 * i), rs.getString(6 + 3 * i), rs.getString(7 + 3 * i)));
					}
				}
				Drink currDrink = new Drink(name, rating, ingredients, instructions, id);
				allDrinks.add(currDrink);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return allDrinks;
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