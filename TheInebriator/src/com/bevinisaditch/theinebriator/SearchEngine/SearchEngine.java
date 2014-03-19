package com.bevinisaditch.theinebriator.SearchEngine;

import java.util.ArrayList;

import com.bevinisaditch.theinebriator.ClassFiles.DataBaseReader;
import com.bevinisaditch.theinebriator.ClassFiles.Drink;

public class SearchEngine {
	Ranker ranker;
	
	public SearchEngine() {
		ranker = new BM25Ranker();
	}
	
	public ArrayList<Drink> searchByName(String name) {
		
		ArrayList<String> terms = new ArrayList<String>();
		terms.add(name);
		
		//TODO: Fix this
		ArrayList<Drink> relevantDrinks = DataBaseReader.getAllDrinks();
		
		ArrayList<Drink> sortedDrinks = ranker.rank(terms, relevantDrinks);
		
		return sortedDrinks;
		
	}
	
	public ArrayList<Drink> searchByIngredient(ArrayList<String> ingredientNames) {
		
		//TODO: Fix this
		ArrayList<Drink> relevantDrinks = DataBaseReader.getAllDrinks();
				
		ArrayList<Drink> sortedDrinks = ranker.rank(ingredientNames, relevantDrinks);
		
		return sortedDrinks;
	}

}
