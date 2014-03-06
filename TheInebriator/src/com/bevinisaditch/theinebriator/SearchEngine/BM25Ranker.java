package com.bevinisaditch.theinebriator.SearchEngine;

import java.util.ArrayList;
import java.util.Arrays;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;

public class BM25Ranker extends Ranker {
	
	//Constants used in BM25
	private double b = .75;
	private double k = 1.2;

	@Override
	public ArrayList<Drink> rank(ArrayList<String> terms,
			ArrayList<Drink> drinks) {
		
		ArrayList<String> individualTerms = parseTerms(terms);

		int averageLength = getAvgLengthOfDrinks(drinks);
		
		//For each drink, get a score
		for (Drink drink : drinks) {
			double score = 0.0;
			
			//Sum up all terms to get score
			for (String term : individualTerms) {
				double termFreq = 0.0;
				
				//TODO: Get term frequency by querying DB
				
				double invDocFreq = Math.log((drinks.size()-termFreq+.5)/(termFreq + .5))/Math.log(2);
				
				double totalFreq = 0.0;
				double docFreq = 0.0;
				for (Ingredient ingredient : drink.getIngredients()) {
					if (ingredient.getName().toLowerCase().contains(term.toLowerCase())) {
						docFreq += 1.0;
					}
					totalFreq += 1.0;
				
					docFreq /= totalFreq;
					
					double numerator = docFreq*(k+1)*invDocFreq;
					double denominator = docFreq + k*(1-b+ b*(totalFreq/averageLength));
					
					score += numerator/denominator;
				}
			}
		}
		
		return null;
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
	
	
	public int getAvgLengthOfDrinks(ArrayList<Drink> drinks) {
		int averageLength = 0;
		for (Drink drink : drinks){
			//Add 1 for the name
			averageLength += 1;
			averageLength += drink.getIngredients().size();			
		}
		return averageLength/drinks.size();
	}
	

}
