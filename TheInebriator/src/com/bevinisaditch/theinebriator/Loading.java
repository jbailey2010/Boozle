package com.bevinisaditch.theinebriator;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.Menu;
import android.widget.ImageView;

public class Loading extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		handleInitialLoad();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loading, menu);
		return true;
	}
	
	
	public void handleInitialLoad()
	{
		ImageView img = (ImageView)findViewById(R.id.loading_gif);
		img.setBackgroundResource(R.drawable.drink_gif);
		AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
		frameAnimation.start();
	}

}
