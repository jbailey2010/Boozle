package com.bevinisaditch.theinebriator.ClassFiles;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.text.WordUtils;

import com.bevinisaditch.theinebriator.Home;
import com.bevinisaditch.theinebriator.SearchEngine.SearchEngine;
import com.bevinisaditch.theinebriator.Utils.GeneralUtils;
import com.devingotaswitch.theinebriator.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Handles the logic of establishing what is to be searched, then passes that along to
 * the search itself and ranker
 * @author Jeff
 *
 */
public class SearchManagement {
	public static List<String> reqIngredients;
	public static List<String> optIngredients;
	public static String searchDrinkName = "";
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

	/**
	 * Handles the configuring of the searching by name popup
	 */
	public static void searchByName(final Context c) {
		final List<String> drinkNames = Home.getDrinkNames();
		for (String name : drinkNames) {
			name = WordUtils.capitalizeFully(name);
		}
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
			}
	    });
	    //Sorts the list of names and builds an adapter with that
	    final List<String> namesSorted = GeneralUtils.sortSingleList(drinkNames);
	    final AutoCompleteTextView input = (AutoCompleteTextView)dialog.findViewById(R.id.search_input_view);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_dropdown_item_1line, namesSorted);    
	    input.setThreshold(2);
	    input.setAdapter(adapter);
	    if(searchDrinkName.length() > 1)
	    {
	    	input.setText(searchDrinkName);
	    }
	    Button submit = (Button) dialog.findViewById(R.id.clear_confirm);
	    submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String possName = (String)input.getText().toString();
				searchDrinkName = possName;
				dialog.dismiss();
				SearchEngine search = new SearchEngine(c);
				search.searchByName(possName);
			}
	    });
	    TextView clear = (TextView)dialog.findViewById(R.id.clear);
	    clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				input.setText("");;
			}
	    });
	    TextView back = (TextView)dialog.findViewById(R.id.back);
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				getSearchType(c);
			}
	    });
	}
	
	/**
	 * Handles the configuring of the searching by ingredients popup
	 */
	public static void searchByIngredients(final Context c) {
		final List<String> drinkNames = Home.getIngredients();
		final Dialog dialog = new Dialog(c, R.style.DialogBackground);		
		if (optIngredients == null) {
			optIngredients = new ArrayList<String>();
		}
		
		if (reqIngredients == null)
		{
			reqIngredients = new ArrayList<String>();
		}
		 
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
			}
	    });
	    //Sorts the list of ingredients alphabetically and sets it as an adapter
	    final AutoCompleteTextView input = (AutoCompleteTextView)dialog.findViewById(R.id.search_input_view);
	    final List<String> namesSorted = GeneralUtils.sortSingleList(drinkNames);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_dropdown_item_1line, namesSorted);    
	    input.setThreshold(2);
	    input.setAdapter(adapter);
	    final Button add = (Button)dialog.findViewById(R.id.search_add);
	    final TextView optional = (TextView)dialog.findViewById(R.id.optional_ingredients);
	    final TextView required = (TextView)dialog.findViewById(R.id.required_ingredients);
	    final Button submit = (Button)dialog.findViewById(R.id.search_submit);
	    TextView clear = (TextView)dialog.findViewById(R.id.clear);
	    clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				optIngredients.clear();
				reqIngredients.clear();
				optional.setText(" ");
				submit.setBackground(c.getResources().getDrawable(R.drawable.btn_grey));
				required.setText(" ");
				input.setText("");
			}
	    });
	    TextView back = (TextView)dialog.findViewById(R.id.back);
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				getSearchType(c);
			}
	    });
	    final RadioButton reqRadio = (RadioButton)dialog.findViewById(R.id.radio_required);
	    //Adds the ingredient in question to the appropriate list
	    add.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String name = input.getText().toString();
				if(reqIngredients.contains(name) || optIngredients.contains(name))
				{
					Toast.makeText(c, "That ingredient is already added", Toast.LENGTH_SHORT).show();
					input.setText("");
				}
				else
				{
					if(reqIngredients.size() == 0 && optIngredients.size() == 0)
					{
						submit.setBackground(c.getResources().getDrawable(R.drawable.btn_blue));
					}
					if(reqRadio.isChecked())
					{
						reqIngredients.add(name);
					}
					else
					{
						optIngredients.add(name);
					}
					updateTextViews(required, reqIngredients, "Required Ingredients:");
					updateTextViews(optional, optIngredients, "Optional Ingredients:");
					input.setText("");
				}
			}
	    });
	    submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(optIngredients.size() > 0 || reqIngredients.size() > 0)
				{
					dialog.dismiss();
					SearchEngine search = new SearchEngine(c);
					search.searchByIngredient((ArrayList<String>) optIngredients, (ArrayList<String>) reqIngredients);
				}
			}
	    });
	    if(optIngredients.size() == 0 && reqIngredients.size() == 0)
	    {
		    optIngredients = new ArrayList<String>();
			reqIngredients = new ArrayList<String>();
	    }
	    else
	    {
	    	updateTextViews(required, reqIngredients, "Required Ingredients:");
			updateTextViews(optional, optIngredients, "Optional Ingredients:");
	    }
	}
	
	/**
	 * Updates the ingredient list as things are changed
	 */
	public static void updateTextViews(TextView view, List<String>ingrList, String header)
	{
		StringBuilder ingrBuilder = new StringBuilder(100);
		if(ingrList.size() > 0)
		{
			ingrBuilder.append(header + "\n");
		}
		for(String ingr : ingrList)
		{
			ingrBuilder.append(ingr + "\n");
		}
		view.setText(ingrBuilder.toString());
	}
}
