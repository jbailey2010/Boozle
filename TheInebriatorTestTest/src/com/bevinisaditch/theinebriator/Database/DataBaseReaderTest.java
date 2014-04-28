package com.bevinisaditch.theinebriator.Database;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;

public class DataBaseReaderTest extends AndroidTestCase {
	
	ArrayList<Drink> allDrinks;

	public void testReadDatabase()
	{
		DrinkDatabaseHandler handler = new DrinkDatabaseHandler(this.getContext());
		//handler.reCreateTables();
		allDrinks = handler.getAllDrinks();
		ArrayList<Ingredient> ingredients;
		Drink sampleDrink;
		ingredients = new ArrayList<Ingredient>();
		sampleDrink = new Drink("American Beauty Shot", Drink.Rating.THUMBSNULL, ingredients, "Mix everything in a cocktail shaker with ice. Strain into a chilled cocktail glass or a highball glass filled with ice.", 0);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		sampleDrink = new Drink("Algonquin Cocktail", Drink.Rating.THUMBSNULL, ingredients, "Combine all ingredients in cocktail shaker. Stir with ice and strain into a cocktail glass or over ice in a rocks glass.", 1);
		assertDrinkFound(sampleDrink);
	}
	
	private void assertDrinkFound(Drink sampleDrink)
	{
		boolean foundDrink = false;
		Drink otherDrink = allDrinks.get(sampleDrink.getId());
		foundDrink = sampleDrink.getName().equals(otherDrink.getName()) && sampleDrink.getInstructions().equals(otherDrink.getInstructions());
		if (!foundDrink)
		{
			//System.out.println("Looked for ID: " + sampleDrink.getId() + "\tName: " + sampleDrink.getName());
			//System.out.println("Found      ID: " + otherDrink.getId() + "\tName: " + otherDrink.getName());
			System.out.println(" Looked for " + sampleDrink);
			System.out.println("\n Found " + otherDrink);
		}
		assertTrue(foundDrink);
	}
	
	public void testGetRelevantDrinksByName() {
		RenamingDelegatingContext context  = new RenamingDelegatingContext(getContext(), "test_");
		DrinkDatabaseHandler handler = new DrinkDatabaseHandler(context);
		Drink drink1 = new Drink("Rum and Coke");
		drink1.setInstructions(" ");
		Drink drink2 = new Drink("Tequila and lemonade");
		drink2.setInstructions(" ");
		//handler.addDrinkWithoutIngredients(drink1);
		//handler.addDrinkWithoutIngredients(drink2);
		
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("billion");
		
		
		ArrayList<Drink> relevantDrinks = handler.getRelevantDrinksByName(terms);
		
		assertEquals(1, relevantDrinks.size());
		
	}
}
