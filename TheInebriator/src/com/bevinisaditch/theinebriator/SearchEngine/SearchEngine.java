package com.bevinisaditch.theinebriator.SearchEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.bevinisaditch.theinebriator.Home;
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
		drinkHandler = Home.getHandler(context);
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
	 */
	public void searchByName(final String name) {
		if (ranker == null) {
	    	ArrayList<String> nameList = new ArrayList<String>();
			nameList.add(name);	
			ranker = new BM25Ranker(context, null, nameList, SEARCH_NAME);			    
		} 
		ranker.execute();
	}
	
	
	
	/**
	 * Takes optional and required ingredients and returns a list of sorted drinks
	 * @param optIngredients - optional ingredients
	 * @param reqIngredients - required ingredients
	 */
	public void searchByIngredient(final ArrayList<String> optIngredients, final ArrayList<String> reqIngredients) {
		if (ranker == null) {
			ranker = new BM25Ranker(context, optIngredients, reqIngredients, 
	        		SEARCH_INGREDIENT);			    
		} 
        ranker.execute();
	}

	
}
