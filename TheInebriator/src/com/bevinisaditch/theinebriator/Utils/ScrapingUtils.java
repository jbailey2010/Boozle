package com.bevinisaditch.theinebriator.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.widget.Toast;
/**
 * A mostly static library to help with some scraping
 * @author Jeff
 *
 */
public class ScrapingUtils {
	public static String ua = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.2 Safari/537.36"; 

	
	/**
	 * Gets the document containing the source of the page to be queried.
	 */
	public static Document makeConnection(String url) throws IOException {
		Document doc = Jsoup.connect(url).userAgent(ua).timeout(0).get();
		return doc;	
	}
	
	
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
	 * Gets a list of links given an identifier, as opposed to a list of text results
	 */
	public static List<String> getLinks(String url, String params) throws IOException
	{
		Document doc = Jsoup.connect(url).userAgent(ua).timeout(0).get();
		List<String> results = new ArrayList<String>();
		Elements links = doc.select(params);
		for(Element element : links)
		{
			results.add(element.attr("href"));
		}
		return results;
	}
	
	/**
	 * This will print the number of results a query would have, for debugging purposes
	 */
	public static int printQueryResultSize(String url, String params, boolean toastToggle, Context cont) throws IOException
	{
		int size = handleQuery(url, params).size();
		System.out.println("Results had a size of " + size);
		if(toastToggle)
		{
			Toast.makeText(cont, "Results had a size of " + size, Toast.LENGTH_LONG).show();
		}
		return size;
	}
	
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
