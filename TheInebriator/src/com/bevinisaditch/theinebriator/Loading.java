package com.bevinisaditch.theinebriator;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.devingotaswitch.theinebriator.R;

public class Loading extends Activity {
	public Context cont;
	private TextView header;
	public int counter = 0;
	private Thread t;
	private ImageView img;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		header = (TextView)findViewById(R.id.loading_header);
		ActionBar ab = getActionBar();
		cont = this;
		ab.setDisplayShowTitleEnabled(false);
		handleInitialLoad();
		t = new Thread() {
			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(500);
						runOnUiThread(new Runnable() {
					        @Override
					        public void run() 
					        {
					        	if(counter == 0)
					            {
					        		header.setText("Please wait");
					            }
					            if(counter == 1)
					            {
					            	header.setText("Please wait.");
					            }
					            else if(counter == 2)
					            {
					            	header.setText("Please wait..");
					            }
					            else if(counter == 3)
					            {
					            	counter = -1;
					                header.setText("Please wait...");
					            }
					            counter++;
					        }
						});
					}
				} catch (InterruptedException e) {    }
			}
		};
		t.start();
		
		//DELETE THIS LATER
		img.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				((Loading) cont).sendToHome();
			}
		});
	}

	/**
	 * Auto-generated, unused in this activity
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loading, menu);
		return true;
	}
	
	private void sendToHome()
	{
		header.setText("Stop waiting");
		t.interrupt();
		Intent i2 = new Intent(Loading.this, Home.class);
		startActivity(i2);
		overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
	}
	
	/**
	 * Sets the animation of the gif to work, then manages the loading of the data from file
	 */
	private void handleInitialLoad()
	{
		img = (ImageView)findViewById(R.id.loading_gif);
		img.setBackgroundResource(R.drawable.drink_gif);
		AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
		frameAnimation.start();
	}

}
