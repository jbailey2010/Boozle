package com.devingotaswitch.theinebriator.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;

public class IngredientTest {

	@Test
	public void test() {
		Ingredient ing  = new Ingredient("vodka", "3/4", "cup");
		assertTrue(ing.toString().equals("vodka (3/4 cup)"));
	}

}
