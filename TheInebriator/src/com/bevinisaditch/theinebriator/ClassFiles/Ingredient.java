package com.bevinisaditch.theinebriator.ClassFiles;

/**
 * Basic class for an ingredient of a drink.
 * @author termaat1
 *
 */
public class Ingredient {

	private String name;
	private String quantity;
	private String units;
	
	public Ingredient(String name, String quantity, String units)
	{
		this.name = name;
		this.quantity = quantity;
		this.units = units;
	}
	
	public String toString()
	{
		return name + " (" + quantity + " " + units + ")";
	}
}
