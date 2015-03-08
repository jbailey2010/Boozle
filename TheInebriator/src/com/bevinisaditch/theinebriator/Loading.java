package com.bevinisaditch.theinebriator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.Menu;
import android.widget.ImageView;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.IngredientIDPair;
import com.bevinisaditch.theinebriator.ClassFiles.Matching;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;
import com.bevinisaditch.theinebriator.Utils.GeneralUtils;
import com.devingotaswitch.theinebriator.R;
/**
 * The activity that loads from file, with a fun gif to kill time
 * @author Jeff
 *
 */
public class Loading extends Activity {
	public Context cont;
	private ImageView img;
	public static List<String> drinkNames;
	public static List<String> ingrNames;
	public static HashMap<Integer, IngredientIDPair> allPairs;
	
	/**
	 * Sets the display, and spawns the loading threads
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		cont = this;
		handleInitialLoad();
		AsyncLoader loader = new AsyncLoader();
		loader.execute(this);
	}

	/**
	 * Auto-generated, unused in this activity
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.loading, menu);
		return true;
	}
	
	/**
	 * Sets the animation of the gif to work, then manages the loading of the data from file
	 */
	private void handleInitialLoad() {
		img = (ImageView)findViewById(R.id.loading_gif);
		img.setBackgroundResource(R.drawable.drink_gif);
		AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
		frameAnimation.start();
	}
	
	/**
	 * The asynctask that will load data from file and pass it to the home activity
	 * @author Jeff
	 *
	 */
	private class AsyncLoader extends AsyncTask<Activity, Void, Void> {
		Activity act;
		List<String> drinks;
		List<String> ingredients;
		HashMap<Integer, IngredientIDPair> pairs;
		HashSet<String> units;
		
		private String sanitizeIngr(String input){
			//Apply trimming here for units and whatnot
			String[] ingrArr = input.split(" ");
			for(int i = 0; i < ingrArr.length; i++){
				String elem = ingrArr[i];
				if(units.contains(elem) && i+1 < ingrArr.length){
					input = input.split(elem)[1];
					break;
				}
			}
			return input;
		}
		
		/**
		 * Calls the method to load the drinks
		 */
        @Override
        protected Void doInBackground(Activity... params) {
        	act = (Activity)params[0];
        	DrinkDatabaseHandler drinkHandler = new DrinkDatabaseHandler(cont);
        	drinks = drinkHandler.getDrinkNames();
        	pairs = drinkHandler.getAllPairs();
        	ingredients = new ArrayList<String>();
        	
			units = GeneralUtils.getUnits();
        	for(Integer key : pairs.keySet()){
        		ingredients.add(WordUtils.capitalize(sanitizeIngr(pairs.get(key).name)));
        	}
        	
        	return null;
        }

        /**
         * Once it's done, move over to send to home
         */
        @Override
        protected void onPostExecute(Void result) {
            drinkNames = drinks;
            ingrNames = ingredients;
            allPairs = pairs;
            Intent intent = new Intent(act, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, 0);
            overridePendingTransition(0,0); //0 for no animation
        }
    }

}
