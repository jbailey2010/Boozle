package com.bevinisaditch.theinebriator.ClassFiles;

import android.test.AndroidTestCase;

import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;

public class IngredientTest extends AndroidTestCase {

	public void test() {
		Ingredient ing  = new Ingredient("vodka", "3/4", "cup");
		assertEquals("Ingredient [name=vodka, quantity=3/4, units=cup]", ing.toString());
	}

}
