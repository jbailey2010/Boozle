package com.bevinisaditch.theinebriator.SearchEngine;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.Database.TermFrequencyDatabaseHandler;

/**
 * Advanced unit test for the BM25Ranker.
 * 
 * This class tests the ranker for all characters in the ASCII character set to ensure
 * no character will interfere with run-time operations.
 * 
 * @author michael
 *
 */
@RunWith(Parameterized.class)
public class B25RankerParameterizedTest extends AndroidTestCase {
	private BM25Ranker ranker;
	private Drink drink;
	private ArrayList<String> terms;
	private ArrayList<Drink> drinks;
	ArrayList<String> data;
	
	public void setUp() {
		System.setProperty("dexmaker.dexcache", getContext().getCacheDir().toString());
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
			
			ranker = new BM25Ranker(context, terms, drinks, SearchEngine.SEARCH_NAME);
			
			TermFrequencyDatabaseHandler mockedDB = mock(TermFrequencyDatabaseHandler.class);
			when(mockedDB.getTermFrequency(any(String.class))).thenReturn(null);
			
			ranker.handler = mockedDB;
		}
		
		assertEquals(drink, ranker.rank(terms, drinks).get(0));
		
	}
}
