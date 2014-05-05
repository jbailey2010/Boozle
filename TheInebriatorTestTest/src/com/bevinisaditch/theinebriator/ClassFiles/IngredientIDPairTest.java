package com.bevinisaditch.theinebriator.ClassFiles;

import android.test.AndroidTestCase;

/**
 * This class tests the IngredientIDPair data strucutre, which is used in the database
 *
 */
public class IngredientIDPairTest extends AndroidTestCase 
{
	public void testConstructor()
	{
		IngredientIDPair pair = new IngredientIDPair(0, "whiskey");
		assertTrue(pair.name.equals("whiskey"));
		assertTrue(pair.id == 0);
	}
}