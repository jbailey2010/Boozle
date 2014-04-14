package com.bevinisaditch.theinebriator.SearchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.bevinisaditch.theinebriator.Home;
import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
import com.bevinisaditch.theinebriator.ClassFiles.TermFrequency;
import com.bevinisaditch.theinebriator.SearchEngine.BM25Ranker;

public class BM25RankerTest extends AndroidTestCase {
	private Drink drink1;
	private Drink drink2;
	private Drink drink3;
	private ArrayList<Drink> drinks = new ArrayList<Drink>();
	private BM25Ranker ranker;
	private ArrayList<String> terms;
	private RenamingDelegatingContext context;

	public void setUp() throws Exception {
		context = new RenamingDelegatingContext(getContext(), "test_");
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
		terms = new ArrayList<String>();
		
	}

	public void testRank_DrinkName() throws InterruptedException, ExecutionException {
		terms.add("rum");
		
		ranker = new BM25Ranker(context, terms, drinks);
		ranker.execute();
		ArrayList<Drink> sortedDrinks = ranker.get();
		
		assertEquals(drinks.get(0), sortedDrinks.get(0));
		
		terms.clear();
		terms.add("whiskey");
		
		sortedDrinks = ranker.rank(terms, drinks);
		
		assertEquals(drinks.get(2), sortedDrinks.get(0));
	}
	
	public void testRank_IngredientName() throws InterruptedException, ExecutionException {
		Drink drink4 = new Drink("double rum and coke");
		drink4.addIngredient(new Ingredient("rum", "1", "oz"));
		drink4.addIngredient(new Ingredient("rum", "1", "oz"));
		drinks.add(drink4);
		
		terms.add("rum");
		
		ranker = new BM25Ranker(context, terms, drinks);
		ranker.execute();
		ArrayList<Drink> sortedDrinks = ranker.get();
		
		assertEquals(drinks.get(3), sortedDrinks.get(0));
		assertEquals(drinks.get(0), sortedDrinks.get(1));
	}
	
	public void testParseTerms() {
		terms.add("Orange Juice");
		terms.add("Rum and Coke");
		
		ArrayList<String> expectedParse = new ArrayList<String>();
		expectedParse.add("Orange");
		expectedParse.add("Juice");
		expectedParse.add("Rum");
		expectedParse.add("and");
		expectedParse.add("Coke");
		
		ranker = new BM25Ranker(context, terms, drinks);
		
		assertEquals(expectedParse, ranker.parseTerms(terms));
	}
	
	public void testGetAvgLength() {
		ranker = new BM25Ranker(context, null, drinks);
		int avgLength = ranker.getAvgLengthOfDrinks(drinks);
		int expectedAvgLength = 3;
		assertEquals(expectedAvgLength, avgLength);
	}
	
	public void testSortDrinks() {
		HashMap<Drink, Double> unsortedDrinks = new HashMap<Drink, Double>();
		unsortedDrinks.put(drink1, 1.0);
		unsortedDrinks.put(drink2, 2.0);
		unsortedDrinks.put(drink3, 3.0);
		
		ranker = new BM25Ranker(context, null, drinks);
		ArrayList<Drink> sortedDrinks = ranker.sortDrinks(unsortedDrinks);
		assertEquals(drink1, sortedDrinks.get(2));
		assertEquals(drink2, sortedDrinks.get(1));
		assertEquals(drink3, sortedDrinks.get(0));
		
	}
	
	public void testTermFrequency() {
		drinks.clear();
		drinks.add(new Drink("test1"));
		drinks.add(new Drink("test2"));
		drinks.add(new Drink("test3"));
		
		terms.clear();
		terms.add("test1");
		terms.add("test2");
		terms.add("test3");
		
		ranker = new BM25Ranker(context, terms, drinks);
		
		TermFrequency termFreq1 = new TermFrequency("test1", .5f);
		TermFrequency termFreq2 = new TermFrequency("test2", .375f);
		TermFrequency termFreq3 = new TermFrequency("test3", .125f);
		ranker.handler.addTermFreq(termFreq1);
		ranker.handler.addTermFreq(termFreq2);
		ranker.handler.addTermFreq(termFreq3);
		
		ArrayList<Drink> sortedDrinks = ranker.rank(terms, drinks);
		
		assertEquals(drinks.get(2), sortedDrinks.get(0));
		assertEquals(drinks.get(1), sortedDrinks.get(1));
		assertEquals(drinks.get(0), sortedDrinks.get(2));
		
	}

}
