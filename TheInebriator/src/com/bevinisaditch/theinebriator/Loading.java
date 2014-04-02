package com.bevinisaditch.theinebriator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bevinisaditch.theinebriator.ClassFiles.DataBaseReader;
import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;
import com.devingotaswitch.theinebriator.R;

public class Loading extends Activity {
	public Context cont;
	private ImageView img;
	public static ArrayList<Drink> drinks;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		ActionBar ab = getActionBar();
		cont = this;
		ab.setDisplayShowTitleEnabled(false);
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
	 * Transition function that will transition loading screen to home screen
	 */
	private void sendToHome()
	{
		Intent i2 = new Intent(Loading.this, Home.class);
		startActivity(i2);
		overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
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
	
	private class AsyncLoader extends AsyncTask<Activity, Void, ArrayList<Drink>> {
		Activity act;
        @Override
        protected ArrayList<Drink> doInBackground(Activity... params) {
        	act = (Activity)params[0];
        	DrinkDatabaseHandler drinkHandler = new DrinkDatabaseHandler(cont);
            return drinkHandler.getAllDrinks();
        }

        @Override
        protected void onPostExecute(ArrayList<Drink> result) {
            drinks = result;
            Intent intent = new Intent(act, Home.class);
            startActivity(intent);
        }
    }

}
