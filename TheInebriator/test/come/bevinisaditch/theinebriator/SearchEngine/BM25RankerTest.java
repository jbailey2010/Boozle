package come.bevinisaditch.theinebriator.SearchEngine;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.SearchEngine.BM25Ranker;

public class BM25RankerTest {
	private static ArrayList<Drink> drinks = new ArrayList<Drink>();
	private static BM25Ranker ranker = new BM25Ranker();

	@BeforeClass
	public static void setUpDrinkList() throws Exception {
		Drink drink1 = new Drink("rum and coke");
		Drink drink2 = new Drink("vodka and coke");
		Drink drink3 = new Drink("whiskey and coke");
		drinks.add(drink1);
		drinks.add(drink2);
		drinks.add(drink3);
		
	}

	@Test
	public void testRank_DrinkName() {
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("rum");
		ArrayList<Drink> sortedDrinks = ranker.rank(terms, drinks);
		assertEquals(drinks.get(1), sortedDrinks.get(1));
		
		terms.clear();
		terms.add("whiskey");
		sortedDrinks = ranker.rank(terms, drinks);
		assertEquals(drinks.get(3), sortedDrinks.get(1));
		
		
	}

}
