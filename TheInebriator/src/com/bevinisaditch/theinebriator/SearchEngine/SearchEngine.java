package com.bevinisaditch.theinebriator.SearchEngine;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.util.Log;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;

/**
 * Search engine that should be called by the android search activity (Home.java)
 * 
 * @author michael
 *
 */
public class SearchEngine {
	Context context;
	DrinkDatabaseHandler drinkHandler;
	BM25Ranker ranker = null;
	public static final Integer SEARCH_NAME = 0;
	public static final Integer SEARCH_INGREDIENT = 1;
	
	/**
	 * Constructor
	 * 
	 * @param context - Android activity context
	 */
	public SearchEngine(Context context) {
		this.context = context;
		drinkHandler = new DrinkDatabaseHandler(context);
	}
	
	/**
	 * Used for testing purposes
	 * @param context
	 * @param ranker
	 * @param drinkHandler
	 */
	public SearchEngine(Context context, BM25Ranker ranker, DrinkDatabaseHandler drinkHandler) {
		this.context = context;
		this.ranker = ranker;
		this.drinkHandler = drinkHandler;
	}
	
	/**
	 * Takes a name to search for and returns the sorted drinks
	 * @param name - name of drink you wish to search for
	 * @return - ArrayList<Drink> of sorted drinks
	 */
	public ArrayList<Drink> searchByName(String name) {
		
		Log.d("SearchEngine", "SearchByName is called");
		ArrayList<String> terms = new ArrayList<String>();
		terms.add(name);
		
		
		Log.d("SearchEngine", "Getting drinks...");
		//TODO: Fix this
		
		String[] pterms = terms.get(0).split("\\s+");
		ArrayList<String> parsedTerms = new ArrayList<String>();
		for (String term : pterms) {
			parsedTerms.add(term);
		}
		ArrayList<Drink> relevantDrinks = drinkHandler.getRelevantDrinksByName(parsedTerms);
		//ArrayList<Drink> relevantDrinks = drinkHandler.getAllDrinks();
		Log.d("SearchEngine", "Got drinks...");
		
		
		if (ranker == null) {
			ranker = new BM25Ranker(context, terms, relevantDrinks, SEARCH_NAME);
		} 
		
		Log.d("SearchEngine", "Ranking drinks...");
		this.ranker.execute();
		Log.d("SearchEngine", "Drinks ranked");
		
		ArrayList<Drink> sortedDrinks;
		try {
			sortedDrinks = ranker.get();
		} catch (InterruptedException e) {
			sortedDrinks = null;
			e.printStackTrace();
		} catch (ExecutionException e) {
			sortedDrinks = null;
			e.printStackTrace();
		}
		
		return sortedDrinks;
		
	}
	
	/**
	 * Takes optional and required ingredients and returns a list of sorted drinks
	 * @param optIngredients - optional ingredients
	 * @param reqIngredients - required ingredients
	 * @return sorted drinks
	 */
	public ArrayList<Drink> searchByIngredient(ArrayList<String> optIngredients, ArrayList<String> reqIngredients) {
		Log.d("SearchEngine", "SearchByIngredient called");
		//TODO: Fix this
		
		Log.d("SearchEngine", "Getting drinks...");
		ArrayList<Drink> relevantDrinks = drinkHandler.getRelevantDrinksByIngredient(reqIngredients);
		Log.d("SearchEngine", "Got drinks");
		
		ArrayList<String> searchTerms = new ArrayList<String>();
		searchTerms.addAll(optIngredients);
		searchTerms.addAll(reqIngredients);
		
		if (ranker == null) {
			ranker = new BM25Ranker(context, searchTerms, relevantDrinks, SEARCH_INGREDIENT);
		}
		
		Log.d("SearchEngine", "Ranking drinks...");
		ranker.execute();
		Log.d("SearchEngine", "drinks ranked");
		
		ArrayList<Drink> sortedDrinks;
		try {
			sortedDrinks = ranker.get();
		} catch (InterruptedException e) {
			sortedDrinks = null;
			e.printStackTrace();
		} catch (ExecutionException e) {
			sortedDrinks = null;
			e.printStackTrace();
		}
		
		return sortedDrinks;
	}

}
