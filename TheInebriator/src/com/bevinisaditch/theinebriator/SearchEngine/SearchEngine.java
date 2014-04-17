package com.bevinisaditch.theinebriator.SearchEngine;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Context;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;

public class SearchEngine {
	Context context;
	DrinkDatabaseHandler drinkHandler;
	BM25Ranker ranker = null;
	public static final Integer SEARCH_NAME = 0;
	public static final Integer SEARCH_INGREDIENT = 1;
	
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
		
		ArrayList<String> terms = new ArrayList<String>();
		terms.add(name);
		
		//TODO: Fix this
		ArrayList<Drink> relevantDrinks = drinkHandler.getAllDrinks();
		
		if (ranker == null) {
			ranker = new BM25Ranker(context, terms, relevantDrinks, SEARCH_NAME);
		} 
		this.ranker.execute();
		
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
		
		//TODO: Fix this
		ArrayList<Drink> relevantDrinks = drinkHandler.getAllDrinks();
		
		ArrayList<String> searchTerms = new ArrayList<String>();
		searchTerms.addAll(optIngredients);
		searchTerms.addAll(reqIngredients);
		
		if (ranker == null) {
			ranker = new BM25Ranker(context, searchTerms, relevantDrinks, SEARCH_INGREDIENT);
		}
		ranker.execute();
		
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
