package com.bevinisaditch.theinebriator.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bevinisaditch.theinebriator.Loading;
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

		private static HashSet<String> unitsSet;
		private static List<String> dNames;
		private static List<String> iNames;
		
		public static List<String> getINames(){
			if(iNames == null || iNames.size() == 0){
				iNames = deduplicateList(Loading.ingrNames);
			}
			return iNames;
		}
		
		public static List<String> getDNames(){
			if(dNames == null || dNames.size() == 0){
				dNames = deduplicateList(Loading.drinkNames);
			}
			return dNames;
		}
		
		/**
		 * A small helper method to handle deduplication of names of ingredients 
		 * and drink names
		 * 
		 * @param names - the ingredient or drink names, uncut
		 * @return the trimmed, deduplicated version
		 */
		private static List<String> deduplicateList(List<String> names){
			List<String> dedupNames = new ArrayList<String>();
			Set<String> dedup = new HashSet<String>();
			for(String name : names){
				if(!dedup.contains(name)){
					dedup.add(name);
				}
			}
			dedupNames.addAll(dedup);
			return dedupNames;
		}
		
		public static HashSet<String> getUnits(){
			if(unitsSet == null || unitsSet.size() == 0){
				unitsSet = populateSet();
			}
			return unitsSet;
		}
		
		private static HashSet<String> populateSet(){
			String[] units = {"teaspoon","scoop","cup","cups","part","package", "plateful", 
					"shot","dashes","dash","tsp","tbsp","pony","ml","sprig","pinch","inch","jigger",
					"can","cans","bottle","tb","drop","liter","litre","twist","amount",
					"spoon","squeeze","pinch","stalk","bag","fifth","bottles","liters",
			       "gal","splashes","splash","float","pint","glass","clbottle","drops",
			       "tablespoon","tablespoons","ponies","gallon","quart","oz", "oz)",
			       "ounce","slice","cl","whole","piece","pieces","g","lb", "oz", 
			       "l","dl","pt","qt"};
			return new HashSet<String>(Arrays.asList(units));
		}
		
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
		 * @param name - The drink to look at
		 * @param cont - The context for the sake of the asynchronous check
		 */
		public static void bumpEntityValue(String name, final Context cont)
		{
			String key = "http://www.boozle.com/" + name;
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
