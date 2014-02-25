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

		String instructions1 = "Pour the orange vodka into a glass with ice. Fill it with half orange juice and half lemon-lime soda.";
		String instructions2 = "Combine both ingredients into shot glass.";

		GoodCocktailsScraper.scrapeDrinks();

		// Testing names
		assertEquals("007 name scrape unsuccessful", GoodCocktailsScraper
				.returnScrapedDrinks().get(0).getName(), drinkName1);
		assertEquals("24 Karat Nightmare name scrape unsuccessful",
				GoodCocktailsScraper.returnScrapedDrinks().get(1).getName(),
				drinkName2);

		// Testing instructions
		assertEquals("007 instruction scrape unsuccessful",
				GoodCocktailsScraper.returnScrapedDrinks().get(0)
						.getInstructions(), instructions1);
		assertEquals("24 Karat Nightmare instruction scrape unsuccessful",
				GoodCocktailsScraper.returnScrapedDrinks().get(1)
						.getInstructions(), instructions2);

	}

}
