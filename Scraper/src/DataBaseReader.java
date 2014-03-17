import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;

public class DataBaseReader {
	
	public static void main(String[] args)
	{
		for (Drink currDrink : getAllDrinks())
		{
			System.out.print("Drink #" + currDrink.getId());
			System.out.println("\t\t\tName: " + currDrink.getName());
			System.out.println("Instructions: " + currDrink.getInstructions());
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
			Class.forName("org.sqlite.JDBC");
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
		} catch (SQLException|ClassNotFoundException e) {
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
			Class.forName("org.sqlite.JDBC");
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
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:drinks.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
			ResultSet results = stmt.executeQuery(command);
			while(results.next()) {
				return results.getInt("ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}
}
