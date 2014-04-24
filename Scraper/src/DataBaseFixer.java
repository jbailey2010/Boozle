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
		return null;}
	
	public static ArrayList<Drink> fixDrinks()
	{
		int count = 0;
		ArrayList<Drink> drinks = DataBaseReader.getAllDrinks();
		System.out.println(drinks.size());
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
					drinks.get(i).getName().contains("#3")) {
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
		
		for (int i = 0; i < drinks.size(); i++)
		{
			drinks.get(i).setId(i);
		}
		System.out.println("Done");
		return drinks;
	}
	
	public static ArrayList<Drink> fixIngredients(ArrayList<Drink> drinks)
	{
		for (Drink currDrink : drinks)
		{
			ArrayList<Ingredient> newIngredients = new ArrayList<Ingredient>();
			for (Ingredient currIngredient : currDrink.getIngredients())
			{
				if ((currIngredient.getQuantity() == null || currIngredient.getQuantity().isEmpty() || currIngredient.getQuantity().equals("null")) && (currIngredient.getUnits() == null || currIngredient.getUnits().isEmpty() || currIngredient.getUnits().equals("null")))
				{
					Ingredient newIngredient = parseIngredient(currIngredient.getName());
					newIngredients.add(newIngredient);
				}
				else
				{
					newIngredients.add(currIngredient);
				}
			}
			currDrink.setIngredients(newIngredients);
		}
		return drinks;
	}
	
	private static final String[] POSSIBLE_UNITS = {"oz", "dash", "tsp", "tbsp", "pony", "jigger", "cup",
        "pt", "qt", "gal", "splash", "dashes", "splashes", "float", "part",
        "tablespoon", "teaspoon", "ponies", "gallon", "quart",
        "ounce", "slice", "scoop"};
	
	public static Ingredient parseIngredient(String line)
	{
		String name;
		String qty;
		String units;
		
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
		
		return new Ingredient("", "", line);
		
	}
}
