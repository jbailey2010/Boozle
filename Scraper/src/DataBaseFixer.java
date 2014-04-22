import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Map;


public class DataBaseFixer {

	public static void main(String args[]) throws SQLException { 

		ArrayList<Drink> drinks = fixDrinks();
	
	}

	public static ArrayList<Drink> fixDrinks()
	{
		ArrayList<Drink> drinks = DataBaseReader.getAllDrinks();
		System.out.println(drinks.size());
		for(int i = 0; i < drinks.size(); i++) {

			//Makes sure there are ingredients and there aren't duplicates
			if (drinks.get(i).getIngredients().size() == 0 || 
					drinks.get(i).getName().contains("#2") || 
					drinks.get(i).getName().contains("#3")) {
				drinks.remove(i);
			}
			
			//Fixes the unicode issue
			for (Ingredient ing : drinks.get(i).getIngredients())
			{
				ing.setName(Normalizer.normalize(ing.getName(), Normalizer.Form.NFKC));
				ing.setQuantity(Normalizer.normalize(ing.getQuantity(), Normalizer.Form.NFKC));
				ing.setUnits(Normalizer.normalize(ing.getUnits(), Normalizer.Form.NFKC));
			}
			System.out.println(drinks.get(i).getIngredients().toString());
		}
		System.out.println(drinks.size() + "\u00BD");
		
		for (int i = 0; i < drinks.size(); i++)
		{
			drinks.get(i).setId(i);
		}
		
		return drinks;
	}
}
