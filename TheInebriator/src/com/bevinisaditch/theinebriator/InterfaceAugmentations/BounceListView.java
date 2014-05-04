package com.bevinisaditch.theinebriator.InterfaceAugmentations;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

/**
 * Adds bouncing of the listview
 * @author Jeff
 *
 */
public class BounceListView extends ListView {
	 private static final int MAX_Y_OVERSCROLL_DISTANCE = 250;
	 private Context mContext;
	 private int mMaxYOverscrollDistance;

	 /**
	  * Constructor only needing a context
	  */
	 public BounceListView(Context context) {
		  super(context);
		  mContext = context;
		  initBounceListView();
	 }

	 /**
	  * Constructor for if the list is partially styled
	  */
	 public BounceListView(Context context, AttributeSet attrs) {
		  super(context, attrs);
		  mContext = context;
		  initBounceListView();
	 }

	 /**
	  * Constructor for if the list is specifically styled
	  */
	 public BounceListView(Context context, AttributeSet attrs, int defStyle) {
		  super(context, attrs, defStyle);
		  mContext = context;
		  initBounceListView();
	 }

	 /**
	  * Establishes how far to overscroll
	  */
	 private void initBounceListView() {
		  final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		  final float density = metrics.density;
		  mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
	 }
	
	 /**
	  * Uses the native overScrollBy method to replace the 0 overscroll max that's there thanks to an apple lawsuit with the 
	  * dp version of my arbitrarily chosen max.
	  */
	 @Override
	 protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY,
			 							int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) 
	 {
		  return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
	 }

}