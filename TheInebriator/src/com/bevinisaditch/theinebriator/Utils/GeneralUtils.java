package com.bevinisaditch.theinebriator.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.socialize.EntityUtils;
import com.socialize.entity.Entity;
import com.socialize.error.SocializeException;
import com.socialize.listener.entity.EntityAddListener;
import com.socialize.listener.entity.EntityGetListener;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * A static library for general utilities needed through other parts of the app
 * @author Jeff
 *
 */
public class GeneralUtils {

		/**
		 * Checks to see if it can connect to the internet
		 * @param cont - A context to look through the app and see what it knows
		 * @return - a boolean that's true if it can, false if it can't
		 */
		public static boolean testInternet(Context cont){
			ConnectivityManager connectivityManager  = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		}
		
		/**
		 * Sorts a list of hashmaps alphabetically by one of the elements of each map
		 * @param data - the list of maps to sort
		 * @return - the sorted list of maps
		 */
		public static List<Map<String, String>> sortData(List<Map<String, String>> data){
			Collections.sort(data, new Comparator<Map<String, String>>() {
				public int compare(Map<String, String> a, Map<String, String> b) {
					String aNorm = a.get("main").toLowerCase();
					String bNorm = b.get("main").toLowerCase();
					int judgment = aNorm.compareTo(bNorm);
					if(judgment < 0){
						return -1;
					}
					if(judgment > 0){
						return 1;
					}
					return 0;
				}
			});
			return data;
		}
		
		/**
		 * Takes a single list of strings and sorts it alphabetically
		 * @param data - the list of strings to be sorted
		 * @return - the sorted list
		 */
		public static List<String> sortSingleList(List<String> data){
			Collections.sort(data);
			return data;
		}
		
		/**
		 * Takes the entity, and if it already has a thumbs up count, increases it, otherwise create it at one
		 * @param dr - The drink to look at
		 * @param cont - The context for the sake of the asynchronous check
		 */
		public static void bumpEntityValue(Drink dr, final Context cont)
		{
			String key = "http://www.boozle.com/" + dr.getName();
			EntityUtils.getEntity((Activity) cont, key, new EntityGetListener() {
				//The entity was gotten, though an error is still possible
				@Override
				public void onGet(Entity entity) {
					int newVal = 1;
				   	if(entity.getMetaData() != null && entity.getMetaData().length() != 0)
				   	{
				   		newVal = Integer.parseInt(entity.getMetaData()) + 1;
				   	}
				   	entity.setMetaData(String.valueOf(newVal));
			    	EntityUtils.saveEntity((Activity)cont , entity, new EntityAddListener() {
			       		@Override
			    		public void onCreate(Entity result) {
			       			//If we want to add some kind of handler, here is where to do so
			    		}
						@Override
						public void onError(SocializeException error) {
							//Some kind of error in saving, collision?
						}
			    	});
				}
				@Override
				public void onError(SocializeException error) {
					if(isNotFoundError(error)) {
						// No entity found
					}
					else {
						//Some other kind of error
					}
				}
			});
		 	
		}
}
