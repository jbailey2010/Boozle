package com.bevinisaditch.theinebriator.ClassFiles;

import java.util.ArrayList;

/**
 * Basic drink object. Has list of ingredients and whether it is thumbsed up/down/favorited.
 * @author Jason
 *
 */
public class Drink {
	public enum Rating { THUMBSUP, THUMBSDOWN, THUMBSNULL }
	private Rating rating;
	private ArrayList<Ingredient> ingredients;
	private boolean isFavorited;
	private String name;
	
	public Drink(String name)
	{
		rating = Rating.THUMBSNULL;
		ingredients = new ArrayList<Ingredient>();
		isFavorited = false;
		this.name = name;
	}
	
	public Rating getRating()
	{
		return rating;
	}
	
	public void setRating(Rating rating)
	{
		this.rating = rating;
	}
	
	public ArrayList<Ingredient> getIngredients()
	{
		return ingredients;
	}
	
	public void setIngredients(ArrayList<Ingredient> ingredients)
	{
		this.ingredients = ingredients;
	}
	
	public void addIngredient(Ingredient ingredient)
	{
		ingredients.add(ingredient);
	}
	
	public boolean isFavorited()
	{
		return isFavorited;
	}
	
	public void favorite()
	{
		isFavorited = true;
	}
	
	public void unfavorite()
	{
		isFavorited = false;
	}
	
	public String getName()
	{
		return name;
	}
}
