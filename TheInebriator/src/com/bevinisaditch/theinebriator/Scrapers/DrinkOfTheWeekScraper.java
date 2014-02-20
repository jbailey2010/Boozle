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
			doc = Jsoup.connect("http://www.drinkoftheweek.com/list-drinks-a-z/")
					.data("query", "Java")
					.userAgent("Mozilla")
					.cookie("auth", "token")
					.timeout(3000)
					.post();
			Elements all_drinks = doc.getElementsByClass("drinkgroup");
			
			for (Element drink_group: all_drinks) {
				System.out.println(drink_group.getElementsByAttribute("href").text());
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
