import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;


public class DataBaseReader {


	private static Connection c = null;

	public static void main(String[] args) throws Exception
	{
		//transformDataBase();

	}

	static ArrayList<Drink> allDrinks = new ArrayList<Drink>();
	static ArrayList<Matching> allMatches = new ArrayList<Matching>();
	static ArrayList<IngredientIDPair> allPairs = new ArrayList<IngredientIDPair>();
	public static ArrayList<Drink> getAllDrinksTwo()
	{
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
		} catch (SQLException|ClassNotFoundException e) {
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

	public static void transformDataBase() throws Exception
	{
		ArrayList<String> allIngredientNames = new ArrayList<String>();
		ArrayList<DBDrink> allDBDrinks = new ArrayList<DBDrink>();
		for (Drink currDrink : getAllDrinks())
		{
			DBDrink dataBaseDrink = new DBDrink(currDrink);
			//printDrink(currDrink);
			for (Ingredient currIngredient : currDrink.getIngredients())
			{
				int id = allIngredientNames.size();
				String currName = currIngredient.getName();
				boolean nameAlreadyExists = false;

				for (int i = 0; i < allIngredientNames.size(); i++)
				{
					String str = allIngredientNames.get(i);
					if (str.equals(currName))
					{
						nameAlreadyExists = true;
						id = i;
						break;
					}
				}
				if (!nameAlreadyExists)
				{
					allIngredientNames.add(currName);
				}
				DBIngredient dataBaseIngredient = new DBIngredient(id, currIngredient.getQuantity(), currIngredient.getUnits());
				dataBaseDrink.ingredients.add(dataBaseIngredient);
			}
			allDBDrinks.add(dataBaseDrink);
		}
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:drinksAndIngredientsFour.db");
		createDrinksTable();
		//checkFoDup(allDBDrinks);
		insertDrinkListToDB(allDBDrinks);
		createIngredientsTable();
		insertIngredientListToDB(allIngredientNames);
		createMatchingTable();
		insertMatchingsToDB(allDBDrinks);
		c.close();
	}

	private static void checkFoDup(ArrayList<DBDrink> allDrinks)
	{
		for (DBDrink curr : allDrinks)
		{
			for (DBDrink other : allDrinks)
			{
				if (curr != other)
				{
					if (curr.id == other.id)
					{
						System.out.println("ERR===========" + curr.id);
					}
				}
			}
		}
	}

	private static void createMatchingTable() throws Exception {
		Statement stmt = null;
		stmt = c.createStatement();
		String command= "CREATE TABLE MATCHINGS" +
				"(ID INT PRIMARY KEY       NOT NULL," +
				"DRINKID        INT        NOT NULL," +
				"INGREDIENTID   INT        NOT NULL," +
				"QUANTITY       TEXT," +
				"UNITS          TEXT)";
		stmt.executeUpdate(command);
		stmt.close();
	}
	private static void createIngredientsTable() throws Exception {
		Statement stmt = null;
		stmt = c.createStatement();
		String command = "CREATE TABLE INGREDIENTS" +
				"(ID INT PRIMARY KEY      NOT NULL," +
				"NAME           TEXT      NOT NULL)";
		stmt.executeUpdate(command);
		stmt.close();
	}
	private static void createDrinksTable() throws Exception {
		Statement stmt = null;
		stmt = c.createStatement();
		String command = "CREATE TABLE DRINKS" +
				"(ID INT PRIMARY KEY       NOT NULL," +
				"NAME           TEXT       NOT NULL," +
				"RATING         INT        NOT NULL," +
				"INSTRUCTIONS   TEXT       NOT NULL)" ;
		stmt.executeUpdate(command);
		stmt.close();
	}
	private static void insertDrinkListToDB(ArrayList<DBDrink> currList)
	{
		System.out.println("Begin insertDrinkListToDB");
		Statement stmt;
		for (DBDrink currDrink : currList)
		{
			//System.out.println(currDrink.id);
			try
			{
				stmt = c.createStatement();
				String insertCommand = "INSERT INTO DRINKS" +
						" VALUES(" + currDrink.id + ", '" + currDrink.name + "', " + ratingToInt(currDrink.rating) + ", '" + currDrink.instructions + "')";
				//System.out.println(insertCommand);
				stmt.executeUpdate(insertCommand);
				stmt.close();
			}
			catch (SQLException e)
			{
				//System.out.println(currDrink.id);
				e.printStackTrace();
			}
		}
	}
	private static void insertIngredientListToDB(ArrayList<String> names)
	{
		int id = 0;
		Statement stmt;
		for (String str : names) {
			try
			{
				stmt = c.createStatement();
				String insertCommand = "INSERT INTO INGREDIENTS" +
						" VALUES(" + id + ", '" + str + "')";
				id++;
				stmt.executeUpdate(insertCommand);
				stmt.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	private static void insertMatchingsToDB(ArrayList<DBDrink> drinks) throws SQLException
	{
		int id = 0;
		Statement stmt;
		for (DBDrink currDrink : drinks)
		{
			for (DBIngredient currIngredient : currDrink.ingredients)
			{
				stmt = c.createStatement();
				String insertCommand = "INSERT INTO MATCHINGS" +
						" VALUES(" + id + ", " + currDrink.id + ", " + currIngredient.nameID + ", '" + currIngredient.qty + "', '" + currIngredient.units + "')";
				id++;
				stmt.executeUpdate(insertCommand);
				stmt.close();
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
			c.close();
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
