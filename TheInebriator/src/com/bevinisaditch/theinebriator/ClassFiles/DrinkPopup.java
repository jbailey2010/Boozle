package com.bevinisaditch.theinebriator.ClassFiles;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devingotaswitch.theinebriator.R;

public class DrinkPopup {
	public static Context cont;
	public static ImageView tu;
	public static ImageView td;
	
	public static void drinkPopUpInit(Context c, String name, String ingredients)
	{
		cont = c;
		final Dialog dialog = new Dialog(cont, R.style.DialogBackground);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.drink_popup);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    TextView close = (TextView)dialog.findViewById(R.id.close);
	    close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
	    });
	    TextView ingredientsView = (TextView)dialog.findViewById(R.id.ingredients_view);
	    ingredientsView.setText(ingredients);
	    TextView nameView = (TextView)dialog.findViewById(R.id.drink_name);
	    nameView.setText(name);
	    tu = (ImageView)dialog.findViewById(R.id.thumbs_up_img);
	    td = (ImageView)dialog.findViewById(R.id.thumbs_down_img);
	    tu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				toggleThumbsUp();
			}
	    });
	    td.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				toggleThumbsDown();
			}
	    });
	}
	
	public static void toggleThumbsUp(){
		tu.setImageResource(R.drawable.thumbsupselected);
		td.setImageResource(R.drawable.thumbsdown);
	}
	
	public static void toggleThumbsDown(){
		tu.setImageResource(R.drawable.thumbsup);
		td.setImageResource(R.drawable.thumbsdownselected);
	}
}
