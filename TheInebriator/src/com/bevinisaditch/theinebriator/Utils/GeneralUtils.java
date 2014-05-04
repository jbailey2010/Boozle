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

public class GeneralUtils {

		public static boolean testInternet(Context cont){
			ConnectivityManager connectivityManager  = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		}
		
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
		
		public static List<String> sortSingleList(List<String> data){
			Collections.sort(data);
			return data;
		}
		
		public static void bumpEntityValue(Drink dr, final Context cont)
		{
			String key = "http://www.boozle.com/" + dr.getName();
			EntityUtils.getEntity((Activity) cont, key, new EntityGetListener() {
				
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
			    		}
						@Override
						public void onError(SocializeException error) {
						}
			    	});
				}
				
				@Override
				public void onError(SocializeException error) {
					if(isNotFoundError(error)) {
						// No entity found
					}
					else {
						// Handle error
					}
				}
			});
		 	
		}
}
