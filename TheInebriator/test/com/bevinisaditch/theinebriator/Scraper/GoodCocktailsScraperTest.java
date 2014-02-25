package com.bevinisaditch.theinebriator.Scraper;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.bevinisaditch.theinebriator.Scrapers.GoodCocktailsScraper;

public class GoodCocktailsScraperTest {

	@Test
	public void testScrapedDrinks() throws IOException {
	
		String drinkName1 = "007";
		String drinkName2 = "24 Karat Nightmare";
		
		GoodCocktailsScraper.scrapeDrinks();
		
		assertEquals("007 scraped successfuly", GoodCocktailsScraper.returnScrapedDrinks().get(0).getName(), drinkName1);
		assertEquals("24 Karat Nightmare scraped successfuly", GoodCocktailsScraper.returnScrapedDrinks().get(1).getName(), drinkName2);
	}
}
