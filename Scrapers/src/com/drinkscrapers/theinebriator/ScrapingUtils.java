package com.drinkscrapers.theinebriator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import android.content.Context;
//import android.widget.Toast;

public class ScrapingUtils {
	public static String ua = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.2 Safari/537.36"; 

	/**
	 * This is the query function for a list
	 * @param url the url to be parsed
	 * @return the text that was parsed
	 * @throws IOException
	 */
	public static List<String> handleQuery(String url, String params) throws IOException
	{
		StringBuilder result = new StringBuilder(5000);
		Document doc = Jsoup.connect(url).userAgent(ua).timeout(0).get();
        Elements links = doc.select(params);

        for (Element element : links) 
        {
        	result.append(element.text() + "\n");
        }
        return Arrays.asList(result.toString().split("\n"));
	}
	
	/**
	 * This will print the number of results a query would have, for debugging purposes
	 */
	/*public static void printQueryResultSize(String url, String params, boolean toastToggle, Context cont) throws IOException
	{
		int size = handleQuery(url, params).size();
		System.out.println("Results had a size of " + size);
		if(toastToggle)
		{
			Toast.makeText(cont, "Results had a size of " + size, Toast.LENGTH_LONG).show();
		}
	}*/
	
	/**
	 * This will print the source of a query, for debugging purposes
	 */
	public static void printQuerySource(String url) throws IOException
	{
		System.out.println(Jsoup.connect(url).get().html());
	}
	
	/**
	 * Just in case the web site doesn't like using the chrome user agent/timeout being infinite is bad, this will work generically
	 */
	public static List<String> handleQueryNoUA(String url, String params) throws IOException
	{
		StringBuilder result = new StringBuilder(5000);
		Document doc = Jsoup.connect(url).get();
        Elements links = doc.select(params);

        for (Element element : links) 
        {
        	result.append(element.text() + "\n");
        }
        return Arrays.asList(result.toString().split("\n"));
	}
	
	/**
	 * This is the query function for a list
	 * called twice+ with the same page
	 */
	public static List<String> handleQueryMulti(Document doc, String url, String params) throws IOException
	{
		StringBuilder result = new StringBuilder(5000);
        Elements links = doc.select(params);
        for (Element element : links) 
        {
        	result.append(element.text() + "\n");
        }
        return Arrays.asList(result.toString().split("\n"));
	}
}