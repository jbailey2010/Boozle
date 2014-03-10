package com.bevinisaditch.theinebriator.ClassFiles;

//import Drink;
//import Ingredient;

import java.util.ArrayList;

//import Drink.Rating;

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
	private String instructions;
	private String url;
	private int id;
	
	public Drink(String name, Rating rating, ArrayList<Ingredient> ingredients, String instructions, int id)
	{
		this.name = name;
		this.rating = rating;
		this.ingredients = ingredients;
		this.instructions = instructions;
		this.id = id;
	}
	
	public Drink(String name)
	{
		rating = Rating.THUMBSNULL;
		ingredients = new ArrayList<Ingredient>();
		isFavorited = false;
		this.name = name;
	}
	
	public int getId()
	{
		return id;
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
	
	public void setInstructions(String instructions)
	{
		this.instructions = instructions;
	}
	
	public String getInstructions()
	{
		return instructions;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Drink [rating=" + rating + ", ingredients=" + ingredients
				+ ", isFavorited=" + isFavorited + ", name=" + name
				+ ", instructions=" + instructions + ", url=" + url + "]";
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ingredients == null) ? 0 : ingredients.hashCode());
		result = prime * result
				+ ((instructions == null) ? 0 : instructions.hashCode());
		result = prime * result + (isFavorited ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Drink other = (Drink) obj;
		if (ingredients == null) {
			if (other.ingredients != null)
				return false;
		} else if (!ingredients.equals(other.ingredients))
			return false;
		if (instructions == null) {
			if (other.instructions != null)
				return false;
		} else if (!instructions.equals(other.instructions))
			return false;
		if (isFavorited != other.isFavorited)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rating != other.rating)
			return false;
		return true;
	}
	
	
}
