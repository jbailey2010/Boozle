package com.bevinisaditch.theinebriator.Database;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import android.test.AndroidTestCase;

import com.bevinisaditch.theinebriator.ClassFiles.DataBaseReader;
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
		ingredients.add(new Ingredient("1 part DeKuyper Pucker sour-apple schnapps", "", ""));
		ingredients.add(new Ingredient("1 part Absolut vodka", "", ""));
		sampleDrink = new Drink("2-Point Play", Drink.Rating.THUMBSNULL, ingredients, "Pour ingredients into a cocktail shaker with ice. Shake and strain into a chilled martini glass. Garnish with an apple wedge.", 0);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("3 oz Cannabis Vodka (or make your own)", "", ""));
		ingredients.add(new Ingredient("Dry vermouth (enough to coat the glass)", "", ""));
		ingredients.add(new Ingredient("One hit super premium sativa or indica", "", ""));
		sampleDrink = new Drink("420 Martini", Drink.Rating.THUMBSNULL, ingredients, "Coat a chilled cocktail glass with the vermouth. Chill vodka in a shaker with ice. Shake well and strain into the cocktail glass. Take the bong hit, hold, and take a sip of the martini. Exhale.", 1);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("1 tsp brown sugar", "", ""));
		ingredients.add(new Ingredient("1 part lemon juice", "", ""));
		ingredients.add(new Ingredient("1 part Bacardi Oro", "", ""));
		ingredients.add(new Ingredient("1 part Bacardi 151", "", ""));
		ingredients.add(new Ingredient("1 part 42 BELOW Passion Fruit", "", ""));
		ingredients.add(new Ingredient("1 part unsweetened pineapple juice", "", ""));
		ingredients.add(new Ingredient("1 part fresh lime juice", "", ""));
		ingredients.add(new Ingredient("1 part passion fruit syrup", "", ""));
		ingredients.add(new Ingredient("1 dash angostura bitters", "", ""));
		sampleDrink = new Drink("42Zombie", Drink.Rating.THUMBSNULL, ingredients, "Dissolve sugar and lemon juice, then add all other ingredients and shake well with crushed ice. Pour entire contents into a collins glass and garnish with lime wedge.", 2);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		sampleDrink = new Drink("4th of July Watermelon Cocktail", Drink.Rating.THUMBSNULL, ingredients, "", 3);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("1 1/2 oz American Gin", "", ""));
		ingredients.add(new Ingredient("3/4 oz Dry vermouth", "", ""));
		ingredients.add(new Ingredient("3/4 oz Blanc vermouth", "", ""));
		ingredients.add(new Ingredient("2 Dashes orange bitters", "", ""));
		sampleDrink = new Drink("50/50 Split", Drink.Rating.THUMBSNULL, ingredients, "In a mixing glass, add spirits and mixers. Fill with ice and stir. Strain into a chilled martini glass and garnish with a lemon twist.", 4);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		sampleDrink = new Drink("A Happy Jewish New Year", Drink.Rating.THUMBSNULL, ingredients, "", 6);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		sampleDrink = new Drink("Aberdeen Flip", Drink.Rating.THUMBSNULL, ingredients, "", 7);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("1 oz anisette", "", ""));
		ingredients.add(new Ingredient("1 oz Herbsaint anise liqueur", "", ""));
		ingredients.add(new Ingredient("Soda water", "", ""));
		sampleDrink = new Drink("Absinthe Frapp&eacute;", Drink.Rating.THUMBSNULL, ingredients, "Pour the anisette and Herbsaint into an old-fashioned glass filled with ice, stir and add soda water.", 8);
		assertDrinkFound(sampleDrink);
	}
	
	private void assertDrinkFound(Drink sampleDrink)
	{
		boolean foundDrink = false;
		Drink otherDrink = allDrinks.get(sampleDrink.getId());
		foundDrink = sampleDrink.equals(otherDrink);
		if (!foundDrink)
		{
			//System.out.println("Looked for ID: " + sampleDrink.getId() + "\tName: " + sampleDrink.getName());
			//System.out.println("Found      ID: " + otherDrink.getId() + "\tName: " + otherDrink.getName());
			System.out.println(" Looked for " + sampleDrink);
			System.out.println("\n Found " + otherDrink);
		}
		assertTrue(foundDrink);
	}
}
