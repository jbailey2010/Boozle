package com.bevinisaditch.theinebriator.ClassFiles;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bevinisaditch.theinebriator.Home;
import com.bevinisaditch.theinebriator.Loading;
import com.bevinisaditch.theinebriator.TwitterInteraction;
import com.bevinisaditch.theinebriator.ClassFiles.Drink.Rating;
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;
import com.bevinisaditch.theinebriator.Utils.GeneralUtils;
import com.devingotaswitch.theinebriator.R;
import com.socialize.ActionBarUtils;
import com.socialize.entity.Entity;
import com.socialize.ui.actionbar.ActionBarOptions;
/**
 * Handles the logic for the drink pop up entirely
 * @author Jeff
 *
 */
public class DrinkPopup {
	public static Context cont;
	public static ImageView tu;
	public static ImageView td;
	public static boolean isThumbsUp;
	public static boolean isThumbsDown;
	public static String nameDrink;
	public static String ingrDrink;
	public static String instrDrink;
	
	/**
	 * Configures the initial pop up to appropriately handle input and
	 * display the data from the clicked element itself
	 */
	public static void drinkPopUpInit(final Context c, final String name, String ingredients, final String instr, final boolean update, Rating rating, boolean showRefresh, final boolean isAllRandom)
	{
		nameDrink = name;
		ingrDrink = ingredients;
		instrDrink = instr;
		cont = c;
		final Dialog dialog = new Dialog(cont, R.style.DialogBackground);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.setContentView(R.layout.drink_popup);
	    //If there's internet, add the comment thread
	    if(GeneralUtils.testInternet(c)){
		 	String entityKey = "http://www.boozle.com/" + name;
		 	Entity entity = Entity.newInstance(entityKey, name);
		 	ActionBarOptions options = new ActionBarOptions();
		 	options.setHideShare(true);
		 	options.setFillColor(Color.parseColor("#272727"));
		 	options.setBackgroundColor(Color.parseColor("#191919"));
		 	options.setAccentColor(Color.parseColor("#0000cc"));
		 	View actionBarWrapped = ActionBarUtils.showActionBar((Activity) cont, R.layout.drink_popup, entity, options);
		 	dialog.setContentView(actionBarWrapped);
		}
	    dialog.show(); 
	    TextView close = (TextView)dialog.findViewById(R.id.close);
	    close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
	    });
	    TextView ingredientsView = (TextView)dialog.findViewById(R.id.ingredients_view);
	    ingredientsView.setText(ingredients);
	    TextView nameView = (TextView)dialog.findViewById(R.id.drink_name);
	    nameView.setText(name);
	    //If it's from the home screen, come up with another random drink 
	    ImageView refresh = (ImageView)dialog.findViewById(R.id.rerandomize);
	    if(showRefresh){
		    refresh.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					if(isAllRandom){
						((Home)cont).showAllRandomDrink();
						dialog.dismiss();
					}
					else{
						((Home)cont).showRandomDrink(); 
					}
				} 
		    });
	    }
	    else{
	    	refresh.setVisibility(View.GONE);
	    }
	    TextView instrView = (TextView)dialog.findViewById(R.id.instructions_view);
	    instrView.setText(instr);
	    ImageView twitter = (ImageView)dialog.findViewById(R.id.twitter_logo);
	    //If there's internet, allow twitter interaction, otherwise hide it
	    if(GeneralUtils.testInternet(cont)){
		    twitter.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					TwitterInteraction obj = new TwitterInteraction();
					obj.twitterInitial(cont, name);
				}
		    });
	    }
	    else{
	    	twitter.setVisibility(View.GONE);
	    }
	    tu = (ImageView)dialog.findViewById(R.id.thumbs_up_img);
	    td = (ImageView)dialog.findViewById(R.id.thumbs_down_img);
	    if(rating.equals(Drink.Rating.THUMBSUP)){
	    	tu.setImageResource(R.drawable.thumbsupselected);
	    }
	    if(rating.equals(Drink.Rating.THUMBSDOWN)){ 
	    	td.setImageResource(R.drawable.thumbsdownselected);
	    }
	    tu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isThumbsDown || (!isThumbsUp && !isThumbsDown))
				{
					toggleThumbsUp(update);
					DataBaseReader.toggleThumbs(DataBaseReader.idFromNameAndInst(name, instr), Drink.Rating.THUMBSUP);
				}
				else if(isThumbsUp)
				{
					neutralizeThumbs(update);
					DataBaseReader.toggleThumbs(DataBaseReader.idFromNameAndInst(name, instr), Drink.Rating.THUMBSNULL);
				}
			}
	    });
	    td.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isThumbsUp || (!isThumbsUp && !isThumbsDown))
				{
					toggleThumbsDown(update);
					DataBaseReader.toggleThumbs(DataBaseReader.idFromNameAndInst(name, instr), Drink.Rating.THUMBSDOWN);
				}
				else if(isThumbsDown)
				{
					neutralizeThumbs(update);
					DataBaseReader.toggleThumbs(DataBaseReader.idFromNameAndInst(name, instr), Drink.Rating.THUMBSNULL);
				}
			}
	    });
	}
	
	/**
	 * Sets the thumbs up icon to show and adjusts the original list
	 * Configures the logic as a whole, then passes the baton to the specifics, database management...etc.
	 */
	public static void toggleThumbsUp(boolean update){
		tu.setImageResource(R.drawable.thumbsupselected);
		td.setImageResource(R.drawable.thumbsdown);
		isThumbsUp = true;
		isThumbsDown = false;
		setThumbs(Integer.toString(R.drawable.thumbsup), Drink.Rating.THUMBSUP, update);
	}
	
	/**
	 * Sets the thumbs up icon to show and adjusts the original list
	 * Configures the logic as a whole, then passes the baton to the specifics, database management...etc.
	 */
	public static void toggleThumbsDown(boolean update){
		tu.setImageResource(R.drawable.thumbsup);
		td.setImageResource(R.drawable.thumbsdownselected);
		isThumbsUp = false;
		isThumbsDown = true;
		setThumbs(Integer.toString(R.drawable.thumbsdown), Drink.Rating.THUMBSDOWN, update);
	}
	
	/**
	 * Sets no thumbs selections and adjusts the original list
	 * Configures the logic as a whole, then passes the baton to the specifics, database management...etc.
	 */
	public static void neutralizeThumbs(boolean update){
		tu.setImageResource(R.drawable.thumbsup);
		td.setImageResource(R.drawable.thumbsdown);
		isThumbsUp = false;
		isThumbsDown = false;
		setThumbs("", Drink.Rating.THUMBSNULL, update);
	}
	
	/**
	 * Finds the element in the original dataset and adjusts the image shown as such
	 * It then calls the logic to potentially bump thumbs up, and save the change
	 */
	public static void setThumbs(String status, Rating rating, boolean doUpdateThumbs) {
		
		if(doUpdateThumbs)
		{
			for(HashMap<String, String> datum : Home.dataSet)
			{
				if(datum.get("name").equals(nameDrink) && datum.get("info").equals(ingrDrink) && datum.get("ingr").equals(instrDrink))
				{
					datum.put("img", status);
					Home.adapter.notifyDataSetChanged();
					break;
				}
			}
			updateDrinkState(nameDrink, ingrDrink, instrDrink, rating, Home.drinks);
		}
		else
		{
			updateDrinkState(nameDrink, ingrDrink, instrDrink, rating, Loading.drinks);
		}
	}
	
	/**
	 * Saves whatever change was made to the database and, if there's internet and the thumbs 
	 * was a thumbs up, then it sends that data to the server and adjusts as such
	 */
	public static void updateDrinkState(String name, String ingr, String instr, Rating rating, ArrayList<Drink> drinks){
		Home dummy = new Home();
		//TODO: If can remain (only uses name of drink). Update db method to uniquely identify and update there.
		for(Drink drink : drinks)
		{
			if(drink.getName().equals(name) && dummy.makeIngredientsBetter(drink.getIngredients()).equals(ingr) && drink.getInstructions().equals(instr))
			{
				if(rating.equals(Drink.Rating.THUMBSUP) && GeneralUtils.testInternet(cont)){
					GeneralUtils.bumpEntityValue(drink, cont);
				}
				drink.setRating(rating);
				long id = drink.getId();
				DrinkDatabaseHandler db = new DrinkDatabaseHandler(cont);
				db.setDrinkRating(id, rating);
				break;
			}
		}
	}
}
