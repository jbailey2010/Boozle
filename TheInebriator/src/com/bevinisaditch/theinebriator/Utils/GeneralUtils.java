package com.bevinisaditch.theinebriator.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
}
