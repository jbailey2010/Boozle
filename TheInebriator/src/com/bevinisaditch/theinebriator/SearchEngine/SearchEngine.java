package com.bevinisaditch.theinebriator.SearchEngine;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Context;

import com.bevinisaditch.theinebriator.ClassFiles.DataBaseReader;
import com.bevinisaditch.theinebriator.ClassFiles.Drink;

public class SearchEngine {
	Context context;
	
	public SearchEngine(Context context) {
		this.context = context;
	}
	
	public ArrayList<Drink> searchByName(String name) {
		
		ArrayList<String> terms = new ArrayList<String>();
		terms.add(name);
		
		//TODO: Fix this
		ArrayList<Drink> relevantDrinks = DataBaseReader.getAllDrinks();
		
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
		ArrayList<Drink> relevantDrinks = DataBaseReader.getAllDrinks();
				
		BM25Ranker ranker = new BM25Ranker(context, optIngredients, relevantDrinks);
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
