import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;


public class TestSQL {

	static int id = 0;
	public static void main( String args[] )
	{
		Connection c = null;
		Statement stmt = null;

		try {   

			int max = 20;
			
			String COLUMN_INGREDIENTS = "ingredient_0 TEXT, quantity_0 TEXT, unit_0 TEXT";
			for(int i = 1; i < max; i++) {
				COLUMN_INGREDIENTS += ", ingredient_" + i + " TEXT, quantity_" + i + " TEXT, unit_" + i + " TEXT";
			}


			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:drinks.db");
			stmt = c.createStatement();
			String command = "CREATE TABLE DRINKS" +
					"(ID INT PRIMARY KEY       NOT NULL," +
					"NAME           TEXT       NOT NULL," +
					"RATING         INT        NOT NULL," +
					"INSTRUCTIONS   TEXT       NOT NULL," +
					COLUMN_INGREDIENTS + ")";
			stmt.executeUpdate(command);
			stmt.close();
			
			
			ArrayList<Drink> dranks_1 = DrinkOfTheWeekScraper.scrapeDrinks();
			System.out.println("COMPLETED SCRAPING DrinkOfTheWeek");
			insertListToDB(c, dranks_1);
			ArrayList<Drink> dranks_2 = DrinkMixerScraper.scrapeDrinks();
			System.out.println("COMPLETED SCRAPING DrinkMixer");
			insertListToDB(c, dranks_2);
			ArrayList<Drink> dranks_3 = GoodCocktailsScraper.returnScrapedDrinks();
			System.out.println("COMPLETED SCRAPING GoodCocktails");
			insertListToDB(c, dranks_3);
			
			

			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	private static void insertListToDB(Connection c, ArrayList<Drink> currList)
	{
		Statement stmt;
		for (Drink currDrink : currList)
		{
			try
			{
				stmt = c.createStatement();
				String insertCommand = "INSERT INTO DRINKS" +
						" VALUES(" + id + ", '" + currDrink.getName() + "', " + ratingToInt(currDrink.getRating()) + ", '" + currDrink.getInstructions() + "'";
				int numColumnsRemaining = 60;
				for (Ingredient currIngredient : currDrink.getIngredients())
				{
					insertCommand += ", '" + currIngredient.getName() + "', '" + currIngredient.getQuantity() + "', '" + currIngredient.getUnits() + "'";
					numColumnsRemaining -= 3;
				}
				while (numColumnsRemaining > 0)
				{
					insertCommand += ", null";
					numColumnsRemaining--;
				}
				insertCommand += ")";
				stmt.executeUpdate(insertCommand);
				stmt.close();
				id++;
				System.out.println("ID: " + id + "\t\t");
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private static int ratingToInt(Drink.Rating rating)
	{
		if (rating == Drink.Rating.THUMBSDOWN)
			return -1;
		else if (rating == Drink.Rating.THUMBSNULL)
			return 0;
		else
			return 1;
	}
}
