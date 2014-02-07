package com.bevinisaditch.theinebriator.InterfaceAugmentations;
import com.bevinisaditch.theinebriator.Home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ProgressBar;

public class ActivitySwipeDetector implements View.OnTouchListener {
	 private Activity activity;
	 public String origin;
	 static final int MIN_DISTANCE = 140;
	 private float downX, upX;
	 public boolean isFarLeft;

	 public ActivitySwipeDetector(final Activity activity) { 
		  this.activity = activity;
	 }
	
	 public void onLeftToRightSwipe(){
	  	((Home)activity).toggleMenu();
	 }

	 public boolean onTouch(View v, MotionEvent event) {
		 float deltaX = 0;
		 switch(event.getAction()){
			  case MotionEvent.ACTION_DOWN: {
				   downX = event.getX();
				   if(downX < 10.0)
				   {
					   isFarLeft = true;
				   }
				   else
				   {
					   isFarLeft = false;
				   }
				   return true;
			  }
			  case MotionEvent.ACTION_UP: {
				   upX = event.getX();
				   deltaX = downX - upX;
				   if(Math.abs(deltaX) > MIN_DISTANCE){
					   if(deltaX < 0 && isFarLeft) { this.onLeftToRightSwipe(); return true; }
				   } 
			  }
		 }
		 return false;
	 }
}