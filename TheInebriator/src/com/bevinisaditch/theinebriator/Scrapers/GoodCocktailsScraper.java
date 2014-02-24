package com.bevinisaditch.theinebriator.Scrapers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bevinisaditch.theinebriator.Utils.ScrapingUtils;

public class GoodCocktailsScraper {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		scrapeDrinks();

	}

	public static void scrapeDrinks() throws IOException {
		List<String> links = new ArrayList<String>();
		
		/*need to decide what param to give here*/
		links = ScrapingUtils.handleQuery("http://www.goodcocktails.com/recipes/browse_drinks.php?letter=ALL", "");
		
		
		
	}
	
}
