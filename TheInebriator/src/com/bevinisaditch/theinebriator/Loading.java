package com.bevinisaditch.theinebriator;

import java.util.ArrayList;
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
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;
import com.devingotaswitch.theinebriator.R;
/**
 * The activity that loads from file, with a fun gif to kill time
 * @author Jeff
 *
 */
public class Loading extends Activity {
	public Context cont;
	private ImageView img;
	public static ArrayList<Drink> drinks;
	/**
	 * Sets the display, and spawns the loading threads
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		//ActionBar ab = getActionBar();
		cont = this;
		//ab.setDisplayShowTitleEnabled(false);
		handleInitialLoad();
		AsyncLoader loader = new AsyncLoader();
		loader.execute(this);
	}

	/**
	 * Auto-generated, unused in this activity
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loading, menu);
		return true;
	}
	
	/**
	 * Sets the animation of the gif to work, then manages the loading of the data from file
	 */
	private void handleInitialLoad()
	{
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
	private class AsyncLoader extends AsyncTask<Activity, Void, ArrayList<Drink>> {
		Activity act;
		/**
		 * Calls the method to load the drinks
		 */
        @Override
        protected ArrayList<Drink> doInBackground(Activity... params) {
        	act = (Activity)params[0];
        	DrinkDatabaseHandler drinkHandler = new DrinkDatabaseHandler(cont);
            return drinkHandler.getAllDrinks();
        }

        /**
         * Once it's done, move over to send to home
         */
        @Override
        protected void onPostExecute(ArrayList<Drink> result) {
            drinks = result;
            Intent intent = new Intent(act, Home.class);
            startActivity(intent);
        }
    }

}
