package com.bevinisaditch.theinebriator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;









import org.apache.commons.lang3.text.WordUtils;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.bevinisaditch.theinebriator.Database.DrinkDatabaseHandler;
import com.bevinisaditch.theinebriator.InterfaceAugmentations.BounceListView;
import com.bevinisaditch.theinebriator.Utils.GeneralUtils;
import com.devingotaswitch.theinebriator.R;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.socialize.EntityUtils;
import com.socialize.Socialize;
import com.socialize.entity.EntityStats;
import com.socialize.error.SocializeException;
import com.socialize.listener.entity.EntityGetListener;
/**
 * Handles the important parts of the activity, the logic of searching, 
 * the interactions with the data...etc.
 * @author Jeff
 *
 */
public class Home extends Activity {
	public Context cont;
	private BounceListView list;
	SideNavigationView sideNavigationView;
	public static SimpleAdapter adapter;
	public static List<HashMap<String, String>> dataSet;
	public static boolean backToNoResults = false;
	public static boolean backToListResults = false;
	public List<Drink> backup;
	public LinearLayout ll;
	public Menu menuObj; 
	public MenuItem scrollUp;
	public MenuItem clearRes;
	
	/* A singleton instance of the handler, since it is the means to data now */
	private static DrinkDatabaseHandler handler;
	
	
	/**
	 * Sets up the getting of the data from Loading, then shows the default home screen
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		cont = this; 
		handler = Home.getHandler(cont);
		ll = (LinearLayout)findViewById(R.id.home_base);
		if(Loading.drinkNames == null || Loading.drinkNames.size() == 0){
			Intent intent = new Intent(this, Loading.class);
            startActivity(intent);
		}
		setNoResults();
		Socialize.onCreate(this, savedInstanceState);
	}
	
	@Override
    protected void onPause() {
        super.onPause();

        // Call Socialize in onPause
        Socialize.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Call Socialize in onResume
        Socialize.onResume(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Call Socialize in onStart
        Socialize.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Call Socialize in onStop
        Socialize.onStop(this);
    }

    @Override
    protected void onDestroy() {
        // Call Socialize in onDestroy before the activity is destroyed
        Socialize.onDestroy(this);

        super.onDestroy();
    }

	/**
	 * Gets the menu icons handy for displaying them when need be
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
	public boolean onOptionsItemSelected(MenuItem item) {  
		switch (item.getItemId())  {
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
	 * Overridden and handled to make sure the back button only works between parts of this
	 * activity, not actually sending you back to loading.
	 */
	@Override
	public void onBackPressed() {
		if(!backToNoResults && !backToListResults){
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
		}
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
	            	if(dataSet.size() > 0) 	{
	            		showRandomDrink();
	            	}
	            	else {
	            		Toast.makeText(cont, "This requires you to search first", Toast.LENGTH_SHORT).show();
	            	}
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
	            	if(GeneralUtils.testInternet(cont)) {
						DrinkInfo.displayStats(cont);		
					}
					else {
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
	 * A way to access the database object, since it's now the 
	 * means to get the individual data with the list of drinks gone.
	 * 
	 * @param cont - a context to create the object
	 * @return the singleton handler
	 */
	public static DrinkDatabaseHandler getHandler(Context cont){
		if(handler == null){
			handler = new DrinkDatabaseHandler(cont);
		}
		return handler;
	}

	/**
	 * If the menu is gone, show it, if visible, hide it, and make sure the ontouchlistener remains valid
	 */
	public void toggleMenu() {
		sideNavigationView.toggleMenu();
	}

	
	/**
	 * Makes a popup to make sure the user meant to clear results
	 */
	public void confirmClear() {
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
	public void setNoResults() {
		if(backup != null){
			backup.clear();
		}
		if(clearRes != null) {
			clearRes.setVisible(false);
			clearRes.setEnabled(false);
		}
		if(scrollUp != null) {
			scrollUp.setVisible(false);
			scrollUp.setEnabled(false);
		}
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.no_results, ll, false);
		ll.removeAllViews();
		ll.addView(res);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		instantiateButtons(res);
	}
	
	/**
	 * Displays the thumbs up/down-ed drinks 
	 */
	public void listRatingDrinks(Rating thumbStatus, boolean searchFlag) {
		DrinkDatabaseHandler handler = Home.getHandler(cont);
		List<Drink> results = handler.getRatedDrinks(thumbStatus);
		if(results.size() == 0) {
			Toast.makeText(cont, "You have to thumbs up/down a drink to see it here", Toast.LENGTH_LONG).show();
		}
		else {
			listviewInit(results, searchFlag);
		}
	}
	
	/**
	 * Sets up the listview to display the appropriate data in the activity
	 */
	public void listviewInit(List<Drink> results, boolean doBackUp) {
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
		dataSet = new ArrayList<HashMap<String, String>>();
		for(Drink curr: results) {
			HashMap<String, String> datum = new HashMap<String, String>();
			datum.put("name", curr.getName());
			List<Ingredient> ingr = curr.getIngredients();
			String ingrStr = makeIngredientsBetter(ingr);
			datum.put("info", ingrStr);
			datum.put("ingr", curr.getInstructions());
			datum.put("id", String.valueOf(curr.getId()));
			if(curr.getRating() == Rating.THUMBSUP) {
				datum.put("img", Integer.toString(R.drawable.thumbsup));
			}
			else if(curr.getRating() == Rating.THUMBSDOWN) {
				datum.put("img", Integer.toString(R.drawable.thumbsdown));
			}
			else {
				datum.put("img", "");
			}
		    getDrinkInformation(curr);
			dataSet.add(datum);
		}
		if(results.size() == 0){
			HashMap<String, String> datum = new HashMap<String, String>();
			datum.put("name", "No results were found");
			datum.put("info", "");
			datum.put("ingr", "Try again, perhaps using the autocomplete to assist you");
			dataSet.add(datum);
		}
		adapter = new SimpleAdapter(cont, dataSet, 
	    		R.layout.two_line_listview_elem, 
	    		new String[] {"name", "info", "ingr", "id", "img"}, 
	    		new int[] {R.id.text1, 
	    			R.id.text2, R.id.text3, R.id.idHidden, R.id.imageView1});
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
				if(!name.contains("No results")){
					list.setSelection(arg2);
					String ingredients = ((TextView)((RelativeLayout)arg1).findViewById(R.id.text2)).getText().toString();
					String instr = ((TextView)((RelativeLayout)arg1).findViewById(R.id.text3)).getText().toString().split("\n\nTotal Views: ")[0];
					long id = Long.valueOf(((TextView)((RelativeLayout)arg1).findViewById(R.id.idHidden)).getText().toString());
					DrinkPopup.drinkPopUpInit(cont, name, ingredients, instr, id, true, 
							getDrinkRating(name, instr, ingredients), false, false);
				}
			}
	    });
	    list.setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				if(arg1 > 3) {
					scrollUp.setVisible(true);
					scrollUp.setEnabled(true);
				}
				else {
					scrollUp.setVisible(false);
					scrollUp.setEnabled(false);
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}
	    });
	}
	
	/**
	 * Gets the random drink from the search results and displays it
	 */
	public void showRandomDrink() {
		int randIndex = (int) (Math.random() * dataSet.size());
		HashMap<String, String> drinkMap = dataSet.get(randIndex);
		String name = drinkMap.get("name");
		String ingr = drinkMap.get("info");
		String instr = drinkMap.get("ingr");
		long id = Long.valueOf(drinkMap.get("id"));
		DrinkPopup.drinkPopUpInit(cont, name, ingr, instr, id, true, getDrinkRating(name, instr, ingr), true, false);
		list.setSelection(randIndex);
	}
	
	/**
	 * Gets a random drink from all drinks, being called from the home screen
	 */
	public void showAllRandomDrink(){
		int randIndex = (int) (Math.random() * Loading.drinkNames.size());
		handler = Home.getHandler(cont);
		Drink drink = handler.getRandomDrink(randIndex);
		String name = drink.getName();
		String ingr = makeIngredientsBetter(drink.getIngredients());
		String instr = drink.getInstructions();
		long id = drink.getId();
		DrinkPopup.drinkPopUpInit(cont, name, ingr, instr, id, false, getDrinkRating(name, instr, ingr), true, true);
	}
	
	/**
	 * Iterates over drinks to get the rating of a drink
	 */
	public Rating getDrinkRating(String name, String instr, String ingr){
		handler = Home.getHandler(cont);
		return handler.getDrinkRating(handler.getDrinkId(name, instr));
	}
	
	/**
	 * Sets the thumbs up image in an input textview
	 */
	public static void thumbsUpView(RelativeLayout view) {
		String name = ((TextView)(view.findViewById(R.id.text1))).getText().toString();
		String info = ((TextView)(view.findViewById(R.id.text2))).getText().toString();
		for(HashMap<String, String> datum : dataSet) {
			if(datum.get("name").equals(name) && datum.get("info").equals(info)) {
				datum.put("img", Integer.toString(R.drawable.thumbsup));
			}
		}
	}
	
	/**
	 * Sets the thumbs down image in an input textview
	 */
	public static void thumbsDownView(RelativeLayout view) {
		String name = ((TextView)(view.findViewById(R.id.text1))).getText().toString();
		String info = ((TextView)(view.findViewById(R.id.text2))).getText().toString();
		for(HashMap<String, String> datum : dataSet) {
			if(datum.get("name").equals(name) && datum.get("info").equals(info)) {
				datum.put("img", Integer.toString(R.drawable.thumbsdown));
			}
		}
	}
	
	/**
	 * Handles the logic for the buttons. These are the same as the logic of the 
	 * side menu, but they are buttons in the home screen, not menu options
	 */
	public void instantiateButtons(View res) {
		Button random = (Button)res.findViewById(R.id.menu_random);
		random.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
            		showAllRandomDrink();		
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
				if(GeneralUtils.testInternet(cont)) {
					DrinkInfo.displayStats(cont);		
				}
				else {
					Toast.makeText(cont, "This requires an internet connection :(", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
	
	/**
	 * Gets the ingredients into a string format that's pleasant to look at for future
	 * display purposes 
	 */
	public String makeIngredientsBetter(List<Ingredient> ingrList) {
		StringBuilder ingrStr = new StringBuilder(100);
		for(Ingredient iter : ingrList){
			ingrStr.append(iter.toPrettyString() + "\n");
		}
		return ingrStr.toString();
	}
	
	public void getDrinkInformation(final Drink drink){
		if(GeneralUtils.testInternet(this)){
			EntityUtils.getEntity(this, "http://www.boozle.com/" + drink.getId() + "@%" + drink.getName(), new EntityGetListener() {
	    		@Override
	    		public void onError(SocializeException error) {
	    		}
	
				@Override
				public void onGet(com.socialize.entity.Entity result) {
					EntityStats es = result.getEntityStats();
					int views = es.getViews();
					for(HashMap<String, String> iter : dataSet) {
						if(iter.get("name").equals(drink.getName()) && iter.get("id").equals(String.valueOf(drink.getId()))) {
							iter.put("ingr", iter.get("ingr") + "\n\nTotal Views: " + views);
							adapter.notifyDataSetChanged();
							break;
						}
					}
				}
			});
		}
	}
}
