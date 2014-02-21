package com.bevinisaditch.theinebriator.Scrapers;

import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class DrinkOfTheWeekScraper {
	static Document doc;

	public static void main(String[] args) {
		connectToWebsite();

	}
	
	public static void connectToWebsite() {
		try {
			
			//Connect to the website
			doc = Jsoup.connect("http://www.drinkoftheweek.com/list-drinks-a-z/")
					.data("query", "Java")
					.userAgent("Mozilla")
					.cookie("auth", "token")
					.timeout(3000)
					.post();
			
			//Get drinks in the 'drinkgroup' html elements
			Elements all_drinks = doc.getElementsByClass("drinkgroup");
			
			//For each drink group (a, b, c, d, ... z)
			for (Element drink_group: all_drinks) {
				
				//Get the href tags
				Elements urls = drink_group.getElementsByAttribute("href");
				
				//Get URL from href tag
				for (Element urlTag : urls) {
					String url = urlTag.attr("href");
					System.out.println(url);
				}
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
