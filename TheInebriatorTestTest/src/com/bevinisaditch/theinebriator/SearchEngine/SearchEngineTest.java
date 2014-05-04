package com.bevinisaditch.theinebriator.SearchEngine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;

@RunWith(PowerMockRunner.class)
public class SearchEngineTest extends AndroidTestCase {
	Drink drink1;
	Drink drink2;
	ArrayList<Drink> drinks;
	Ingredient ing1;
	Ingredient ing2;
	Ingredient ing3;
	RenamingDelegatingContext context;
	SearchEngine engine;
	DrinkDatabaseHandler mockedDB;
	

	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		System.setProperty("dexmaker.dexcache", getContext().getCacheDir().toString());
		context = new RenamingDelegatingContext(getContext(), "test_");
		
		ing1 = new Ingredient("ing1", "1", "oz");
		ing2 = new Ingredient("ing2", "2", "oz");
		ing3 = new Ingredient("ing3", "3", "oz");
		drink1 = new Drink("drink1");
		drink1.addIngredient(ing1);
		drink1.addIngredient(ing2);
		drink2 = new Drink("drink2");
		drink2.addIngredient(ing3);
		
		drinks = new ArrayList<Drink>();
		drinks.add(drink1);
		drinks.add(drink2);
		
		mockedDB = mock(DrinkDatabaseHandler.class);
		when(mockedDB.getAllDrinks()).thenReturn(drinks);
	}

	public void testSearchByName() throws InterruptedException, ExecutionException {
		ArrayList<Drink> expectedDrinks = new ArrayList<Drink>();
		expectedDrinks.add(0, drink2);
		expectedDrinks.add(1, drink1);
		
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("drink2");
		
		BM25Ranker mockedRanker = spy(new BM25Ranker(context, terms, drinks, SearchEngine.SEARCH_NAME) );
		when(mockedRanker.doInBackground()).thenReturn(expectedDrinks);
		
		engine = new SearchEngine(context, mockedRanker, mockedDB);
		assertEquals(expectedDrinks, engine.searchByName("drink2"));
		verify(mockedDB).getRelevantDrinksByName(any(ArrayList.class));
		verify(mockedRanker).doInBackground();
		
	}
	
	public void testSearchByIngredient() {
		ArrayList<Drink> expectedDrinks = new ArrayList<Drink>();
		expectedDrinks.add(0, drink2);
		expectedDrinks.add(1, drink1);
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("ingr3");
		
		BM25Ranker mockedRanker = spy(new BM25Ranker(context, terms, drinks, SearchEngine.SEARCH_INGREDIENT) );
		when(mockedRanker.doInBackground()).thenReturn(expectedDrinks);
		
		engine = new SearchEngine(context, mockedRanker, mockedDB);
		
		ArrayList<String> optIngs = new ArrayList<String>();
		optIngs.add("ing3");
		ArrayList<String> reqIngs = new ArrayList<String>();
		reqIngs.add("ing3");
		
		assertEquals(expectedDrinks, engine.searchByIngredient(optIngs, reqIngs));
		verify(mockedDB).drinksForRequiredIngredients(any(ArrayList.class));
		verify(mockedRanker).doInBackground();
	}

}
