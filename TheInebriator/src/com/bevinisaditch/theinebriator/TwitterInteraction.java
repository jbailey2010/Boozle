package com.bevinisaditch.theinebriator;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.devingotaswitch.theinebriator.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A class to handle the linking with twitter 
 * @author Jeff
 *
 */
public class TwitterInteraction {
	String validURL = "";
	int pin = -1;
	public Twitter twitter;
	public static Twitter userTwitter;
	RequestToken requestToken;
	AccessToken accessToken = null;
	static AccessToken userToken = null;
	public String drinkName;
	
	/**
	 * Figures out if the linking has been done. If not, initiate that, otherwise 
	 * log the user in.
	 */
	public void twitterInitial(Context cont, String name) {
		drinkName = name;
		long check = readUseID(cont);
		//Not yet set
		if(check == -1)
		{
			TwitterInteraction obj = new TwitterInteraction();
		    TwitterConnection task = obj.new TwitterConnection((Activity)cont);
		    task.execute(cont);
		}
		else //it IS set, so call a function to 'log in' the user'
		{
			logInUser(cont);
		}
	}
	
	/**
	 * Logs in the user and makes a pop up asking them what they'd like to do
	 */
	public void logInUser(final Context cont)
	{
		String token = readToken(cont);
		String tokenSecret = readTokenSecret(cont);
		userToken = new AccessToken(token, tokenSecret);
		userTwitter = TwitterFactory.getSingleton();
		try
		{
			userTwitter.setOAuthConsumer("XtFI13o9m4eY3dkYDNgpVg",
	        		"XqgGCJ9HRdZI2x3xW1o69fLIDZP7WbFTAQyh1qxoCco");
			userTwitter.setOAuthAccessToken(userToken);
		}
		catch(IllegalStateException e)
		{
		}
		ParseTwitterSearch obj = new ParseTwitterSearch(cont, this, drinkName);
		obj.execute(cont);
	}
	
	/**
	 * Gets the validation URL from twitter
	 * @author Jeff
	 *
	 */
	public class TwitterConnection extends AsyncTask<Object, Void, Twitter> 
	{
		ProgressDialog pdia;
		Activity act;
	    public TwitterConnection(Activity activity) 
	    {
	        pdia = new ProgressDialog(activity);
	        pdia.setCancelable(false);
	        act = activity;
	    }

		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia.setMessage("Please wait, fetching the URL...");
		        pdia.show();    
		}

		@Override
		protected void onPostExecute(Twitter result){
		   super.onPostExecute(result);
		   pdia.dismiss();
		   if(result != null)
		   {
			   handleURL(act, result);
		   }
		   else
		   {
			   Toast.makeText(act, "Please kill the app and re-open it to re-attempt to connect to twitter", Toast.LENGTH_LONG).show();
		   }
		}

	    @Override
	    protected Twitter doInBackground(Object... data) 
	    {
	    	final Context cont = (Context)data[0];
	    	Twitter twitter = TwitterFactory.getSingleton();
	    	try{
		        twitter.setOAuthConsumer("XtFI13o9m4eY3dkYDNgpVg",
		        		"XqgGCJ9HRdZI2x3xW1o69fLIDZP7WbFTAQyh1qxoCco");
	    	}catch(IllegalStateException ise)
			{
	    		return null;
			}
	        try {
				requestToken = twitter.getOAuthRequestToken();
		        accessToken = null;
		        validURL = requestToken.getAuthorizationURL();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
	        return twitter;
	    }
	}
	
	/**
	 * Creates a dialog to get the user to validate it, then enter the pin
	 * @param cont
	 */
	public void handleURL(final Activity cont, Twitter twit)
	{
		twitter = twit;
		final Dialog dialog = new Dialog(cont, R.style.DialogBackground);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.twitter_login);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    Button validate = (Button)dialog.findViewById(R.id.twitter_confirm_go);
	    validate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(validURL));
				try{
					cont.startActivity(i); 
				} catch(Exception e){
					Toast.makeText(cont, "An error occurred. Do you have an internet connection?", Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
				handlePin(cont, twitter);
			}
	    });
	    dialog.setCancelable(false);
	}
	
	/**
	 * Gets and fetches the valid pin
	 * @param cont
	 */
	public void handlePin(final Activity cont, final Twitter twitter)
	{
		final Dialog dialog = new Dialog(cont, R.style.DialogBackground);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.twitter_validate_pin);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.FILL_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.setCancelable(false);
	    dialog.show();	
	    final EditText input = (EditText)dialog.findViewById(R.id.twitter_pin_field);
	    Button submit = (Button)dialog.findViewById(R.id.twitter_pin_go);
	    submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String text = input.getText().toString();
				if(text.length() < 5)
				{ 
					Toast.makeText(cont, "Please Enter a Valid PIN", Toast.LENGTH_SHORT).show();
				}
				else
				{
					try{
						pin = Integer.parseInt(text);
						dialog.dismiss();
						finalizeValidation(cont, twitter,dialog);
					}
					catch (NumberFormatException e){
						Toast.makeText(cont, "Please Enter a PIN of Only Numbers", Toast.LENGTH_SHORT).show();
					}
				}
			}
	    });
	}
	
	/**
	 * Calls the authentication asynctask
	 * @param cont
	 * @param dialog 
	 */
	public void finalizeValidation(Context cont, Twitter twitter, Dialog dialog)
	{
		TwitterInteraction obj = new TwitterInteraction();
	    TwitterValidate task = obj.new TwitterValidate((Activity)cont, dialog, twitter);
	    task.execute(cont, twitter, requestToken, Integer.toString(pin));
	} 
	
	/**
	 * Gets the validation URL from twitter
	 * @author Jeff
	 *
	 */
	public class TwitterValidate extends AsyncTask<Object, Void, AccessToken> 
	{
		ProgressDialog pdia;
		Activity act;
		Dialog d;
		Twitter t;
	    public TwitterValidate(Activity activity, Dialog dialog, Twitter twitter) 
	    {
	        pdia = new ProgressDialog(activity);
	        pdia.setCancelable(false);
	        act = activity;
	        t = twitter;
	        d = dialog;
	    }

		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia.setMessage("Please wait, validating your account...");
		        pdia.show();    
		}

		@Override
		protected void onPostExecute(AccessToken result){
		   super.onPostExecute(result);
		   pdia.dismiss();
		   if(result == null)
		   {
			   Toast.makeText(act, "Invalid pin.", Toast.LENGTH_SHORT).show();
			   d.dismiss();
		   }
		   else
		   {
			   d.dismiss();
			   handleAccessToken(act, result);
		   }
		}

	    @Override
	    protected AccessToken doInBackground(Object... data) 
	    {
	    	Context cont = (Context)data[0];
	    	Twitter twit = (Twitter)data[1];
	    	RequestToken rt = (RequestToken)data[2];
	    	String pinStr = (String)data[3];
	    	AccessToken accessToken;
			try {
				accessToken = t.getOAuthAccessToken(rt, pinStr);
				storeID(t.verifyCredentials().getId(), cont);
			} catch (TwitterException e) {
		        if(401 == e.getStatusCode()){
		        	  System.out.println("Error getting token");
			          return null;
			    }else{
			          System.out.println("Error validating token");
			          return null;
			    }
			}
			twit.setOAuthAccessToken(accessToken);
			return accessToken;
	    }
	}
	
	/**
	 * Saves the rest of it to file
	 * @param cont
	 * @param accessToken
	 */
	public void handleAccessToken(Activity cont, AccessToken accessToken)
	{ 
		if(accessToken == null)
		{
			return;
		}
		storeToken(accessToken, cont);
		Toast.makeText(cont, "Successfully set up your account! Please press the menu option again.", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Spawns threads to tweet something from the user's account 
	 *
	 */
	public class ParseTwitterSearch extends AsyncTask<Object, String, Void> 
	{
		ProgressDialog pdia;
		Activity act;
		boolean flag;
		String query;
		TwitterInteraction tw;
		String name;
	    public ParseTwitterSearch(Context cont, TwitterInteraction obj, String drinkName) 
	    {
	    	name = drinkName;
	        pdia = new ProgressDialog(cont);
	        pdia.setCancelable(false);
	        act = (Activity)cont;
	        tw = obj;
	    }

		@Override 
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia.setMessage("Please wait, tweeting...");
		        pdia.show();    
		}

		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			pdia.dismiss();
			Toast.makeText(act, "Success!", Toast.LENGTH_SHORT).show();
		}


	    @Override
	    protected Void doInBackground(Object... data) 
	    {
	    	Context cont = (Context) data[0];
	    	try {
				tw.userTwitter.updateStatus("I just made " + name + " using Boozle!");
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	return null;
	    }
	  }
	
	/**
	 * Writes the use ID to file
	 */
	public static void storeID(long l, Context cont) {
		SharedPreferences.Editor editor = cont.getSharedPreferences("Boozle", 0).edit();
		editor.putLong("Use ID", l);
		editor.commit();
	}

	/**
	 * Writes the token data to file to be read later
	 */
	public static void storeToken(AccessToken token, Context cont) {
		SharedPreferences.Editor editor = cont.getSharedPreferences("Boozle", 0).edit();
		editor.putString("Token", token.getToken());
		editor.putString("Token Secret", token.getTokenSecret());
		editor.commit();
	}
	
	/**
	 * Reads the use id from file
	 */
	public static long readUseID(Context cont) {
		SharedPreferences prefs = cont.getSharedPreferences("Boozle", 0); 
		return prefs.getLong("Use ID", -1);
	}

	/**
	 * Reads the token from file
	 */
	public static String readToken(Context cont) {
		SharedPreferences prefs = cont.getSharedPreferences("Boozle", 0); 
		return prefs.getString("Token", "Not set");
	}

	/**
	 * Reads the token secret from file
	 */
	public static String readTokenSecret(Context cont) {
		SharedPreferences prefs = cont.getSharedPreferences("Boozle", 0); 
		return prefs.getString("Token Secret", "Not set");
	}
}
