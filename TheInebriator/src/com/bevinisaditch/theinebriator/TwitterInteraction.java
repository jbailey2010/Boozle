package com.bevinisaditch.theinebriator;

import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;

public class TwitterInteraction {
	
	/**
	 * Writes the use ID to file
	 */
	public static void storeID(long l, Context cont)
	{
		SharedPreferences.Editor editor = cont.getSharedPreferences("Boozle", 0).edit();
		editor.putLong("Use ID", l);
		editor.commit();
	}

	/**
	 * Writes the token data to file to be read later
	 */
	public static void storeToken(AccessToken token, Context cont)
	{
		SharedPreferences.Editor editor = cont.getSharedPreferences("Boozle", 0).edit();
		editor.putString("Token", token.getToken());
		editor.putString("Token Secret", token.getTokenSecret());
		editor.commit();
	}
	
	/**
	 * Reads the use id from file
	 */
	public static long readUseID(Context cont)
	{
		SharedPreferences prefs = cont.getSharedPreferences("Boozle", 0); 
		return prefs.getLong("Use ID", -1);
	}

	/**
	 * Reads the token from file
	 */
	public static String readToken(Context cont)
	{
		SharedPreferences prefs = cont.getSharedPreferences("Boozle", 0); 
		return prefs.getString("Token", "Not set");
	}

	/**
	 * Reads the token secret from file
	 */
	public static String readTokenSecret(Context cont)
	{
		SharedPreferences prefs = cont.getSharedPreferences("Boozle", 0); 
		return prefs.getString("Token Secret", "Not set");
	}
}
