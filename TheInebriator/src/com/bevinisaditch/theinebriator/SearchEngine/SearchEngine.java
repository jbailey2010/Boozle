package com.bevinisaditch.theinebriator.SearchEngine;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Context;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;

public class SearchEngine {
	Context context;
	DrinkDatabaseHandler drinkHandler;
	
	public SearchEngine(Context context) {
		this.context = context;
		drinkHandler = new DrinkDatabaseHandler(context);
	}
	
	public ArrayList<Drink> searchByName(String name) {
		
		ArrayList<String> terms = new ArrayList<String>();
		terms.add(name);
		
		//TODO: Fix this
		ArrayList<Drink> relevantDrinks = drinkHandler.getAllDrinks();
		
		BM25Ranker ranker = new BM25Ranker(context, terms, relevantDrinks);
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
	
	public ArrayList<Drink> searchByIngredient(ArrayList<String> optIngredients, ArrayList<String> reqIngredients) {
		
		//TODO: Fix this
		ArrayList<Drink> relevantDrinks = drinkHandler.getAllDrinks();
		
		ArrayList<String> searchTerms = new ArrayList<String>();
		searchTerms.addAll(optIngredients);
		searchTerms.addAll(reqIngredients);
				
		BM25Ranker ranker = new BM25Ranker(context, searchTerms, relevantDrinks);
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
