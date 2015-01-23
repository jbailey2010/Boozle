package com.bevinisaditch.theinebriator.SearchEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.bevinisaditch.theinebriator.Home;
import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
import com.bevinisaditch.theinebriator.ClassFiles.TermFrequency;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;

@SuppressLint("DefaultLocale")
public class BM25Ranker extends Ranker {
	public Context context;
	public ProgressDialog loginDialog;
	ArrayList<String> optIngredients;
	ArrayList<String> reqIngredients;
	Integer searchType;
	
	//Constants used in BM25
	private double b = .75;
	private double k = 1.2;
	
	/**
	 * Constructor for ranker
	 * 
	 * @param context - Android activity context
	 * @param terms - Search terms
	 * @param drinks - Relevant drinks to be ranked
	 * @param searchType - Search by name or ingredient... 
	 * 	Use SearchEngine.SEARCH_INGREDIENT or SearchEngine.SEARCH_NAME
	 * @param byName 
	 */
	public BM25Ranker( Context context, ArrayList<String> optIngredients, ArrayList<String> reqIngredients,
			Integer searchType) {
		this.context = context;
		this.optIngredients = optIngredients;
		this.reqIngredients = reqIngredients;
		this.searchType = searchType;
	}
	
	
    protected void onPreExecute() {
    	super.onPreExecute();
		loginDialog = new ProgressDialog(context);
    	loginDialog.setMessage("Please wait... Searching");
    	loginDialog.setCancelable(false);
        loginDialog.show();
    }
	
	@Override
	protected ArrayList<Drink> doInBackground(Void... params) {
		
		return rank();
	}
	
	@Override
    protected void onPostExecute(ArrayList<Drink> result) {
		if (context instanceof Home) {
			Home.backToListResults = false;
			Home.backToNoResults = true;
			((Home) context).listviewInit(result, true);
		}
		loginDialog.dismiss();
    }
	

	@Override
	public ArrayList<Drink> rank() {
		DrinkDatabaseHandler drinkHandler = Home.getHandler(context);
		ArrayList<Drink> drinks = new ArrayList<Drink>();
		ArrayList<String> terms = new ArrayList<String>();
		
		if(searchType == SearchEngine.SEARCH_NAME){
			terms.add(reqIngredients.get(0));
			
			String[] pterms = terms.get(0).split("\\s+");
			ArrayList<String> parsedTerms = new ArrayList<String>();
			for (String term : pterms) {
				parsedTerms.add(term);
			}
			
			drinks = drinkHandler.getRelevantDrinksByName(parsedTerms);
			Log.d("SearchEngine", "Got drinks");

		}
		else if(searchType == SearchEngine.SEARCH_INGREDIENT){
			drinks = drinkHandler.getRelevantDrinksByIngredient(reqIngredients);
			Log.d("SearchEngine", "Got drinks");
			//If by ingredient, add all optional and required ingredients
			terms.addAll(optIngredients);
			terms.addAll(reqIngredients);
			
		}		
		HashMap<Drink, Double> unsortedDrinks = new HashMap<Drink, Double>();		
		ArrayList<String> individualTerms = parseTerms(terms);
		
		//For each drink, get a score
		for (Drink drink : drinks) {
			double score = 0.0;
			
			ArrayList<String> wordsInDrink = new ArrayList<String>();
			if (searchType == SearchEngine.SEARCH_NAME) {
				wordsInDrink.add(drink.getName());
			}
			
			if (searchType == SearchEngine.SEARCH_INGREDIENT) {
				for (Ingredient ing : drink.getIngredients()) {
					wordsInDrink.add(ing.getName());
				}
			}
			
			double totalFreq = parseTerms(wordsInDrink).size();
			//Sum up all terms to get score
			for (String term : individualTerms) {
				double docFreq = 0.0;
				
				if (searchType == SearchEngine.SEARCH_NAME) {
					String[] drinkName = drink.getName().toLowerCase().trim().split("\\s+");
					for(String part : drinkName){
						if(part.equals(term.toLowerCase())){
							docFreq += 1.0;
						}
					}
					
				}
				
				if (searchType == SearchEngine.SEARCH_INGREDIENT) {
					for (Ingredient ingredient : drink.getIngredients()) {
						if ((ingredient.getName().toLowerCase()).contains(term.toLowerCase())) {
							docFreq += 1.0;
						}
					}
				}
				docFreq /= totalFreq;
				score += docFreq;
			}
			unsortedDrinks.put(drink, score);
		}
		
		ArrayList<Drink> sortedDrinks = sortDrinks(unsortedDrinks);
		
		return sortedDrinks;
	}
	
	/**
	 * Sort a hashmap of drinks based on their score
	 * @param unsortedDrinks - A Hashmap of Key=Drink, Value=Double (the score)
	 * @return sorted TreeMap of Drinks based on their score
	 */
	public ArrayList<Drink> sortDrinks(HashMap<Drink, Double> unsortedDrinks) {
		DrinkComparator comparator = new DrinkComparator(unsortedDrinks);
		TreeMap<Drink, Double> sortedDrinks = new TreeMap<Drink, Double>(comparator);
		sortedDrinks.putAll(unsortedDrinks);
		
		ArrayList<Drink> returnedDrinks = new ArrayList<Drink>();
		Entry<Drink, Double> currentEntry = sortedDrinks.pollFirstEntry();
		while (currentEntry != null) {
			Drink currentDrink = currentEntry.getKey();
			returnedDrinks.add(currentDrink);
			currentEntry = sortedDrinks.pollFirstEntry();
		}
		
		return returnedDrinks;
	}
	
	/**
	 * Takes in a list of search terms and parses them by all white space.
	 * For example, 'Orange Juice' would be come 'Orange' and 'Juice'
	 * 
	 * @param terms List of search terms
	 * @return list of terms parsed by white space
	 */
	public ArrayList<String> parseTerms(ArrayList<String> terms) {
		ArrayList<String> individualTerms = new ArrayList<String>();
		for (String term : terms) {
			String[] pieces = term.split("\\s+");
			individualTerms.addAll(Arrays.asList(pieces));
			
		}
		
		return individualTerms;
	}
	
	/**
	 * Takes a list of drinks and returns the average number of words per drink
	 * @param drinks - ArrayList of drinks
	 * @return average number of words per drink
	 */
	public int getAvgLengthOfDrinks(ArrayList<Drink> drinks) {
		int averageLength = 0;
		for (Drink drink : drinks){
			
			ArrayList<String> words = new ArrayList<String>();
			
			//Add name to length of document
			if (searchType == SearchEngine.SEARCH_NAME) {
				words.add(drink.getName());
				averageLength += parseTerms(words).size();
			}
			
			//Add ingredients to length of document
			if (searchType == SearchEngine.SEARCH_INGREDIENT) {
				for (Ingredient ing: drink.getIngredients()) {
					if(ing.getName() != null){
						words.add(ing.getName());
					}
				}
				
				averageLength += parseTerms(words).size();
			}
		}
		
		if (drinks.size() == 0) {
			return 0;
		}
		
		return averageLength/drinks.size();
	}
}

/**
 * Used for sorting drinks in a map based on their score.
 * 
 * @author michael
 */
class DrinkComparator implements Comparator<Drink> {
	Map<Drink, Double> drinks;
	
	public DrinkComparator(Map<Drink, Double> drinks) {
		this.drinks = drinks;
	}
	
	@Override
	public int compare(Drink a, Drink b) {
		if (drinks.get(a) >= drinks.get(b)) {
            return -1;
        } else {
            return 1;
        } 
		
	}	
	
}
