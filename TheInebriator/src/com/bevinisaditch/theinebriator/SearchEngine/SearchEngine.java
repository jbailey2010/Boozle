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
		AsyncLoaderName nameFreq = new AsyncLoaderName();
		nameFreq.execute(name, this);
	}
	
	private class AsyncLoaderName extends AsyncTask<Object, Void, BM25Ranker> {
		SearchEngine engine;

		/**
		 * Calls the method to load the drinks
		 */
        @Override
        protected BM25Ranker doInBackground(Object... params) {
        	//TODO: No longer needs to be asynchronous, is the overhead unnecessary?
        	String name = (String)params[0];
        	engine = (SearchEngine)params[1];

    		if (ranker == null) {
    	    	ArrayList<String> nameList = new ArrayList<String>();
    			nameList.add(name);	
    			ranker = new BM25Ranker(context, null, nameList, SEARCH_NAME);			    
    		} 
    		
        	return ranker;
        }

        /**
         * Once it's done, move over to send to home
         */
        @Override
        protected void onPostExecute(BM25Ranker ranker) {
            engine.ranker = ranker;
            ranker.execute();
        }
    }
	
	/**
	 * Takes optional and required ingredients and returns a list of sorted drinks
	 * @param optIngredients - optional ingredients
	 * @param reqIngredients - required ingredients
	 */
	public void searchByIngredient(final ArrayList<String> optIngredients, final ArrayList<String> reqIngredients) {
		
		AsyncLoaderIngr ingrFreq = new AsyncLoaderIngr();
		ingrFreq.execute(optIngredients, this, reqIngredients);
	}
	
	private class AsyncLoaderIngr extends AsyncTask<Object, Void, BM25Ranker> {
		SearchEngine engine;
		public ProgressDialog loginDialog;


		/**
		 * Calls the method to load the drinks
		 */
        @Override
        protected BM25Ranker doInBackground(Object... params) {
        	//TODO: No longer needs to be asynchronous, is the overhead unnecessary?
        	ArrayList<String> optIngredients = (ArrayList<String>)params[0];
        	engine = (SearchEngine)params[1];
        	ArrayList<String> reqIngredients = (ArrayList<String>)params[2];

    		if (ranker == null) {
    			ranker = new BM25Ranker(context, optIngredients, reqIngredients, 
		        		SEARCH_INGREDIENT);			    
    		} 
    		
        	return ranker;
        }

        /**
         * Once it's done, move over to send to home
         */
        @Override
        protected void onPostExecute(BM25Ranker ranker) {
            engine.ranker = ranker;
            loginDialog.dismiss();
            ranker.execute();
        }
        
        protected void onPreExecute() {
        	super.onPreExecute();
    		loginDialog = new ProgressDialog(context);
        	loginDialog.setMessage("Please wait... warming up");
        	loginDialog.setCancelable(false);
            loginDialog.show();
        }
    }
	
}
