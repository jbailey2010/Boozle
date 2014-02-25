package com.bevinisaditch.theinebriator.Scrapers;

import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;



public class DrinkOfTheWeekScraper {

	public static void main(String[] args) {
		scrapeDrinks();

	}
	
	/**
	 * Scrapes drinks from drinkoftheweek.com
	 */
	public static void scrapeDrinks() {
		try {
			
			//Connect to the website
			Document mainWebsite = connectToWebsite("http://www.drinkoftheweek.com/list-drinks-a-z/");
			
			//Get drinks in the 'drinkgroup' html elements
			Elements all_drinks = mainWebsite.getElementsByClass("drinkgroup");
			
			//For each drink group (a, b, c, d, ... z)
			for (Element drink_group: all_drinks) {
				
				//Get the href tags
				Elements urls = drink_group.getElementsByAttribute("href");
				
				//Get URL from href tag
				for (Element urlTag : urls) {
					String url = urlTag.attr("href");
					scrapeIndividualDrink(url);
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses an individual drink page and returns a drink
	 * @param url - URL of the drink
	 * @return Drink object representing the parsed data
	 * @throws IOException - Thrown if cannot connect to web page
	 */
	public static Drink scrapeIndividualDrink(String url) throws IOException {
		System.out.println("Connecting to: " + url);
		Document drinkPage = connectToWebsite(url);
		//TODO: Continue from here
		
		return new Drink("test");
	}
	
	/**
	 * Helper function to connect to a web page
	 * 
	 * @param url - URL of web page to connect to
	 * @return JSoup document that can be searched through
	 * @throws IOException - Thrown if you can't connect
	 */
	private static Document connectToWebsite(String url) throws IOException {
		Document doc = Jsoup.connect(url)
				.data("query", "Java")
				.userAgent("Mozilla")
				.cookie("auth", "token")
				.timeout(3000)
				.post();
		
		return doc;
	}
	

}
