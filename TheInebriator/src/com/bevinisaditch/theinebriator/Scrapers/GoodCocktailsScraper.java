package com.bevinisaditch.theinebriator.Scrapers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bevinisaditch.theinebriator.Utils.ScrapingUtils;

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
		}
		
	}
	
	public static void scrapeIndividualDrunk(String url) {
		
	}
	
}
