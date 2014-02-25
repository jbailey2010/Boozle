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
	
	private static List<Drink> drinks = new ArrayList<Drink>();

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
		
		List<String> drinkLinks = ScrapingUtils.getLinks("http://www.goodcocktails.com/recipes/browse_drinks.php?letter=ALL", "a[href^=mixed_drink.php?drinkID=]");
			
		//Running loop to run scraper for individual drinks
		for(int i = 0; i < drinkLinks.size(); i++) {
			String url = "http://www.goodcocktails.com/recipes/" + drinkLinks.get(i);
			scrapeIndividualDrink(url);
			
			if(i==1) 
				break; //just making sure that only two drinks are being scraped for the time being
		}
		
	}
	
	/**
	 * 
	 * @param url
	 * @throws IOException
	 */
	public static void scrapeIndividualDrink(String url) throws IOException {
		
		Document doc = ScrapingUtils.makeConnection(url);
		
		//Obtaining info div
		Elements infoDiv = doc.select("div#drinkRecipe");
		
		//String manipulation to extract drink name from tag
		String drinkName = infoDiv.select("h2").first().toString();		
		drinkName = drinkName.substring(drinkName.indexOf(">") + 1, drinkName.lastIndexOf("<"));
		
//		Elements ingredients = doc.select("div#drinkRecipe").select("ul li");
		
		//Creating a new drink and setting its name (for now)
		Drink drink = new Drink(drinkName);
		
		//Adding it to the list of drinks
		drinks.add(drink);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<Drink> returnScrapedDrinks() {
		return drinks;	
	}
	
}
