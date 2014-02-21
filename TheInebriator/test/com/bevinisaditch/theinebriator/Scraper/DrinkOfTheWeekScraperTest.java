package com.bevinisaditch.theinebriator.Scraper;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.Scrapers.DrinkOfTheWeekScraper;

public class DrinkOfTheWeekScraperTest {
	static Drink armSour;
	
	@BeforeClass
	public static void setUpTestDrinks() {
		armSour = new Drink("Amaretto Sour");
		
	}

	@Test
	public void testScrapeIndividualDrink() throws IOException {
		Drink armarettoSour = DrinkOfTheWeekScraper.scrapeIndividualDrink("http://www.drinkoftheweek.com/drink_recipes/amaretto-sour/#axzz2tpUGWLYA");
		
		assertEquals(armarettoSour, armSour);
	}

}
