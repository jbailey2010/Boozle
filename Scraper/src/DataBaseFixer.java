import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Map;


public class DataBaseFixer {

	public static void main(String args[]) throws SQLException { 

		ArrayList<Drink> drinks = fixDrinks();
	
	}

	public static ArrayList<Ingredient> parse(){
		return null;
	}
	
	/**
	 * Fix drinks goes through the original database and cleans it up
	 *  getting rid of duplicates, drinks without names, drinks with
	 *  very odd ingredients, unicode encoding issues, and fixes the
	 *  id's
	 * @return
	 */
	public static ArrayList<Drink> fixDrinks()
	{
		int count = 0;
		//get all drinks and fix the ingredients using the fixingredients function
		ArrayList<Drink> drinksOrig = DataBaseReader.getAllDrinks();
		ArrayList<Drink> drinks = fixIngredients(drinksOrig);
		/*
		for (Drink currDrink : drinks)
		{
			if (currDrink.getName().equals("Claret Punch"))
			{
				System.out.println("CLARET PUNCH WAS INDEED FOUND " + currDrink.getId());
			}
			if (currDrink.getName().equals("Hillary Wallbanger"))
			{
				System.out.println("HILLARY WALLBANGER INDEED FOUND " + currDrink.getId());
			}
		}*/
		System.out.println(drinks.size());
		// loops through all drinks and gets rid of 3/4 of them for the sake of runtime
		// and cleans up copies
		for(int i = 0; i < drinks.size(); i++) {
			count++;
			//Remove every fifth ingredient
			if(count % 4 != 0) {
				drinks.remove(i);
				i--;
				continue;
			}
			
			//Makes sure there are ingredients and there aren't duplicates
			if (drinks.get(i).getIngredients().size() == 0 || 
					drinks.get(i).getName().contains("#2") || 
					drinks.get(i).getName().contains("#3") ||
					drinks.get(i).getName().trim().equals("")) {
				drinks.remove(i);
				i--;
				continue;
			}
			
			//Fixes the unicode issue
			for (Ingredient ing : drinks.get(i).getIngredients())
			{
				ing.setName(Normalizer.normalize(ing.getName(), Normalizer.Form.NFKC).replaceAll("[^\\p{ASCII}]",  ""));
				ing.setQuantity(Normalizer.normalize(ing.getQuantity(), Normalizer.Form.NFKC).replaceAll("[^\\p{ASCII}]",  ""));
				ing.setUnits(Normalizer.normalize(ing.getUnits(), Normalizer.Form.NFKC).replaceAll("[^\\p{ASCII}]",  ""));
			}
		}
		
		// set the drink id correctly
		for (int i = 0; i < drinks.size(); i++)
		{
			drinks.get(i).setId(i);
		}
		System.out.println("Done");
		return drinks;
	}
	
	/**
	 * Fixes ingredients by checking quantity, unit, and name
	 *  and throws out drinks that behave erratically
	 * @param drinks
	 * @return
	 */
	public static ArrayList<Drink> fixIngredients(ArrayList<Drink> drinks)
	{
		ArrayList<Drink> trimmedDrinks = new ArrayList<Drink>();
		int numDrinksThrownOut = 0;
		//Loops through all of the drinks
		for (Drink currDrink : drinks)
		{
			boolean trashDrink = false;
			ArrayList<Ingredient> newIngredients = new ArrayList<Ingredient>();
			//Loops through all ingredients in current drink
			for (Ingredient currIngredient : currDrink.getIngredients())
			{			
				String origIng = currIngredient.toString();
				Ingredient newIngredient;
				//Fixes ingredient and parses it properly
				boolean quantityIsNull = (currIngredient.getQuantity() == null || currIngredient.getQuantity().isEmpty() || currIngredient.getQuantity().equals("null"));
				boolean unitsIsNull = (currIngredient.getUnits() == null || currIngredient.getUnits().isEmpty() || currIngredient.getUnits().equals("null"));
				boolean nameIsNull = (currIngredient.getName() == null || currIngredient.getName().isEmpty() || currIngredient.getName().equals("null"));
				if (quantityIsNull && unitsIsNull)
				{
					newIngredient = parseIngredient(currIngredient.getName());
				}
				else if (quantityIsNull && nameIsNull)
				{
					newIngredient = parseIngredient(currIngredient.getUnits());
				}
				else if (unitsIsNull && nameIsNull)
				{
					newIngredient = parseIngredient(currIngredient.getQuantity());
				}
				else
				{
					newIngredient = currIngredient;
				}
				String name = newIngredient.getName();
				//Fixing some unit or drink names. Parsing didn't always work correctly
				// issues like of frozen (contains oz) etc.
				if (name.length() > 3)
				{
					if (name.substring(0,3).equals("of "))
					{
						newIngredient.setName(name.substring(3));
					}
					else if (name.substring(0,3).equals("es "))
					{
						String ing = newIngredient.getQuantity() + " " + newIngredient.getUnits() + newIngredient.getName();
						newIngredient = parseIngredient(ing);
					}
					else if (name.substring(0, 3).equals("en ") || name.substring(0, 3).equals("en,"))
					{
						if (newIngredient.getUnits().equals("oz"))
						{
							System.out.println("Corrected: " + newIngredient);
							String ing = newIngredient.getQuantity() + newIngredient.getUnits() + newIngredient.getName();
							System.out.println("String to parse: " + ing);
							newIngredient = parseIngredient(ing);
							if (newIngredient.getName().substring(0, 3).equals("en "))
							{
								newIngredient.setName(newIngredient.getQuantity() + newIngredient.getUnits() + newIngredient.getName());
								newIngredient.setUnits("");
								newIngredient.setQuantity("");
							}
							System.out.println("To: " + newIngredient);
						}
						else
						{
							System.out.println("en without oz");
						}
					}
				}
				if (name.length() > 2)
				{
					if (name.substring(0,2).equals("d "))
					{
						newIngredient.setName(name.substring(2));
					}
				}
				//Decide if drink is valid and this is where we decide if we
				// want to throw a particular recipe
				if (!quantityIsValid(newIngredient))
				{
					System.out.println("Throwing out " + newIngredient);
					trashDrink = true;
				}
				newIngredients.add(newIngredient);//Extracted
			}
			currDrink.setIngredients(newIngredients);
			if (!trashDrink)
			{
				trimmedDrinks.add(currDrink);
			}
			else
			{
				System.out.println("Drink name thrown out: " + currDrink.getName());
				numDrinksThrownOut++;
			}
		}
		System.out.println("Num drinks thrown out: " + numDrinksThrownOut);
		return trimmedDrinks;
	}
	
	/**
	 * checks if a quantity is valid by making sure it has
	 *  a predetermined set of characters
	 * @param ing
	 * @return
	 */
	private static boolean quantityIsValid(Ingredient ing)
	{
		String validChars = "0123456789/-. aA";
		String qty = ing.getQuantity();
		//Loop through the string and validate it with
		// all the validChars
		for (int i = 0; i < qty.length(); i++)
		{
			char curr = qty.charAt(i);
			boolean match = false;
			for (int j = 0; j < validChars.length(); j++)
			{
				if (curr == validChars.charAt(j))
				{
					match = true;
				}
			}
			if (!match)
			{
				return false;
			}
		}
		return true;
	}
	
	// The possible units that we will allow. Units earlier on the list will
	// be caught before units later in the list
	private static final String[] POSSIBLE_UNITS = {"teaspoon", "scoop", "cup", "part", "package", 
		"shot", "dashes", "dash", "tsp", "tbsp", "pony", "ml", "sprig", "pinch", "inch", "jigger",
		"can ", "bottle", "tb", "drop", "liter", "litre", "twist", "heaping bar spoon", "bar spoon",
		"spoon", "squeeze", "pinch", "stalk", "bag",
        "gal", "splashes", "splash", "float", "pint", "glass",
        "tablespoon", "ponies", "gallon", "quart", "oz",
        "ounce", "slice", "cl ", "whole", "piece", " g ", "lb", "dl ", "pt ", "qt"};
	
	/**
	 * parses every ingredient. takes the units and quantity and separates them
	 *  from the name.
	 * @param line
	 * @return
	 */
	public static Ingredient parseIngredient(String line)
	{
		line = line.toLowerCase();
		String name;
		String qty;
		String units;
		
		//Loop through the possible units and checks string
		// if one of the units are there. if it does
		// then everything before it becomes a quantity and
		// everything after it becomes the name
		for (String possibleUnit : POSSIBLE_UNITS)
		{
			int index = line.indexOf(possibleUnit);
			if (index >= 0)
			{
				qty = line.substring(0, index).trim();
				int unitStrLen = possibleUnit.length();
				if (line.length() > index + unitStrLen)
				{
					char after = line.charAt(index + unitStrLen);
					if (after == '.' || after == 's')
					{
						unitStrLen++;
					}
				}
				units = line.substring(index, index + unitStrLen).trim();
				name = line.substring(index + unitStrLen).trim();
				return new Ingredient(name, qty, units);
			}
		}
		
		return new Ingredient(line, "", "");
		
	}
}
