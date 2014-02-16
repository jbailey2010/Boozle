package com.bevinisaditch.theinebriator.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
