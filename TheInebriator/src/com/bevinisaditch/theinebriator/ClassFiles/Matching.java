package com.bevinisaditch.theinebriator.ClassFiles;

public class Matching {
	public int drinkID = -1;
	public int ingredientID = -1;
	public int matchID = -1;
	public String quantity = null;
	public String units = null;
	
	public Matching()
	{
		
	}
	
	public Matching(int drink, int ing, int match, String qty, String units)
	{
		drinkID = drink;
		ingredientID = ing;
		matchID = match;
		quantity = qty;
		this.units = units;
	}
}
