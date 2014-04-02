package com.bevinisaditch.theinebriator.Scraper;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import android.test.AndroidTestCase;

import com.bevinisaditch.theinebriator.ClassFiles.DataBaseReader;
import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;

public class DataBaseReaderTest {
	
	ArrayList<Drink> allDrinks;

	@Test
	public void testReadDatabase()
	{
		DrinkDatabaseHandler handler = new DrinkDatabaseHandler(null);
		allDrinks = handler.getAllDrinks();
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("Brandy or Rum (Light)", "1 1/2", "oz."));
		ingredients.add(new Ingredient("Cream / Half & Half", "1/2", "oz."));
		ingredients.add(new Ingredient("Honey", "1", "Tbsp."));
		Drink sampleDrink = new Drink("Zoom", Drink.Rating.THUMBSNULL, ingredients, "Pour all ingredients into a shaker with ice. Shake and strain into a chilled martini glass.", 0);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("Vodka", "1", "oz."));
		ingredients.add(new Ingredient("Raspberry Liqueur", "1", "oz."));
		ingredients.add(new Ingredient("Lemon-Lime Soda or Club Soda / Carbonated Water", "", ""));
		sampleDrink = new Drink("Zipperhead", Drink.Rating.THUMBSNULL, ingredients, "Pour vodka and raspberry liqueur into a glass with ice. Fill it with lemon-lime soda or soda water. Do not stir or shake.", 0);
		assertDrinkFound(sampleDrink);
		ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("Rum (Light)", "2", "oz."));
		ingredients.add(new Ingredient("Strawberry Liqueur (Optional)", "1/2", "oz."));
		ingredients.add(new Ingredient("Sweet & Sour Mix", "1", "oz."));
		ingredients.add(new Ingredient("", "3", "Strawberries"));
		sampleDrink = new Drink("Strawberry Daiquiri", Drink.Rating.THUMBSNULL, ingredients, "Put all ingredients into a blender with ice. Blend until smooth and pour into a glass. If it is too thick add more sweet and sour.", 0);
		assertDrinkFound(sampleDrink);
	}
	
	private void assertDrinkFound(Drink sampleDrink)
	{
		boolean foundDrink = false;
		for (Drink currDrink : allDrinks)
		{
			if (currDrink.equals(sampleDrink))
			{
				foundDrink = true;
			}
		}
		assertTrue(foundDrink);
	}
}
