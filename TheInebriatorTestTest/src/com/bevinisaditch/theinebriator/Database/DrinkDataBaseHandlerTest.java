package com.bevinisaditch.theinebriator.Database;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;

/**
 * Tests functions related to the DrinkDatabaseHandler
 *
 * Tests getRelevantDrinks and getAllDrinks
 *
 */
public class DrinkDataBaseHandlerTest extends AndroidTestCase {
	DrinkDatabaseHandler handler;
	ArrayList<Drink> allDrinks;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context  = new RenamingDelegatingContext(getContext(), "test_");
		handler = new DrinkDatabaseHandler(context, true);
	}

	//TODO: Fix this test to not populate DB every time. Ex: use the handler defined in setUp() method
	public void testReadDatabase()
	{
		DrinkDatabaseHandler handler = new DrinkDatabaseHandler(this.getContext());
		allDrinks = handler.getAllDrinks();
		ArrayList<Ingredient> ingredients;
		Drink sampleDrink;
		ingredients = new ArrayList<Ingredient>();
		sampleDrink = new Drink("Hawaiian Island Surfer", Drink.Rating.THUMBSNULL, ingredients, "Blend briefly with half a glassful of crushed ice in a wine goblet. Garnish with fruit, and serve with straws.", 1261l);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		sampleDrink = new Drink("Hot Rum Chocolate", Drink.Rating.THUMBSNULL, ingredients, "Combine rum and cocoa in an irish coffee cup, and sweeten to taste. Top with cream, sprinkle with grated chocolate, and serve.", 1262l);
		assertDrinkFound(sampleDrink);
	}
	
	private void assertDrinkFound(Drink sampleDrink)
	{
		boolean foundDrink = false;
		Drink otherDrink = allDrinks.get(sampleDrink.getId().intValue());
		foundDrink = sampleDrink.getName().equals(otherDrink.getName()) && sampleDrink.getInstructions().equals(otherDrink.getInstructions());
		assertEquals(sampleDrink.getName(), otherDrink.getName());
		assertEquals(sampleDrink.getInstructions(), otherDrink.getInstructions());
		if (!foundDrink)
		{
			//System.out.println("Looked for ID: " + sampleDrink.getId() + "\tName: " + sampleDrink.getName());
			//System.out.println("Found      ID: " + otherDrink.getId() + "\tName: " + otherDrink.getName());
			System.out.println(" Looked for " + sampleDrink);
			System.out.println("\n Found " + otherDrink);
		}
		assertTrue(foundDrink);
	}
	
	//TODO: Fix this test to not populate the entire DB every time
	public void testGetRelevantDrinksByName() {
		DrinkDatabaseHandler handler = new DrinkDatabaseHandler(this.getContext());
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
	
	public void testAddDrink() {
		Ingredient ing1 = new Ingredient("rum", "1", "2");
		Ingredient ing2 = new Ingredient("coke", "3", "4");
		Ingredient ing3 = new Ingredient("lemonade", "5", "6");
		
		Drink drink1 = new Drink("rum and coke");
		drink1.setInstructions(" ");
		drink1.addIngredient(ing1);
		drink1.addIngredient(ing2);
		
		Drink drink2 = new Drink("rum");
		drink2.setInstructions(" ");
		drink2.addIngredient(ing3);
		
		
		assertEquals(0, handler.getAllDrinks().size());
		
		long drinkID = handler.addDrink(drink1);
		
		assertTrue(drinkID != -1);
		
		assertEquals(1, handler.getAllDrinks().size());
		assertEquals(drink1, handler.getAllDrinks().get(0));
		assertEquals(drink1.getIngredients(), handler.getAllDrinks().get(0).getIngredients());
		
		handler.addDrink(drink2);
		
		assertTrue(drinkID != -1);
		
		assertEquals(2, handler.getAllDrinks().size());
		assertEquals(drink2, handler.getAllDrinks().get(1));
		assertEquals(drink2.getIngredients(), handler.getAllDrinks().get(1).getIngredients());
	}
	
	public void testGetIngredientsByDrinkID() {
		Ingredient ing1 = new Ingredient("rum", "1", "2");
		Ingredient ing2 = new Ingredient("coke", "3", "4");
		Ingredient ing3 = new Ingredient("lemonade", "5", "6");
		
		Drink drink1 = new Drink("rum and coke");
		drink1.setInstructions(" ");
		drink1.addIngredient(ing1);
		drink1.addIngredient(ing2);
		
		Drink drink2 = new Drink("lemonade");
		drink2.setInstructions(" ");
		drink2.addIngredient(ing3);
		
		long id1 = handler.addDrink(drink1);
		long id2 = handler.addDrink(drink2);
		
		ArrayList<Ingredient> ings1 = handler.getIngredientsForDrinkID(id1);
		ArrayList<Ingredient> ings2 = handler.getIngredientsForDrinkID(id2);
		
		System.out.println(ings1.get(0).toString());
		
		assertEquals(2, ings1.size());
		assertEquals(1, ings2.size());
		assertEquals(ing1, ings1.get(0));
		assertEquals(ing2, ings1.get(1));
		assertEquals(ing3, ings2.get(0));
		
		
	}
	
	public void testGetRelevantDrinksByIngredient() {
		Ingredient ing1 = new Ingredient("Rum", null, null);
		Ingredient ing2 = new Ingredient("coke", null, null);
		Ingredient ing3 = new Ingredient("tequila", null, null);
		Ingredient ing4 = new Ingredient("lemonade", null, null);
		Ingredient ing5 = new Ingredient("chocolate rum", null, null);
		Ingredient ing6 = new Ingredient("RUM chocolate", null, null);
		Ingredient ing7 = new Ingredient("chocolate rum chocolate", null, null);
		Ingredient ing8 = new Ingredient("RUM", null, null);
		
		Drink drink1 = new Drink("Rum and Coke");
		drink1.setInstructions(" ");
		drink1.addIngredient(ing1);
		drink1.addIngredient(ing2);
		
		Drink drink2 = new Drink("Tequila and lemonade");
		drink2.setInstructions(" ");
		drink2.addIngredient(ing3);
		drink2.addIngredient(ing4);
		
		Drink drink3 = new Drink("chocolate rum");
		drink3.setInstructions(" ");
		drink3.addIngredient(ing5);
		
		Drink drink4 = new Drink("RUM chocolate");
		drink4.setInstructions(" ");
		drink4.addIngredient(ing6);
		
		Drink drink5 = new Drink("chocolate rum chocolate");
		drink5.setInstructions(" ");
		drink5.addIngredient(ing7);
		
		Drink drink6 = new Drink("RUM");
		drink6.setInstructions(" ");
		drink6.addIngredient(ing8);
		
		handler.addDrink(drink1);
		handler.addDrink(drink2);
		handler.addDrink(drink3);
		handler.addDrink(drink4);
		handler.addDrink(drink5);
		handler.addDrink(drink6);
		
		ArrayList<String> reqIngs = new ArrayList<String>();
		reqIngs.add(ing1.getName());
		ArrayList<Drink> drinks = handler.getRelevantDrinksByIngredient(reqIngs);
		assertEquals(5, drinks.size());
		assertEquals(drink1, drinks.get(0));
		assertEquals(drink3, drinks.get(1));
		assertEquals(drink4, drinks.get(2));
		assertEquals(drink5, drinks.get(3));
		assertEquals(drink6, drinks.get(4));
		
		
		reqIngs.clear();
		reqIngs.add(ing2.getName());
		drinks = handler.getRelevantDrinksByIngredient(reqIngs);
		assertEquals(1, drinks.size());
		assertEquals(drink1, drinks.get(0));
		
		reqIngs.clear();
		reqIngs.add(ing3.getName());
		drinks = handler.getRelevantDrinksByIngredient(reqIngs);
		assertEquals(1, drinks.size());
		assertEquals(drink2, drinks.get(0));
		
		reqIngs.clear();
		reqIngs.add(ing4.getName());
		drinks = handler.getRelevantDrinksByIngredient(reqIngs);
		assertEquals(1, drinks.size());
		assertEquals(drink2, drinks.get(0));
		
		reqIngs.clear();
		reqIngs.add(ing7.getName());
		drinks = handler.getRelevantDrinksByIngredient(reqIngs);
		assertEquals(1, drinks.size());
		assertEquals(drink5, drinks.get(0));
		
		
	}
}
