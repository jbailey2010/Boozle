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
	public static boolean isThumbsUp;
	public static boolean isThumbsDown;
	
	public static void drinkPopUpInit(Context c, String name, String ingredients, String instr)
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
	    TextView instrView = (TextView)dialog.findViewById(R.id.instructions_view);
	    instrView.setText(instr);
	    tu = (ImageView)dialog.findViewById(R.id.thumbs_up_img);
	    td = (ImageView)dialog.findViewById(R.id.thumbs_down_img);
	    tu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isThumbsDown || (!isThumbsUp && !isThumbsDown))
				{
					toggleThumbsUp();
				}
				else if(isThumbsUp)
				{
					neutralizeThumbs();
				}
			}
	    });
	    td.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isThumbsUp || (!isThumbsUp && !isThumbsDown))
				{
					toggleThumbsDown();
				}
				else if(isThumbsDown)
				{
					neutralizeThumbs();
				}
			}
	    });
	}
	
	public static void toggleThumbsUp(){
		tu.setImageResource(R.drawable.thumbsupselected);
		td.setImageResource(R.drawable.thumbsdown);
		isThumbsUp = true;
		isThumbsDown = false;
	}
	
	public static void toggleThumbsDown(){
		tu.setImageResource(R.drawable.thumbsup);
		td.setImageResource(R.drawable.thumbsdownselected);
		isThumbsUp = false;
		isThumbsDown = true;
	}
	
	public static void neutralizeThumbs(){
		tu.setImageResource(R.drawable.thumbsup);
		td.setImageResource(R.drawable.thumbsdown);
		isThumbsUp = false;
		isThumbsDown = false;
	}
}
