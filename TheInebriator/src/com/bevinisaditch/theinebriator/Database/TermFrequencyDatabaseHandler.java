package com.bevinisaditch.theinebriator.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.bevinisaditch.theinebriator.Home;
import com.bevinisaditch.theinebriator.Loading;
import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
import com.bevinisaditch.theinebriator.ClassFiles.TermFrequency;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class handles the database operations for term frequency, which is used in the search engine.
 * 
 * @author michael
 *
 */
public class TermFrequencyDatabaseHandler {
 
    private static final String DATABASE_NAME = "termFrequency";
 
    public TermFrequencyDatabaseHandler() {
    }
	
	
	/**
	 * Takes a list of drinks and calculates the frequencies for each term.
	 * Then inserts in to the database.
	 * 
	 * @param allDrinks - ArrayList of drinks to break down into terms
	 */
	public void populateDatabase(Context cont, List<String> drinkNames, List<String> list) {
		HashMap<String, Float> termFreq = new HashMap<String, Float>();
        Integer totalTermCount = 0;
        for (String name : drinkNames) {
        	name = name.toLowerCase();
        	String[] terms = name.split("\\s+");
        	Float count;
        	for (String term : terms) {        		
	        	count = termFreq.get(term);
	        	if (count != null) {
	        		termFreq.put(term, count + 1);
	        	} else {
	        		termFreq.put(term, 1f);
	        	}
	        	totalTermCount += 1;
        	}
        }
        for(String name : list){
        	name = name.toLowerCase();
        	String [] terms = name.split("\\s+");
        	Float count;
        	for (String term : terms) {
	        	count = termFreq.get(term);
	           	if (count != null) {
	           		termFreq.put(term, count + 1);
	           	} else {
	           		termFreq.put(term, 1f);
	           	}
	           	totalTermCount += 1;
        	}
        }
        
        //Calculate the frequency for term
        for (Entry<String, Float> entry : termFreq.entrySet()) {
        	termFreq.put(entry.getKey(), entry.getValue()/totalTermCount);
        }
		        
        //Add each entry to the database
        addAllTermFreq(termFreq, cont, drinkNames.size());
	}
	
	private void addAllTermFreq(HashMap<String, Float> termMap, Context c, int drinkCount) {
		SharedPreferences.Editor editor = c.getSharedPreferences(DATABASE_NAME, 0).edit();
		editor.putInt("LastDrinkSize", drinkCount);
		for(String term : termMap.keySet()){
			editor.putFloat(term, termMap.get(term));
		}
		editor.apply();
	}
	 
	/**
	 * Get an instance of term frequency by name of the term
	 * 
	 * @param term - name of the term
	 * @return TermFrequency instance
	 */
	public TermFrequency getTermFrequency(Context c, String term) {
		SharedPreferences sp = c.getSharedPreferences(DATABASE_NAME, 0);
		return new TermFrequency(term, sp.getFloat(term, 0));
	}
	
	public int lastCount(Context c){
		SharedPreferences sp = c.getSharedPreferences(DATABASE_NAME, 0);
		return sp.getInt("LastDrinkSize", 0);
	}
}
