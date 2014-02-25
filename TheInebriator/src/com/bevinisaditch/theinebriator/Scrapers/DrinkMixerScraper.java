package com.bevinisaditch.theinebriator.Scrapers;

import java.io.IOException;
import java.util.List;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.Utils.ScrapingUtils;

public class DrinkMixerScraper {

	public static void main(String[] args) {
		try {
			scrapeDrinks();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Scrapes drinks from drinkoftheweek.com
	 * @throws IOException 
	 */
	public static void scrapeDrinks() throws IOException {
		// For each drink group (a, b, c, d, ... z)
		//for (int i = 0; i < 12000; i++) {
			String url = "http://www.drinksmixer.com/drink10000.html";//" + i + ".html");
			Document doc = ScrapingUtils.makeConnection(url);
			String ingredients = ScrapingUtils.handleQueryMulti(doc, url, "span.ingredient").get(0);
			String name = ScrapingUtils.handleQueryMulti(doc, url, "span.item").get(0).split(" recipe")[0];
			String instructions = ScrapingUtils.handleQueryMulti(doc, url, "div.RecipeDirections").get(0);
		//}
	}


}
