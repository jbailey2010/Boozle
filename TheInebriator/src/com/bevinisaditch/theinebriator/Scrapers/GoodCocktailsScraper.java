package com.bevinisaditch.theinebriator.Scrapers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bevinisaditch.theinebriator.Utils.ScrapingUtils;

public class GoodCocktailsScraper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void scrapeDrinks() throws IOException {
		List<String> links = new ArrayList<String>();
		links = ScrapingUtils.handleQuery("http://www.goodcocktails.com/recipes/browse_drinks.php?letter=ALL", "");
		
	}
	
}
