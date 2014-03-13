package com.bevinisaditch.theinebriator.SearchEngine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
import com.bevinisaditch.theinebriator.SearchEngine.BM25Ranker;

public class BM25RankerTest {
	private Drink drink1;
	private Drink drink2;
	private Drink drink3;
	private ArrayList<Drink> drinks = new ArrayList<Drink>();
	private BM25Ranker ranker = new BM25Ranker();

	@Before
	public void setUpDrinkList() throws Exception {
		drink1 = new Drink("rum and coke");
		drink2 = new Drink("vodka and coke");
		drink3 = new Drink("whiskey and coke");
		drink1.addIngredient(new Ingredient("rum", "1", "oz"));
		drink1.addIngredient(new Ingredient("coke", "1", "oz"));
		drink2.addIngredient(new Ingredient("vodka", "1", "oz"));
		drink2.addIngredient(new Ingredient("coke", "1", "oz"));
		drink3.addIngredient(new Ingredient("whiskey", "1", "oz"));
		drink3.addIngredient(new Ingredient("coke", "1", "oz"));
		drinks.clear();
		drinks.add(drink1);
		drinks.add(drink2);
		drinks.add(drink3);
		
	}

	@Test
	public void testRank_DrinkName() {
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("rum");
		
		ArrayList<Drink> sortedDrinks = ranker.rank(terms, drinks);
		
		assertEquals(drinks.get(0), sortedDrinks.get(0));
		
		terms.clear();
		terms.add("whiskey");
		
		sortedDrinks = ranker.rank(terms, drinks);
		
		assertEquals(drinks.get(2), sortedDrinks.get(0));
	}
	
	@Test
	public void testRank_IngredientName() {
		Drink drink4 = new Drink("double rum and coke");
		drink4.addIngredient(new Ingredient("rum", "1", "oz"));
		drink4.addIngredient(new Ingredient("rum", "1", "oz"));
		drinks.add(drink4);
		
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("rum");
		
		ArrayList<Drink> sortedDrinks = ranker.rank(terms, drinks);
		
		assertEquals(drinks.get(3), sortedDrinks.get(0));
		assertEquals(drinks.get(0), sortedDrinks.get(1));
	}
	
	@Test
	public void testParseTerms() {
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("Orange Juice");
		terms.add("Rum and Coke");
		
		ArrayList<String> expectedParse = new ArrayList<String>();
		expectedParse.add("Orange");
		expectedParse.add("Juice");
		expectedParse.add("Rum");
		expectedParse.add("and");
		expectedParse.add("Coke");
		
		assertEquals(expectedParse, ranker.parseTerms(terms));
	}
	
	@Test
	public void testGetAvgLength() {
		int avgLength = ranker.getAvgLengthOfDrinks(drinks);
		int expectedAvgLength = 3;
		assertEquals(expectedAvgLength, avgLength);
	}
	
	@Test
	public void testSortDrinks() {
		HashMap<Drink, Double> unsortedDrinks = new HashMap<Drink, Double>();
		unsortedDrinks.put(drink1, 1.0);
		unsortedDrinks.put(drink2, 2.0);
		unsortedDrinks.put(drink3, 3.0);
		
		ArrayList<Drink> sortedDrinks = ranker.sortDrinks(unsortedDrinks);
		assertEquals(drink1, sortedDrinks.get(2));
		assertEquals(drink2, sortedDrinks.get(1));
		assertEquals(drink3, sortedDrinks.get(0));
		
	}

}
