package com.bevinisaditch.theinebriator.Scraper;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
import com.bevinisaditch.theinebriator.Scrapers.DrinkOfTheWeekScraper;

public class DrinkOfTheWeekScraperTest {
	static Drink armSour;
	
	@BeforeClass
	public static void setUpTestDrinks() {
		armSour = new Drink("Amaretto Sour");
		Ingredient ing1 = new Ingredient("1 oz amaretto bitters", null, null);
		Ingredient ing2 = new Ingredient("2 oz lemon juice or sweet & sour", null, null);
		Ingredient ing3 = new Ingredient("Orange slice", null, null);
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(ing1);
		ingredients.add(ing2);
		ingredients.add(ing3);
		armSour.setIngredients(ingredients);
		armSour.setInstructions("Combine all the ingredients in a shaker filled with ice, and strain into a chilled sour glass. Garnish with the orange slice.");
		
	}

	@Test
	public void testScrapeIndividualDrink() throws IOException {
		Drink armarettoSour = DrinkOfTheWeekScraper.scrapeIndividualDrink("http://www.drinkoftheweek.com/drink_recipes/amaretto-sour/#axzz2tpUGWLYA");
		assertEquals(armarettoSour, armSour);
	}
	
	@Test
	public void testScrapeIngredient()
	{
		String line = "12 oz thing";
		Ingredient result = DrinkOfTheWeekScraper.parseIngredient(line);
		assertTrue(result.equals(new Ingredient("thing", "12", "oz")));
		result = DrinkOfTheWeekScraper.parseIngredient("12 oz. thing");
		assertTrue(result.equals(new Ingredient("thing", "12", "oz.")));
		result = DrinkOfTheWeekScraper.parseIngredient("12 ounce thing");
		assertTrue(result.equals(new Ingredient("thing", "12", "ounce")));
		result = DrinkOfTheWeekScraper.parseIngredient("12 ounces of thing");
		assertTrue(result.equals(new Ingredient("of thing", "12", "ounces")));
		result = DrinkOfTheWeekScraper.parseIngredient("12 oz");
		assertTrue(result.equals(new Ingredient("", "12", "oz")));
		result = DrinkOfTheWeekScraper.parseIngredient("12 oz.");
		assertTrue(result.equals(new Ingredient("", "12", "oz.")));
		result = DrinkOfTheWeekScraper.parseIngredient("52 thimbles wine");
		assertTrue(result.equals(new Ingredient("", "", "52 thimbles wine")));
		result = DrinkOfTheWeekScraper.parseIngredient("oz thing");
		assertTrue(result.equals(new Ingredient("thing", "", "oz")));
		
	}

}
