

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 * Scraper for DrinkOfTheWeek.com
 * 
 * @author michael
 *
 */
public class DrinkOfTheWeekScraper {

	private static final String[] POSSIBLE_UNITS = {"oz", "dash", "tsp", "tbsp", "pony", "jigger", "cup",
	         "pt", "qt", "gal", "splash", "dashes", "splashes", "float", "part",
	         "tablespoon", "teaspoon", "ponies", "gallon", "quart",
	         "ounce", "slice", "scoop"};
	
	
	public static void main(String[] args) {
		ArrayList<Drink> drinks = scrapeDrinks();
		System.out.println(drinks.size());

	}
	
	/**
	 * Scrapes drinks from drinkoftheweek.com
	 */
	public static ArrayList<Drink> scrapeDrinks() {
		try {
			ArrayList<Drink> drinksList = new ArrayList<Drink>();
			
			//Connect to the website
			Document mainWebsite = connectToWebsite("http://www.drinkoftheweek.com/list-drinks-a-z/");
			
			//Get drinks in the 'drinkgroup' html elements
			Elements all_drinks = mainWebsite.getElementsByClass("drinkgroup");
			
			//For each drink group (a, b, c, d, ... z)
			for (Element drink_group: all_drinks) {
				
				//Get the href tags
				Elements urls = drink_group.getElementsByAttribute("href");
				
				//Get URL from href tag
				for (Element urlTag : urls) {
					String url = urlTag.attr("href");
					drinksList.add(scrapeIndividualDrink(url));
				}
			}
			
			return drinksList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Parses an individual drink page and returns a drink
	 * @param url - URL of the drink
	 * @return Drink object representing the parsed data
	 * @throws IOException - Thrown if cannot connect to web page
	 */
	public static Drink scrapeIndividualDrink(String url) throws IOException {
		System.out.println("Connecting to: " + url);
		Document drinkPage = connectToWebsite(url);
		
		String title = drinkPage.getElementsByClass("pagetitle").first().html();
		System.out.println(title);
		Drink drink = new Drink(title);
		
		Elements ingredients = drinkPage.select("ul.ingredients li");
		ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>(); 
		
		
		
		for (Element ingredient : ingredients) {
			System.out.println(ingredient.text());
			Ingredient ingredientToAdd = new Ingredient(ingredient.text(), null, null);
			ingredientList.add(ingredientToAdd);
			
		}
		
		drink.setIngredients(ingredientList);
		
		String instructions = drinkPage.getElementsByClass("entry").select("div").get(3).select("p").text();
		System.out.println(instructions);
		
		drink.setInstructions(instructions);
		
		
		return drink;
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
		
		return new Ingredient("", "", line);
		
	}
	
	/**
	 * Helper function to connect to a web page
	 * 
	 * @param url - URL of web page to connect to
	 * @return JSoup document that can be searched through
	 * @throws IOException - Thrown if you can't connect
	 */
	private static Document connectToWebsite(String url) throws IOException {
		Document doc = Jsoup.connect(url)
				.data("query", "Java")
				.userAgent("Mozilla")
				.cookie("auth", "token")
				.timeout(0)
				.post();
		
		return doc;
	}
	

}
