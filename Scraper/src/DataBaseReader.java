import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;



public class DataBaseReader {


	private static final String CLASS_NAME = "org.sqlite.JDBC";
	private static Connection c = null;
	private static ArrayList<Integer> ingredientIDs;
	private static int maxIngredientID;
	private static final int NUM_DRINKS = 13000;

	public static void main(String[] args) throws Exception
	{
		transformDataBase(DataBaseFixer.fixDrinks());
		
		
		c = DriverManager.getConnection("jdbc:sqlite:drinksAndIngredientsFive.db");
		c.setAutoCommit(false);
		ArrayList<Drink> drinks;
		ArrayList<Matching> matchings;
		ArrayList<IngredientIDPair> pairs;
		matchings = getMatches();
		pairs = getPairs();
		drinks = getDrinks();
		c.close();
		writeDrinks(drinks);
		writeMatchings(matchings);
		writePairs(pairs);
		//writeDrinkTest(10, getAllDrinksTwo());
		
	}
	
	private static void writeDrinkTest(int numTests, ArrayList<Drink> drinks)
	{
		/*
		ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("1 part DeKuyper Pucker sour-apple schnapps", "", ""));
		ingredients.add(new Ingredient("1 part Absolut vodka", "", ""));
		Drink sampleDrink = new Drink("2-Point Play", Drink.Rating.THUMBSNULL, ingredients, "Pour ingredients into a cocktail shaker with ice. Shake and strain into a chilled martini glass. Garnish with an apple wedge.", 0);
		assertDrinkFound(sampleDrink);
		*/
		for (int i = 0; i < numTests; i++)
		{
			Drink currDrink = drinks.get(i);
			System.out.println("ingredients = new ArrayList<Ingredient>();");
			for (Ingredient currIngredient : currDrink.getIngredients())
			{
				if (currIngredient.getQuantity() == null || currIngredient.getQuantity().equals("null"))
					currIngredient.setQuantity("");
				if (currIngredient.getUnits() == null || currIngredient.getUnits().equals("null"))
					currIngredient.setUnits("");
				System.out.println("ingredients.add(new Ingredient(\"" + currIngredient.getName() + "\", \"" + currIngredient.getQuantity() + "\", \"" + currIngredient.getUnits()+ "\"));");
			}
			System.out.println("sampleDrink = new Drink(\"" + currDrink.getName() + "\", " + ratingToString(currDrink.getRating()) + ", ingredients, \"" + currDrink.getInstructions() + "\", " + currDrink.getId() + ");");
			System.out.println("assertDrinkFound(sampleDrink);");
		}
	}
	
	private static String ratingToString(Drink.Rating rating)
	{
		if (rating == Drink.Rating.THUMBSUP)
		{
			return "Drink.Rating.THUMBSUP";
		}
		else if (rating == Drink.Rating.THUMBSDOWN)
		{
			return "Drink.Rating.THUMBSDOWN";
		}
		else
			return "Drink.Rating.THUMBSNULL";
	}

	private static void writeDrinks(ArrayList<Drink> drinks)
	{
		try {
 
			File file = new File("drinkDataShort.txt");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			else
			{
				file.delete();
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//--
			//ID
			//Name
			//Rating
			//Instructions
			for (Drink currDrink : drinks)
			{
				String line = "--\n" + currDrink.getId() + "\n" + currDrink.getName() + "\n" + currDrink.getRating() + "\n" + currDrink.getInstructions() + "\n";
				bw.write(line);
			}
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeMatchings(ArrayList<Matching> matches)
	{
		try {
			 
			File file = new File("matchDataShort.txt");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			else
			{
				file.delete();
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//--
			//ID
			//DrinkID
			//IngredientID
			//Quantity
			//Units
			for (Matching currMatch : matches)
			{
				String line = "--\n" + currMatch.matchID + "\n" + currMatch.drinkID + "\n" + currMatch.ingredientID + "\n" + currMatch.quantity + "\n" + currMatch.units + "\n";
				bw.write(line);
			}
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writePairs(ArrayList<IngredientIDPair> pairs)
	{
		try {
			 
			File file = new File("pairDataShort.txt");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			else
			{
				file.delete();
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//--
			//ID
			//Name
			for (IngredientIDPair currPair : pairs)
			{
				String line = "--\n" + currPair.id + "\n" + currPair.name + "\n";
				bw.write(line);
			}
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static ArrayList<Drink> allDrinks = new ArrayList<Drink>();
	static ArrayList<Matching> allMatches = new ArrayList<Matching>();
	static ArrayList<IngredientIDPair> allPairs = new ArrayList<IngredientIDPair>();
	public static ArrayList<Drink> getAllDrinksTwo()
	{
		c = null;
		Statement stmt = null;
		try {
			Class.forName(CLASS_NAME);
			c = DriverManager.getConnection("jdbc:sqlite:drinksAndIngredientsFour.db");
			c.setAutoCommit(false);
			allMatches = getMatches();
			allPairs = getPairs();
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

	private static ArrayList<Drink> getDrinks() throws SQLException {
		ArrayList<Drink> drinks = new ArrayList<Drink>();
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM DRINKS WHERE ID <= "+NUM_DRINKS+";" );
		while ( rs.next() )
		{
			int id = rs.getInt("id");
			String name = rs.getString("name");
			Drink.Rating rating =  intToRating(rs.getInt("rating"));
			String instructions = rs.getString("instructions");
			ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

			Drink currDrink = new Drink(name, rating, ingredients, instructions, id);
			drinks.add(currDrink);
		}
		stmt.close();
		return drinks;
	}
	
	private static ArrayList<IngredientIDPair> getPairs() throws SQLException {
		ArrayList<IngredientIDPair> pairs = new ArrayList<IngredientIDPair>();
		Statement stmt;
		stmt = c.createStatement();
		ResultSet rs3 = stmt.executeQuery("SELECT * FROM INGREDIENTS WHERE ID <= " + maxIngredientID);
		while (rs3.next())
		{
			int id = rs3.getInt("id");
			String name = rs3.getString("name");
			IngredientIDPair pair = new IngredientIDPair(id, name);
			pairs.add(pair);
		}
		stmt.close();
		return pairs;
	}

	private static ArrayList<Matching> getMatches() throws SQLException {
		ArrayList<Matching> matchings = new ArrayList<Matching>();
		Statement stmt;
		stmt = c.createStatement();
		ResultSet rs2 = stmt.executeQuery( "SELECT * FROM MATCHINGS WHERE DRINKID <= "+NUM_DRINKS+"");
		ingredientIDs = new ArrayList<Integer>();
		while (rs2.next())
		{
			int matchid = rs2.getInt("id");
			int drinkid = rs2.getInt("drinkid");
			int ingredientid = rs2.getInt("ingredientid");
			ingredientIDs.add(ingredientid);
			String quantity = rs2.getString("quantity");
			String units = rs2.getString("units");
			Matching currMatch = new Matching(drinkid, ingredientid, matchid, quantity, units);
			matchings.add(currMatch);
		}
		stmt.close();
		maxIngredientID = getMaxIngredientID();
		return matchings;
	}
	
	private static int getMaxIngredientID()
	{
		int max = 0;
		for (Integer curr : ingredientIDs)
		{
			if (curr > max)
			{
				max = curr;
			}
		}
		return max;
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
				ingList.add(currIngredient);
			}
		}
		return ingList;
	}

	public static void transformDataBase(ArrayList<Drink> allDrinks) throws Exception
	{
		ArrayList<String> allIngredientNames = new ArrayList<String>();
		ArrayList<DBDrink> allDBDrinks = new ArrayList<DBDrink>();
		for (Drink currDrink : allDrinks)
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
		Class.forName(CLASS_NAME);
		c = DriverManager.getConnection("jdbc:sqlite:drinksAndIngredientsFive.db");
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
		String deleteCommand = "DROP TABLE MATCHINGS";
		stmt.executeUpdate(deleteCommand);
		stmt.close();
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
		String deleteCommand = "DROP TABLE INGREDIENTS";
		stmt.executeUpdate(deleteCommand);
		stmt.close();
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
		String deleteCommand = "DROP TABLE DRINKS";
		stmt.executeUpdate(deleteCommand);
		stmt.close();
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
		System.out.println("End insertDrinkListToDB");
	}
	private static void insertIngredientListToDB(ArrayList<String> names)
	{
		System.out.println("Begin insertIngredientListToDB");
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
		System.out.println("End insertIngredientListToDB");
	}
	private static void insertMatchingsToDB(ArrayList<DBDrink> drinks) throws SQLException
	{
		System.out.println("beginInsertMatchings");
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
		System.out.println("endInsertMatchings");
	}

	public static ArrayList<Drink> getAllDrinks()
	{
		ArrayList<Drink> allDrinks = new ArrayList<Drink>();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName(CLASS_NAME);
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
			Class.forName(CLASS_NAME);
			c = DriverManager.getConnection("jdbc:sqlite:drinks.db");
			c.setAutoCommit(false);
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
			Class.forName(CLASS_NAME);
			c = DriverManager.getConnection("jdbc:sqlite:drinks.db");
			c.setAutoCommit(false);
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
