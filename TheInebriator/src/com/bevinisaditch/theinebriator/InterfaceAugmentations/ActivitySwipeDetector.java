package com.bevinisaditch.theinebriator.InterfaceAugmentations;
import com.bevinisaditch.theinebriator.Home;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ProgressBar;
/**
 * The class that extends native android ontouchlistener to handle swiping the menu to visibility
 * @author Jeff
 *
 */
public class ActivitySwipeDetector implements View.OnTouchListener {
	 private Activity activity;
	 public String origin;
	 static final int MIN_DISTANCE = 140;
	 private float downX, upX;
	 public boolean isFarLeft;

	 /**
	  * Keeps the context handy for the sake of working back to the home activity 
	  */
	 public ActivitySwipeDetector(final Activity activity) { 
		  this.activity = activity;
	 }
	
	 /**
	  * The swipe was registered, so toggle the home menu
	  */
	 public void onLeftToRightSwipe(){
	  	((Home)activity).toggleMenu();
	 }  

	 /**
	  * Reads the event itself to try and determine if it qualifies for a menu toggle.
	  */
	 public boolean onTouch(View v, MotionEvent event) {
		 float deltaX = 0;
		 switch(event.getAction()){
			  case MotionEvent.ACTION_DOWN: { 
				   downX = event.getX();
				   if(downX < 50.0) {
					   isFarLeft = true; 
				   }
				   else {
					   isFarLeft = false;
				   }
				   return false;
			  }
			  case MotionEvent.ACTION_UP: {
				   upX = event.getX();
				   deltaX = downX - upX;
				   if(Math.abs(deltaX) > MIN_DISTANCE){
					   if(deltaX < 0 && isFarLeft) { 
						   this.onLeftToRightSwipe(); 
						   return false; 
					   }
				   } 
				   
			  }
		 }
		 return false;
	 }
}