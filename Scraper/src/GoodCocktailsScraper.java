

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoodCocktailsScraper {

	private static ArrayList<Drink> drinks = new ArrayList<Drink>();

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		scrapeDrinks();

	}

	/**
	 * 
	 * @throws IOException
	 */
	public static void scrapeDrinks() throws IOException {

		List<String> drinkLinks = ScrapingUtils
				.getLinks(
						"http://www.goodcocktails.com/recipes/browse_drinks.php?letter=ALL",
						"a[href^=mixed_drink.php?drinkID=]");

		// Running loop to run scraper for individual drinks
		for (int i = 0; i < drinkLinks.size(); i++) {
			String url = "http://www.goodcocktails.com/recipes/"
					+ drinkLinks.get(i);
			scrapeIndividualDrink(url);

			if (i == 30)
				break; // just making sure that only two drinks are being
						// scraped for the time being
		}

		System.out.println("");
	}

	/**
	 * 
	 * @param url
	 * @throws IOException
	 */
	public static void scrapeIndividualDrink(String url) throws IOException {

		Document doc = ScrapingUtils.makeConnection(url);

		// Obtaining info div
		Elements infoDiv = doc.select("div#drinkRecipe");

		// String manipulation to extract drink name from tag
		String drinkName = infoDiv.select("h2").first().toString();
		drinkName = drinkName.substring(drinkName.indexOf(">") + 1,
				drinkName.lastIndexOf("<"));

		//Creating a new drink object and setting its name
		Drink drink = new Drink(drinkName); 

		System.out.println(drinkName); //Debugging
		
		// Extraction of ingredients list
		Elements ingredients = infoDiv.select("ul").first().children();

		for (Element ele : ingredients) {
			String ingredientText = ele.text().toString();
			String[] splitstring = ingredientText.split(" ");
			String name = "", qty = "", unit = "";
			Boolean isUnit = false;
			Boolean isQty = false;

			for (int i = 0; i < splitstring.length; i++) {

				// For Quantity
				if (splitstring[i].charAt(0) >= '0'
						&& splitstring[i].charAt(0) <= '9') {
					if (splitstring[i + 1].charAt(0) >= '0'
							&& splitstring[i + 1].charAt(0) <= '9') {
						qty = splitstring[i] + " " + splitstring[i + 1];
						i++;
					} else
						qty = splitstring[i];
					isQty = true;
					continue;
				}

				// For Unit of Qty
				if (!isUnit && isQty) {
					unit = splitstring[i];
					isUnit = true;
					continue;
				}

				// For Name of Ingredient
				name = name + " " + splitstring[i];

			}

			//Creating new ingredient with extracted values
			Ingredient ingredient = new Ingredient(name.trim(), qty.trim(),
					unit.trim());
			//Adding the new ingredient to the drink
			drink.addIngredient(ingredient);
			
			System.out.println(ingredient.toString()); //Debugging

		}

		// String manipulation to extract instructions from tag
		String instructions = infoDiv.select("p").get(1).toString();
		instructions = instructions.substring(instructions.indexOf(">") + 1,
				instructions.lastIndexOf("<"));

		// Setting the instructions of the drink
		drink.setInstructions(instructions);

		System.out.println(instructions + "\n"); //Debugging
		
		// Adding it to the list of drinks
		drinks.add(drink);

	}

	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public static ArrayList<Drink> returnScrapedDrinks() throws IOException {
		scrapeDrinks();
		return drinks;
	}

}
