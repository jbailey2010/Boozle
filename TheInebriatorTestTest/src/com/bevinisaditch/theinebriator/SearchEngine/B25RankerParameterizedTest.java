package com.bevinisaditch.theinebriator.SearchEngine;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;

@RunWith(Parameterized.class)
public class B25RankerParameterizedTest extends AndroidTestCase {
	private BM25Ranker ranker;
	private Drink drink;
	private ArrayList<String> terms;
	private ArrayList<Drink> drinks;
	ArrayList<String> data;
	
	/**
	public B25RankerParameterizedTest(String term, String drinkName) {
		RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
		
		terms = new ArrayList<String>();
		terms.add(term);
		
		drink = new Drink(drinkName);
		drinks = new ArrayList<Drink>();
		drinks.add(drink);
		
		ranker = new BM25Ranker(context, terms, drinks);
		
	}
	
	public B25RankerParameterizedTest() {};
	
	@Parameters
	public static Collection<String[]> data() {
		 Collection<String[]> data = new ArrayList<String[]>();
		 final Charset charset = Charset.forName("US-ASCII");
		 
		 for (int i = 0; i < 255; i++) {
			 ByteBuffer bb = ByteBuffer.allocate(4);
		     bb.putInt(i);
		     String character = new String(bb.array(), charset).trim();
		     if (character.length() != 0) {
		   	     data.add(new String[] {character, character});
		     }
		 }
		 
		 return data;
	}
	
	
	
	**/
	
	public void setUp() {
		data = new ArrayList<String>();
		final Charset charset = Charset.forName("US-ASCII");
		 
		for (int i = 0; i < 255; i++) {
			 ByteBuffer bb = ByteBuffer.allocate(4);
		     bb.putInt(i);
		     String character = new String(bb.array(), charset).trim();
		     if (character.length() != 0) {
		   	     data.add(character);
		     }
		     
		 }
		
	}
	
	public void testRank() {
		
		
		for (String character : data) {
			RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
			
			terms = new ArrayList<String>();
			terms.add(character);
			
			drink = new Drink(character);
			drinks = new ArrayList<Drink>();
			drinks.add(drink);
			
			ranker = new BM25Ranker(context, terms, drinks);
		}
		
		assertEquals(drink, ranker.rank(terms, drinks).get(0));
		
	}
}
