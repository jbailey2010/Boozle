package com.bevinisaditch.theinebriator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import com.bevinisaditch.theinebriator.ClassFiles.DrinkPopup;
import com.bevinisaditch.theinebriator.InterfaceAugmentations.ActivitySwipeDetector;
import com.bevinisaditch.theinebriator.InterfaceAugmentations.BounceListView;
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
	public LinearLayout ll;
	public Menu menuObj;
	public MenuItem scrollUp;
	//Remove this later
	public boolean lastList = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ActionBar ab = getActionBar();
		cont = this; 
		ab.setDisplayShowTitleEnabled(false);
		ll = (LinearLayout)findViewById(R.id.home_base);
		ll.setOnTouchListener(new ActivitySwipeDetector((Activity) cont));
		//Remove this later
		setNoResults();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		menuObj = menu;
		scrollUp = (MenuItem)menuObj.findItem(R.id.menu_scroll_up);
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
				//Remove this later
				if(lastList)
				{   
					setNoResults();
				}
				else 
				{
					listviewInit();  
				}
				return true;
			case R.id.menu_scroll_up:
				list.smoothScrollToPosition(0);
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
	}
	
	public void menuInit(View v){
		ISideNavigationCallback sideNavigationCallback = new ISideNavigationCallback() {
		    @Override
		    public void onSideNavigationItemClick(int itemId) {
		    	switch (itemId) {
	            case R.id.menu_random:
	            	
	                break;
	            case R.id.menu_create:
	            	
	            	break;
	            case R.id.menu_added:
	            	
	            	break;
	            case R.id.menu_liked:
	            	
	            	break;
	            case R.id.menu_disliked:
	            	
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

	public void toggleMenu() {
		sideNavigationView.toggleMenu();
		sideListView = (ListView) sideNavigationView.findViewById(R.id.side_navigation_listview);
		sideListView.setOnTouchListener(new ActivitySwipeDetector((Activity) cont));
	}
	
	public void hideMenu(){
		if(sideNavigationView.isShown())
		{
			sideNavigationView.toggleMenu();
		} 
	}
	
	public void setNoResults()
	{
		lastList = false;
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.no_results, ll, false);
		menuInit(res);
		res.setOnTouchListener(new ActivitySwipeDetector((Activity) cont));
		ll.removeAllViews();
		ll.addView(res);
	}
	
	public void listviewInit()
	{
		lastList = true;
		View res = ((Activity) cont).getLayoutInflater().inflate(R.layout.list_results, ll, false);
		menuInit(res);
		list = (BounceListView)res.findViewById(R.id.listview_rankings);
		list.setOnTouchListener(new ActivitySwipeDetector((Activity) cont));
		dataSet = new ArrayList<HashMap<String, String>>();
		for(int i = 0; i < 50; i++)
		{
			HashMap<String, String> datum = new HashMap<String, String>();
			datum.put("name", "Sample Drink " + (i+1));
			datum.put("info", "List of ingredints, list of ingredients, list of ingredients, list of ingredients, list of ingredients");
			datum.put("ingr", "Instructions to make this drink");
			//ADD LOGIC HERE WHEN THE TIME COMES
			datum.put("img", "");
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
				DrinkPopup.drinkPopUpInit(cont, name, ingredients, instr);
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
}
