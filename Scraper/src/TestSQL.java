import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;


public class TestSQL {

	public static void main( String args[] )
	{
		Connection c = null;
		Statement stmt = null;

		try {   

			ArrayList<Drink> dranks_1 = DrinkOfTheWeekScraper.scrapeDrinks();
			ArrayList<Drink> dranks_2 = DrinkMixerScraper.scrapeDrinks();
			ArrayList<Drink> dranks_3 = GoodCocktailsScraper.returnScrapedDrinks();
			ArrayList<ArrayList<Drink>> listOfLists = new ArrayList<ArrayList<Drink>>();
			listOfLists.add(dranks_1);
			listOfLists.add(dranks_2);
			listOfLists.add(dranks_3);
			int max = 0;
			for(int i = 0; i < dranks_1.size(); i++) {
				if(dranks_1.get(i).getIngredients().size() > max) {
					max = dranks_1.get(i).getIngredients().size();
				}
			}
			for(int i = 0; i < dranks_2.size(); i++) {
				if(dranks_2.get(i).getIngredients().size() > max) {
					max = dranks_2.get(i).getIngredients().size();
				}
			}
			for(int i = 0; i < dranks_3.size(); i++) {
				if(dranks_3.get(i).getIngredients().size() > max) {
					max = dranks_3.get(i).getIngredients().size();
				}
			}
			String COLUMN_INGREDIENTS = "ingredient_0 TEXT NOT NULL, quantity_0 TEXT, unit_0 TEXT";
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
			
			int id = 0;

			for (ArrayList<Drink> currList : listOfLists)
			{
				for (Drink currDrink : currList)
				{
					stmt = c.createStatement();
					String insertCommand = "INSERT INTO DRINKS" +
							" VALUES(" + id + ", '" + currDrink.getName() + "', " + ratingToInt(currDrink.getRating()) + ", '" + currDrink.getInstructions() + "'";
					for (Ingredient currIngredient : currDrink.getIngredients())
					{
						insertCommand += ", '" + currIngredient.getName() + "', '" + currIngredient.getQuantity() + "', '" + currIngredient.getUnits() + "'";
					}
					insertCommand += ")";
					stmt.executeUpdate(insertCommand);
					stmt.close();
					id++;
				}
			}



			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Opened database successfully");
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
