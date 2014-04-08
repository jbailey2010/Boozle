package com.bevinisaditch.theinebriator.ClassFiles;

import android.test.AndroidTestCase;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;

public class DrinkTest extends AndroidTestCase {

	public void testConstructor()
	{
		Drink drank = new Drink("drank");
		assertTrue(drank.getName().equals("drank"));
		assertTrue(drank.getIngredients() != null);
		assertTrue(drank.getIngredients().isEmpty());
		assertTrue(!drank.isFavorited());
		assertTrue(drank.getRating() == Drink.Rating.THUMBSNULL);
	}
	
	public void testRating()
	{
		Drink drank = new Drink("sample");
		assertTrue(drank.getRating() == Drink.Rating.THUMBSNULL);
		drank.setRating(Drink.Rating.THUMBSDOWN);
		assertTrue(drank.getRating() == Drink.Rating.THUMBSDOWN);
		drank.setRating(Drink.Rating.THUMBSUP);
		assertTrue(drank.getRating() == Drink.Rating.THUMBSUP);
	}
	
	public void testFavorite()
	{
		Drink drank = new Drink("sample");
		assertFalse(drank.isFavorited());
		drank.favorite();
		assertTrue(drank.isFavorited());
		drank.unfavorite();
		assertFalse(drank.isFavorited());
	}
	
	public void testAddIngredient()
	{
		Drink drank = new Drink("sample");
		assertTrue(drank.getIngredients() != null);
		assertTrue(drank.getIngredients().isEmpty());
		Ingredient sampleIngredient = new Ingredient("sampleI", "3/4", "cup");
		drank.addIngredient(sampleIngredient);
		assertTrue(drank.getIngredients().contains(sampleIngredient));
		assertTrue(drank.getIngredients().size() == 1);
	}
	
}
