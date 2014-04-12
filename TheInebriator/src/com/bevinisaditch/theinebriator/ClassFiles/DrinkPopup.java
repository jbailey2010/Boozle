package com.bevinisaditch.theinebriator.ClassFiles;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bevinisaditch.theinebriator.Home;
import com.bevinisaditch.theinebriator.TwitterInteraction;
import com.bevinisaditch.theinebriator.ClassFiles.Drink.Rating;
import com.devingotaswitch.theinebriator.R;
import com.socialize.ActionBarUtils;
import com.socialize.entity.Entity;
import com.socialize.ui.actionbar.ActionBarOptions;

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
	public static void drinkPopUpInit(final Context c, final String name, String ingredients, final String instr, final String url)
	{
		nameDrink = name;
		ingrDrink = ingredients;
		instrDrink = instr;
		cont = c;
		final Dialog dialog = new Dialog(cont, R.style.DialogBackground);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialog.setContentView(R.layout.drink_popup);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    // Your entity key. May be passed as a Bundle parameter to your activity
	    
	 	String entityKey = "http://www.boozle.com/" + name;
	 	Entity entity = Entity.newInstance(entityKey, name);
	 	ActionBarOptions options = new ActionBarOptions();
	 	options.setHideShare(true);
	 	options.setFillColor(Color.parseColor("#272727"));
	 	options.setBackgroundColor(Color.parseColor("#191919"));
	 	options.setAccentColor(Color.parseColor("#ff0000"));
	 	View actionBarWrapped = ActionBarUtils.showActionBar((Activity) cont, R.layout.drink_popup, entity, options);
	 	// Now set the view for your activity to be the wrapped view.
	 	dialog.setContentView(actionBarWrapped);
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
	    nameView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				c.startActivity(browserIntent);
			}
	    	
	    });
	    TextView instrView = (TextView)dialog.findViewById(R.id.instructions_view);
	    instrView.setText(instr);
	    ImageView twitter = (ImageView)dialog.findViewById(R.id.twitter_logo);
	    twitter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				TwitterInteraction obj = new TwitterInteraction();
				obj.twitterInitial(cont, name);
			}
	    });
	    tu = (ImageView)dialog.findViewById(R.id.thumbs_up_img);
	    td = (ImageView)dialog.findViewById(R.id.thumbs_down_img);
	    tu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isThumbsDown || (!isThumbsUp && !isThumbsDown))
				{
					toggleThumbsUp();
					DataBaseReader.toggleThumbs(DataBaseReader.idFromNameAndInst(name, instr), Drink.Rating.THUMBSUP);
				}
				else if(isThumbsUp)
				{
					neutralizeThumbs();
					DataBaseReader.toggleThumbs(DataBaseReader.idFromNameAndInst(name, instr), Drink.Rating.THUMBSNULL);
				}
			}
	    });
	    td.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isThumbsUp || (!isThumbsUp && !isThumbsDown))
				{
					toggleThumbsDown();
					DataBaseReader.toggleThumbs(DataBaseReader.idFromNameAndInst(name, instr), Drink.Rating.THUMBSDOWN);
				}
				else if(isThumbsDown)
				{
					neutralizeThumbs();
					DataBaseReader.toggleThumbs(DataBaseReader.idFromNameAndInst(name, instr), Drink.Rating.THUMBSNULL);
				}
			}
	    });
	}
	
	/**
	 * Sets the thumbs up icon to show and adjusts the original list
	 */
	public static void toggleThumbsUp(){
		tu.setImageResource(R.drawable.thumbsupselected);
		td.setImageResource(R.drawable.thumbsdown);
		isThumbsUp = true;
		isThumbsDown = false;
		setThumbs(Integer.toString(R.drawable.thumbsup), Drink.Rating.THUMBSUP);
	}
	
	/**
	 * Sets the thumbs up icon to show and adjusts the original list
	 */
	public static void toggleThumbsDown(){
		tu.setImageResource(R.drawable.thumbsup);
		td.setImageResource(R.drawable.thumbsdownselected);
		isThumbsUp = false;
		isThumbsDown = true;
		setThumbs(Integer.toString(R.drawable.thumbsdown), Drink.Rating.THUMBSDOWN);
	}
	
	/**
	 * Sets no thumbs selections and adjusts the original list
	 */
	public static void neutralizeThumbs(){
		tu.setImageResource(R.drawable.thumbsup);
		td.setImageResource(R.drawable.thumbsdown);
		isThumbsUp = false;
		isThumbsDown = false;
		setThumbs("", Drink.Rating.THUMBSNULL);
	}
	
	/**
	 * Finds the element in the original dataset and adjusts the image shown as such
	 * NOTE: THIS DOES NOT SAVE THE ADJUSTED SELECTION!
	 */
	public static void setThumbs(String status, Rating rating) {
		for(HashMap<String, String> datum : Home.dataSet)
		{
			if(datum.get("name").equals(nameDrink) && datum.get("info").equals(ingrDrink) && datum.get("ingr").equals(instrDrink))
			{
				datum.put("img", status);
				Home.adapter.notifyDataSetChanged();
				updateDrinkState(nameDrink, ingrDrink, instrDrink, rating);
				break;
			}
		}
	}
	
	public static void updateDrinkState(String name, String ingr, String instr, Rating rating){
		for(Drink drink : Home.drinks)
		{
			if(drink.getName().equals(name) && drink.getIngredients().toString().equals(ingr) && drink.getInstructions().equals(instr))
			{
				drink.setRating(rating);
				break;
			}
		}
	}
}
