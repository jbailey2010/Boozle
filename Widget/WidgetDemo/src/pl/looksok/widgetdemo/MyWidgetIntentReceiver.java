package pl.looksok.widgetdemo;

import pl.looksok.widgetdemo.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyWidgetIntentReceiver extends BroadcastReceiver {

	private static int clickCount = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("pl.looksok.intent.action.CHANGE_PICTURE")){
			updateWidgetPictureAndButtonListener(context);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
		remoteViews.setImageViewResource(R.id.widget_image, getImageToSet());
		
		remoteViews.setOnClickPendingIntent(R.id.widget_button, MyWidgetProvider.buildButtonPendingIntent(context));
		
		MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}

	private int getImageToSet() {
		clickCount++;
		//return clickCount % 2 == 0 ? R.drawable.beer : R.drawable.wordpress_icon;
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
