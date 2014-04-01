package com.bevinisaditch.theinebriator.ClassFiles;

public class Matching {
	public int drinkID;
	public int ingredientID;
	public int matchID;
	public String quantity;
	public String units;
	
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
