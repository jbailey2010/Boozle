package com.bevinisaditch.theinebriator.ClassFiles;

import android.test.AndroidTestCase;

public class MatchingTest extends AndroidTestCase {

	public void testEmptyConstructor()
	{
		Matching match = new Matching();
		assertTrue(match.quantity == null);
		assertTrue(match.units == null);
		assertTrue(match.drinkID == -1);
		assertTrue(match.ingredientID == -1);
		assertTrue(match.matchID == -1);
	}
	
	public void testConstructor()
	{
		Matching match = new Matching(1, 2, 3, "5", "pounds");
		assertTrue(match.drinkID == 1);
		assertTrue(match.ingredientID == 2);
		assertTrue(match.matchID == 3);
		assertTrue(match.quantity.equals("5"));
		assertTrue(match.units.equals("pounds"));
	}
}