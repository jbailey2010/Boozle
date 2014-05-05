package com.bevinisaditch.theinebriator.Scrapers;

import java.io.IOException;
import java.util.ArrayList;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;

import android.content.Context;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLite extends SQLiteOpenHelper{

	public static final String TABLE_DRINKS = "drinks";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_INSTRUCTIONS = "instructions";
	
	
	public SQLite(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		populateDatabase(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void populateDatabase(SQLiteDatabase db) {
		try {
			ArrayList<Drink> dranks_1 = DrinkOfTheWeekScraper.scrapeDrinks();
			ArrayList<Drink> dranks_2 = DrinkMixerScraper.scrapeDrinks();
			ArrayList<Drink> dranks_3 = GoodCocktailsScraper.returnScrapedDrinks();
			

			int max = 0;
			for(int i = 0; i < dranks_1.size(); i++) {
				if(dranks_1.get(i).getIngredients().size() > max) {
					max = dranks_1.get(i).getIngredients().size();
				}
			}
			for(int i = 0; i < dranks_2.size(); i++) {
				if(dranks_2.get(i).getIngredients().size() > max) {
					max = dranks_2.get(i).getIngredients().size();
				}
			}
			for(int i = 0; i < dranks_3.size(); i++) {
				if(dranks_3.get(i).getIngredients().size() > max) {
					max = dranks_3.get(i).getIngredients().size();
				}
			}
			
			String COLUMN_INGREDIENTS = "ingredient_0, quantity_0, unit_0";
			for(int i = 1; i < max; i++) {
				COLUMN_INGREDIENTS += ", ingredient_" + i + ", quantity_" + i + ", unit_" + i;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not scrape all the websites");
			return;
		}
		
	}

}
