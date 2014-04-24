package com.bevinisaditch.theinebriator;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.Drink.Rating;
import com.bevinisaditch.theinebriator.ClassFiles.DrinkInfo;
import com.bevinisaditch.theinebriator.ClassFiles.DrinkPopup;
import com.bevinisaditch.theinebriator.ClassFiles.Ingredient;
import com.bevinisaditch.theinebriator.ClassFiles.SearchManagement;
import com.bevinisaditch.theinebriator.InterfaceAugmentations.ActivitySwipeDetector;
import com.bevinisaditch.theinebriator.InterfaceAugmentations.BounceListView;
import com.bevinisaditch.theinebriator.Utils.GeneralUtils;
import com.devingotaswitch.theinebriator.R;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;

public class Home extends Activity {
	public Context cont;
	private BounceListView list;
	SideNavigationView sideNavigationView;
	private ListView sideListView; 
	public static SimpleAdapter adapter;
	public static List<HashMap<String, String>> dataSet;
	public static boolean backToNoResults = false;
	public static boolean backToListResults = false;
	public List<Drink> backup;
	public LinearLayout ll;
	public Menu menuObj;
	public MenuItem scrollUp;
	public MenuItem clearRes;
	public static ArrayList<Drink> drinks;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ActionBar ab = getActionBar();
		cont = this; 
		ab.setDisplayShowTitleEnabled(false);
		ll = (LinearLayout)findViewById(R.id.home_base);
		ll.setOnTouchListener(new ActivitySwipeDetector((Activity) cont));
		drinks = Loading.drinks;
		System.out.println(drinks.size());
		setNoResults();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		menuObj = menu;
		scrollUp = (MenuItem)menuObj.findItem(R.id.menu_scroll_up);
		clearRes = (MenuItem)menuObj.findItem(R.id.menu_clear_results);
		return true;
	}
	 
	/**
	 * Runs the on selection part of the menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{  
		switch (item.getItemId()) 
		{
			case android.R.id.home:
		        toggleMenu(); 
		        return true; 
			case R.id.menu_search:
				SearchManagement.getSearchType(cont);
				return true;
			case R.id.menu_scroll_up:
				list.smoothScrollToPosition(0);
				return true;
			case R.id.menu_clear_results:
				confirmClear();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Overridden and empty because you shouldn't need to go back to loading...ever.
	 */
	@Override
	public void onBackPressed() {
		if(backToNoResults){
			setNoResults();
			backToNoResults = false;
			backToListResults = false;
		}
		if(backToListResults){
			listviewInit(backup, false);
			backToNoResults = true;
			backToListResults = false;
		}
	}
	
	/**
	 * Sets up the various onclicks and configurations for the side menu
	 * and it's respective options
	 */
	public void menuInit(View v){
		ISideNavigationCallback sideNavigationCallback = new ISideNavigationCallback() {
		    @Override
		    public void onSideNavigationItemClick(int itemId) {
		    	switch (itemId) {
		    	case R.id.menu_home:
		    		setNoResults();
		    		break;
	            case R.id.menu_random:
	            	if(dataSet.size() > 0)
	            	{
	            		showRandomDrink();
	            	}
	            	else
	            	{
	            		Toast.makeText(cont, "This requires you to search first", Toast.LENGTH_SHORT).show();
	            	}
	                break;
	            case R.id.menu_create:
	            	
	            	break;
	            case R.id.menu_added:
	            	
	            	break;
	            case R.id.menu_liked:
	            	listRatingDrinks(Rating.THUMBSUP, false);
	            	backToListResults = true;
	            	backToNoResults = false;
	            	break;
	            case R.id.menu_disliked:
	            	listRatingDrinks(Rating.THUMBSDOWN, false);
	            	backToListResults = true;
	            	backToNoResults = false;
	            	break;
	            case R.id.menu_popularity:
	            	if(GeneralUtils.testInternet(cont))
					{
						DrinkInfo.displayStats(cont);		
					}
					else
					{
						Toast.makeText(cont, "This requires an internet connection :(", Toast.LENGTH_SHORT).show();
					}
	            	break;
	            default:
	                return;
		    	}
		    }
		};
		sideNavigationView = (SideNavigationView) v.findViewById(R.id.side_navigation_view);
	    sideNavigationView.setMenuItems(R.menu.side_menu_options);
	    sideNavigationView.setMenuClickCallback(sideNavigationCallback);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * If the menu is gone, show it, if visible, hide it, and make sure the ontouchlistener remains valid
	 */
	public void toggleMenu() {
		sideNavigationView.toggleMenu();
		sideListView = (ListView) sideNavigationView.findViewById(R.id.side_navigation_listview);
		sideListView.setOnTouchListener(new ActivitySwipeDetector((Activity) cont));
	}

	
	/**
	 * Makes a popup to make sure the user meant to clear results
	 */
	public void confirmClear()
	{
		final Dialog dialog = new Dialog(cont, R.style.DialogBackground);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.confirm_clear_popup);
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
	    Button confirm = (Button)dialog.findViewById(R.id.clear_confirm);
	    confirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				dataSet.clear();
				setNoResults();
			}
	    });
	}
	
	/**
	 * Sets the display to be the no results screen, prompting the user to do stuff
	 */
	public void setNoResults()
	{
		if(backup != null){
			backup.clear();
		}
		if(clearRes != null)
		{
			clearRes.setVisible(false);
			clearRes.setEnabled(false);
		}
		if(scrollUp != null)
		{
			scrollUp.setVisible(false);
			scrollUp.setEnabled(false);
		}
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.no_results, ll, false);
		//menuInit(res);
		res.setOnTouchListener(new ActivitySwipeDetector((Activity) cont));
		ll.removeAllViews();
		ll.addView(res);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		instantiateButtons(res);
	}
	
	/**
	 * Displays the thumbs up/down-ed drinks 
	 */
	public void listRatingDrinks(Rating thumbStatus, boolean searchFlag)
	{
		List<Drink> results = new ArrayList<Drink>();
		for(Drink drink : drinks)
		{
			if(drink.getRating() == thumbStatus)
			{
				results.add(drink);
			}
		}
		if(results.size() == 0)
		{
			Toast.makeText(cont, "You have to thumbs up/down a drink to see it here", Toast.LENGTH_LONG).show();
		}
		else
		{
			listviewInit(results, searchFlag);
		}
	}
	
	/**
	 * Sets up the listview to display the appropriate data in the activity
	 */
	public void listviewInit(List<Drink> results, boolean doBackUp)
	{
		
		if(doBackUp){
			backup = new ArrayList<Drink>();
			for(Drink drink : results){
				backup.add(drink);
			}
		}
		clearRes.setVisible(true);
		clearRes.setEnabled(true);
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.list_results, ll, false);
		menuInit(res);
		list = (BounceListView)res.findViewById(R.id.listview_rankings);
		list.setOnTouchListener(new ActivitySwipeDetector((Activity) cont));
		dataSet = new ArrayList<HashMap<String, String>>();
		for(Drink curr: results)
		{
			HashMap<String, String> datum = new HashMap<String, String>();
			datum.put("name", curr.getName());
			List<Ingredient> ingr = curr.getIngredients();
			String ingrStr = makeIngredientsBetter(ingr);
			datum.put("info", ingrStr);
			datum.put("ingr", curr.getInstructions());
			if(curr.getRating() == Rating.THUMBSUP)
			{
				datum.put("img", Integer.toString(R.drawable.thumbsup));
			}
			else if(curr.getRating() == Rating.THUMBSDOWN)
			{
				datum.put("img", Integer.toString(R.drawable.thumbsdown));
			}
			else
			{
				datum.put("img", "");
			}
			dataSet.add(datum);
		}
		adapter = new SimpleAdapter(cont, dataSet, 
	    		R.layout.two_line_listview_elem, 
	    		new String[] {"name", "info", "ingr", "img"}, 
	    		new int[] {R.id.text1, 
	    			R.id.text2, R.id.text3, R.id.imageView1});
	    list.setAdapter(adapter);
	    configListResults();
	    ll.removeAllViews();
	    ll.addView(res);
	}
	 
	/**
	 * Configures the drink pop up on click and the enabling of the scroll menu option on scroll
	 */
	public void configListResults()
	{
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String name = ((TextView)((RelativeLayout)arg1).findViewById(R.id.text1)).getText().toString();
				String ingredients = ((TextView)((RelativeLayout)arg1).findViewById(R.id.text2)).getText().toString();
				String instr = ((TextView)((RelativeLayout)arg1).findViewById(R.id.text3)).getText().toString();
				
				list.setSelection(arg2);
				DrinkPopup.drinkPopUpInit(cont, name, ingredients, instr, getDrinkUrl(name, instr, ingredients), true, getDrinkRating(name, instr, ingredients), false, false);
			}
	    });
	    list.setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				if(arg1 > 3)
				{
					scrollUp.setVisible(true);
					scrollUp.setEnabled(true);
				}
				else
				{
					scrollUp.setVisible(false);
					scrollUp.setEnabled(false);
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				//Sincerely don't care
			}
	    });
	}
	
	/**
	 * Gets the random drink from the search results and displays it
	 */
	public void showRandomDrink()
	{
		int randIndex = (int) (Math.random() * dataSet.size());
		HashMap<String, String> drinkMap = dataSet.get(randIndex);
		String name = drinkMap.get("name");
		String ingr = drinkMap.get("info");
		String instr = drinkMap.get("ingr");
		DrinkPopup.drinkPopUpInit(cont, name, ingr, instr, getDrinkUrl(name, instr, ingr), true, getDrinkRating(name, instr, ingr), true, false);
		list.setSelection(randIndex);
	}
	
	public void showAllRandomDrink(){
		int randIndex = (int) (Math.random() * drinks.size());
		Drink drink = drinks.get(randIndex);
		String name = drink.getName();
		String ingr = makeIngredientsBetter(drink.getIngredients());
		String instr = drink.getInstructions();
		DrinkPopup.drinkPopUpInit(cont, name, ingr, instr, getDrinkUrl(name, instr, ingr), false, getDrinkRating(name, instr, ingr), true, true);
		//list.setSelection(randIndex);
		
	}
	
	/**
	 * Iterates over drinks to get the url of a drink
	 */
	public String getDrinkUrl(String name, String instr, String ingr)
	{
		for(Drink dr : drinks)
		{
			if(dr.getName().equals(name) && dr.getInstructions().equals(instr) && makeIngredientsBetter(dr.getIngredients()).equals(ingr))
			{
				return dr.getUrl();
			}
		}
		return "";
	}
	
	public Rating getDrinkRating(String name, String instr, String ingr){
		for(Drink dr : drinks)
		{
			if(dr.getName().equals(name) && dr.getInstructions().equals(instr) && makeIngredientsBetter(dr.getIngredients()).equals(ingr))
			{
				return dr.getRating();
			}
		}
		return Drink.Rating.THUMBSNULL;
	}
	
	/**
	 * Sets the thumbs up image in an input textview
	 */
	public static void thumbsUpView(RelativeLayout view)
	{
		String name = ((TextView)(view.findViewById(R.id.text1))).getText().toString();
		String info = ((TextView)(view.findViewById(R.id.text2))).getText().toString();
		for(HashMap<String, String> datum : dataSet)
		{
			if(datum.get("name").equals(name) && datum.get("info").equals(info))
			{
				datum.put("img", Integer.toString(R.drawable.thumbsup));
			}
		}
	}
	
	/**
	 * Sets the thumbs down image in an input textview
	 */
	public static void thumbsDownView(RelativeLayout view)
	{
		String name = ((TextView)(view.findViewById(R.id.text1))).getText().toString();
		String info = ((TextView)(view.findViewById(R.id.text2))).getText().toString();
		for(HashMap<String, String> datum : dataSet)
		{
			if(datum.get("name").equals(name) && datum.get("info").equals(info))
			{
				datum.put("img", Integer.toString(R.drawable.thumbsdown));
			}
		}
	}
	
	public static List<String> getDrinkNames()
	{
		List<String> drinkNames = new ArrayList<String>();
		for(Drink drink : drinks)
		{
			drinkNames.add(drink.getName());
		}
		return drinkNames;
	}
	
	public static List<String> getIngredients()
	{
		HashSet<String> drinkSet = new HashSet<String>();
		List<String> drinkNames = new ArrayList<String>();
		for (Drink drink : drinks) {
			for (Ingredient ingr : drink.getIngredients()) {
				if(!drinkSet.contains(ingr.getName())){ 
					drinkSet.add(ingr.getName());
				}
			} 
		}
		for(String ingr : drinkSet){
			drinkNames.add(ingr);
		}
		return drinkNames; 
	}
	public void instantiateButtons(View res)
	{
		Button random = (Button)res.findViewById(R.id.menu_random);
		random.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
            		showAllRandomDrink();		
			}
			
		});
		Button create = (Button)res.findViewById(R.id.menu_create);
		create.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
			}
			
		});
		Button added = (Button)res.findViewById(R.id.menu_added);
		added.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
            				
			}
			
		});
		Button liked = (Button)res.findViewById(R.id.menu_liked);
		liked.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				backToListResults = false;
            	backToNoResults = true;
            	listRatingDrinks(Rating.THUMBSUP, false);	
			}
			 
		});
		Button disliked = (Button)res.findViewById(R.id.menu_disliked);
		disliked.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				backToListResults = false;
            	backToNoResults = true;
            	listRatingDrinks(Rating.THUMBSDOWN, false);		
			}
			
		});  
		Button popularity = (Button)res.findViewById(R.id.menu_popularity);
		popularity.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(GeneralUtils.testInternet(cont))
				{
					DrinkInfo.displayStats(cont);		
				}
				else
				{
					Toast.makeText(cont, "This requires an internet connection :(", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
	
	public String makeIngredientsBetter(List<Ingredient> ingrList)
	{
		StringBuilder ingrStr = new StringBuilder(100);
		for(Ingredient iter : ingrList){
			ingrStr.append(iter.toPrettyString() + "\n");
		}
		return ingrStr.toString();
	}
}
