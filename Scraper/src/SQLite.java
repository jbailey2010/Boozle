import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;

public class SQLite {
	public void populateDatabase() {
		try {
			ArrayList<Drink> dranks_1 = DrinkOfTheWeekScraper.scrapeDrinks();
			ArrayList<Drink> dranks_2 = DrinkMixerScraper.scrapeDrinks();
			ArrayList<Drink> dranks_3 = GoodCocktailsScraper.returnScrapedDrinks();
			

			int max = 0;
			for(int i = 0; i < dranks_1.size(); i++) {
				if(dranks_1.get(i).getIngredients().size() > max) {
					max = dranks_1.get(i).getIngredients().size();
				}
			}
			for(int i = 0; i < dranks_2.size(); i++) {
				if(dranks_2.get(i).getIngredients().size() > max) {
					max = dranks_2.get(i).getIngredients().size();
				}
			}
			for(int i = 0; i < dranks_3.size(); i++) {
				if(dranks_3.get(i).getIngredients().size() > max) {
					max = dranks_3.get(i).getIngredients().size();
				}
			}
			
			String COLUMN_INGREDIENTS = "ingredient_0, quantity_0, unit_0";
			for(int i = 1; i < max; i++) {
				COLUMN_INGREDIENTS += ", ingredient_" + i + ", quantity_" + i + ", unit_" + i;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not scrape all the websites");
			return;
		}
	}
}
