package com.bevinisaditch.theinebriator.SearchEngine;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;

public abstract class Ranker extends AsyncTask<Void, Void, ArrayList<Drink>> {
	
	/**
	 * Takes in a list of drinks and ranks them, returning a sorted
	 * list with the highest scoring drinks first.
	 * @param terms -Terms of the search query
	 * @param drinks - List of drinks to be ranked/sorted
	 * @return - Sorted list of drinks
	 */
	public abstract ArrayList<Drink> rank(
			ArrayList<String> terms, 
			ArrayList<Drink> drinks
			);

}
