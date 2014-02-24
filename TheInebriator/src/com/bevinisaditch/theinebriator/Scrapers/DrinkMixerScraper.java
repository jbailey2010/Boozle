package com.bevinisaditch.theinebriator.Scrapers;

import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;

public class DrinkMixerScraper {

	public static void main(String[] args) {
		scrapeDrinks();
	}

	/**
	 * Scrapes drinks from drinkoftheweek.com
	 */
	public static void scrapeDrinks() {
		// For each drink group (a, b, c, d, ... z)
		//for (int i = 0; i < 12000; i++) {
			Document doc = new Document("http://www.drinkmixer.com/drink10000.html");//" + i + ".html");
			for (Element e : doc.select("span.ingredient")) {
			    System.out.println(e.text());
			}
		//}
	}


}
