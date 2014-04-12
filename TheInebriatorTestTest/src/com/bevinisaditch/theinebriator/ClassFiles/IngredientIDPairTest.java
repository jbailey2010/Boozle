package com.bevinisaditch.theinebriator.ClassFiles;

import android.test.AndroidTestCase;

public class IngredientIDPairTest extends AndroidTestCase 
{
	public void testConstructor()
	{
		IngredientIDPair pair = new IngredientIDPair(0, "whiskey");
		assertTrue(pair.name.equals("whiskey"));
		assertTrue(pair.id == 0);
	}
}