package pl.looksok.widgetdemo;

import pl.looksok.widgetdemo.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyWidgetIntentReceiver extends BroadcastReceiver {

	private static int clickCount = 0;
	private static int drinkCount = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("pl.looksok.intent.action.CHANGE_PICTURE")){
			updateWidgetPictureAndButtonListener(context);
		}
	}
/*
	Function to set default images and then call updater function

*/
	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
		remoteViews.setImageViewResource(R.id.widget_image, getImageToSet());
		remoteViews.setImageViewResource(R.id.count_image, getNumToSet());
		remoteViews.setOnClickPendingIntent(R.id.widget_button, MyWidgetProvider.buildButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.drink_button, MyWidgetProvider.buildButtonPendingIntent(context));
		
		MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
/*
	Function to set the drink counter. Called by ImageViewSource
*/
	private int getNumToSet() {

		drinkCount++;
		if (drinkCount == 1)
			return R.drawable.one;
		if (drinkCount == 2)
			return R.drawable.two;
		if (drinkCount == 3)
			return R.drawable.three;
		if (drinkCount == 4)
			return R.drawable.four;
		else {
			
			drinkCount = 1;
			return R.drawable.one;
		}
		
		
	/*
	Sets drink image based on click
	*/	
		
		
	}	
	private int getImageToSet() {
		clickCount++;
	
		if (clickCount == 1)
					return R.drawable.bm;
		if (clickCount == 2)
					return R.drawable.man;
		if (clickCount == 3)
					return R.drawable.martini;
		
		if (clickCount==4)
					return R.drawable.lemar;
		if (clickCount==5)
					return R.drawable.pisco;
		
		if(clickCount==6)
					return R.drawable.cocktail;
		if(clickCount==7)
					return R.drawable.white;
		
		if(clickCount == 8)
					return R.drawable.mint;
		if(clickCount == 9)
					return R.drawable.contin;
		
		if(clickCount == 10)
					return R.drawable.cors;
							
		else {
				clickCount =1;
				return R.drawable.bm;
		}
	}
	
}
