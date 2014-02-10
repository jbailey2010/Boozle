package com.bevinisaditch.theinebriator;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.ListView;

import com.bevinisaditch.theinebriator.InterfaceAugmentations.ActivitySwipeDetector;
import com.bevinisaditch.theinebriator.InterfaceAugmentations.BounceListView;
import com.devingotaswitch.theinebriator.R;

public class Home extends Activity {
	public Context cont;
	private BounceListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ActionBar ab = getActionBar();
		cont = this;
		ab.setDisplayShowTitleEnabled(false);
		list = (BounceListView)findViewById(R.id.listview_rankings);
		list.setOnTouchListener(new ActivitySwipeDetector((Activity) cont));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	/**
	 * Overridden and empty because you shouldn't need to go back to loading...ever.
	 */
	@Override
	public void onBackPressed() {
	}

	public void toggleMenu() {
		//When side bar is here, toggle the menu
	}

}
