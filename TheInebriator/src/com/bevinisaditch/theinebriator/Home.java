package com.bevinisaditch.theinebriator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ActionBar ab = getActionBar();
		cont = this;
		ab.setDisplayShowTitleEnabled(false);
		list = (BounceListView)findViewById(R.id.listview_rankings);
		list.setOnTouchListener(new ActivitySwipeDetector((Activity) cont));
		menuInit();
		listviewInit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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
	
	public void menuInit(){
		ISideNavigationCallback sideNavigationCallback = new ISideNavigationCallback() {
		    @Override
		    public void onSideNavigationItemClick(int itemId) {
		    	switch (itemId) {
	            case R.id.menu_1:
	            	
	                break;
	            case R.id.menu_2:
	            	
	                break;
	            default:
	                return;
		    	}
		    }
		};
		sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
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
	
	public void listviewInit()
	{
		list = (BounceListView)findViewById(R.id.listview_rankings);
		List<HashMap<String, String>> dataSet = new ArrayList<HashMap<String, String>>();
		for(int i = 0; i < 50; i++)
		{
			HashMap<String, String> datum = new HashMap<String, String>();
			datum.put("name", "Sample Drink " + (i+1));
			datum.put("info", "List of ingredints, list of ingredients, list of ingredients, list of ingredients, list of ingredients");
			dataSet.add(datum);
		}
		final SimpleAdapter adapter = new SimpleAdapter(cont, dataSet, 
	    		R.layout.two_line_listview_elem, 
	    		new String[] {"name", "info"}, 
	    		new int[] {R.id.text1, 
	    			R.id.text2});
	    list.setAdapter(adapter);
	}

}
