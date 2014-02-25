package com.bevinisaditch.theinebriator.Scrapers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bevinisaditch.theinebriator.Utils.ScrapingUtils;
import com.bevinisaditch.theinebriator.ClassFiles.*;

public class GoodCocktailsScraper {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		scrapeDrinks();

	}

	public static void scrapeDrinks() throws IOException {
		
		//making connection to the website
		Document doc = ScrapingUtils.makeConnection("http://www.goodcocktails.com/recipes/browse_drinks.php?letter=ALL");
		
		/*need to decide what param to give here*/
		//drinkLinks = ScrapingUtils.handleQuery("http://www.goodcocktails.com/recipes/browse_drinks.php?letter=ALL", "div#browse-drinks");
		
		//ScrapingUtils.printQueryResultSize("http://www.goodcocktails.com/recipes/browse_drinks.php?letter=ALL", "a[href^=mixed_drink.php?drinkID=]", false, null);
		
		//Finding all the drinks
		Elements elements = doc.select("a[href^=mixed_drink.php?drinkID=]");
		List<String> drinkLinks = new ArrayList<String>();
		
		//Extracting the links for each drink
		for(Element element : elements) {
			drinkLinks.add(element.attr("href").toString());
		}
		
		for(int i = 0; i < drinkLinks.size(); i++) {
			String url = "http://www.goodcocktails.com/recipes/" + drinkLinks.get(i);
			scrapeIndividualDrink(url);
			break; //just making sure that only one drink is being scraped for the time being
		}
		
	}
	
	public static void scrapeIndividualDrink(String url) throws IOException {
		
		Document doc = ScrapingUtils.makeConnection(url);
		
		//Obtaining Drink Name
		Element drinkName = doc.select("div#drinkRecipe h2").first();
		
		//String manipulation to extract drink name from tag
		String name = drinkName.toString();		
		name = name.substring(name.indexOf(">") + 1, name.lastIndexOf("<"));
		
		Drink drink = new Drink(name);
		
		return;
	}
	
}
