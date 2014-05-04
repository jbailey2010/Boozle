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
		sampleDrink = new Drink("Razberi Sunsplash", Drink.Rating.THUMBSNULL, ingredients, "Pour liquors into a hurricane glass. Add juice and grenadine, and shake well. Garnish with a flag, and serve.", 1260);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		sampleDrink = new Drink("The Real Windex", Drink.Rating.THUMBSNULL, ingredients, "Shake vodka and liqueur with ice, and strain into a chilled cocktail glass. Garnish with orange peel, and serve.", 1261);
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
		terms.add("coco");
		
		
		ArrayList<Drink> relevantDrinks = handler.getRelevantDrinksByName(terms);
		
		assertEquals(3, relevantDrinks.size());
		
	}
}
