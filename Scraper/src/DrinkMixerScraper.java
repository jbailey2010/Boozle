

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class DrinkMixerScraper {
	
	private static final String[] POSSIBLE_UNITS = {"oz", "dash", "tsp", "tbsp", "pony", "jigger", "cup",
        "pt", "qt", "gal", "splash", "dashes", "splashes", "float", "part",
        "tablespoon", "teaspoon", "ponies", "gallon", "quart",
        "ounce", "slice", "scoop"};
	
	public static void main(String[] args) {
			scrapeDrinks();
	}

	/**
	 * Scrapes drinks from drinkoftheweek.com
	 * @throws IOException 
	 */
	public static ArrayList<Drink> scrapeDrinks() {
		// For each drink group (a, b, c, d, ... z)
		int max = 0;
		ArrayList<Drink> dranks = new ArrayList<Drink>();
		for (int i = 0; i < 12128; i++) {
			try
			{
				String url = "http://www.drinksmixer.com/drink" + i + ".html";
				Document doc = ScrapingUtils.makeConnection(url);
				Drink temp = new Drink(ScrapingUtils.handleQueryMulti(doc, url, "span.item").get(0).split(" recipe")[0]);
				temp.setIngredients(convertToIngredients(ScrapingUtils.handleQueryMulti(doc, url, "span.ingredient")));
				temp.setInstructions(ScrapingUtils.handleQueryMulti(doc, url, "div.RecipeDirections").get(0));
				dranks.add(i, temp);
			}
			catch (IOException| ArrayIndexOutOfBoundsException e)
			{
				e.printStackTrace();
			}
		}
		return dranks;
	}
	
	public static ArrayList<Ingredient> convertToIngredients(List<String> ingredients)
	{
		ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
		for(int i = 0; i < ingredients.size(); i++)
		{
			Ingredient temp;
			String curr = ingredients.get(i);
			temp = parseIngredient(curr);
			ings.add(temp);
		}
		return ings;
	}

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
		
		return new Ingredient(line, "", "");
		
	}
}
