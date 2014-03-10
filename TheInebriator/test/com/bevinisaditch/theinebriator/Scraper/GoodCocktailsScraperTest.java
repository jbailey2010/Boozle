package com.bevinisaditch.theinebriator.Scraper;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
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

	
	
	@Test
	public void testIngredients() throws IOException{
		GoodCocktailsScraper.scrapeDrinks();
		ArrayList<Ingredient> ing = GoodCocktailsScraper.returnScrapedDrinks().get(5).getIngredients();
		ArrayList<String> ingNames = new ArrayList<String>();
		String[] ingredArr = new String[ingNames.size()];
		for(Ingredient s: ing){
			
			ingNames.add(s.getName());
			
		}
		
		ingredArr = ingNames.toArray(ingredArr);


			assertEquals("Coffee Liqueur",
					ingredArr[0]);
			
			assertEquals("Hazelnut Liqueur", ingredArr[1]);
			assertEquals("Irish Cream", ingredArr[2]);
			
			
			 
	}
	
	@Test
	public void testIngredientsNum() throws IOException{
		GoodCocktailsScraper.scrapeDrinks();
		ArrayList<Ingredient> ing = GoodCocktailsScraper.returnScrapedDrinks().get(1).getIngredients();
		int i = ing.size();
		assertEquals(2, i);
		
			
			
	
	}
			
		}
		
		



