package com.bevinisaditch.theinebriator.ClassFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bevinisaditch.theinebriator.Home;
import com.devingotaswitch.theinebriator.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchManagement {
	
	/**
	 * Creates the pop up that will get input from the user to decide what type of search
	 * is to be done
	 */
	public static void getSearchType(final Context c)
	{
		final Dialog dialog = new Dialog(c, R.style.DialogBackground);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.search_selection_popup);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    TextView close = (TextView)dialog.findViewById(R.id.close);
	    close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
	    });
	    final Spinner typeSpinner = (Spinner)dialog.findViewById(R.id.search_selection_spinner);
	    List<String> spinnerList = new ArrayList<String>();
		spinnerList.add("Search Drinks by Name");
		spinnerList.add("Search Drinks by Ingredients");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(c, 
				android.R.layout.simple_spinner_dropdown_item, spinnerList);
		typeSpinner.setAdapter(spinnerArrayAdapter);
		Button submit = (Button)dialog.findViewById(R.id.search_selection_submit);
		submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String selection = typeSpinner.getSelectedItem().toString();
				if(selection.contains("Name"))
				{
					searchByName(c);
				}
				else if(selection.contains("Ingredients"))
				{
					searchByIngredients(c);
				}
				dialog.dismiss();
			}
		});
	}

	public static void searchByName(final Context c) {
		List<String> drinkNames = Home.getDrinkNames();
		final Dialog dialog = new Dialog(c, R.style.DialogBackground);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.search_name_popup);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    TextView close = (TextView)dialog.findViewById(R.id.close);
	    close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				//Remove this
				((Home)((Activity)c)).listviewInit();
			}
	    });
	    AutoCompleteTextView input = (AutoCompleteTextView)dialog.findViewById(R.id.search_input_view);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_dropdown_item_1line, drinkNames);    
	    input.setThreshold(2);
	    input.setAdapter(adapter);
	}
	
	public static void searchByIngredients(final Context c) {
		List<String> drinkNames = Home.getIngredients();
		final Dialog dialog = new Dialog(c, R.style.DialogBackground);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.search_ingredients_popup);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    TextView close = (TextView)dialog.findViewById(R.id.close);
	    close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				//Remove this
				((Home)((Activity)c)).listviewInit();
			}
	    });
	    AutoCompleteTextView input = (AutoCompleteTextView)dialog.findViewById(R.id.search_input_view);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_dropdown_item_1line, drinkNames);    
	    input.setThreshold(2);
	    input.setAdapter(adapter);
	}
}
