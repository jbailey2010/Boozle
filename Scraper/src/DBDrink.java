import java.util.ArrayList;


public class DBDrink {
	public Drink.Rating rating;
	public ArrayList<DBIngredient> ingredients;
	public boolean isFavorited;
	public String name;
	public String instructions;
	public String url;
	public int id;
	
	public DBDrink()
	{
		rating = Drink.Rating.THUMBSNULL;
	}
	
	public DBDrink(Drink other)
	{
		this.rating = other.getRating();
		this.name = other.getName();
		this.instructions = other.getInstructions();
		this.url = other.getUrl();
		this.id = other.getId();
		this.ingredients = new ArrayList<DBIngredient>();
	}
}
