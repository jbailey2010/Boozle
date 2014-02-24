package com.drinkscrapers.theinebriator;

import java.io.IOException;
import java.util.List;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.drinkscrapers.theinebriator.ScrapingUtils;;

public class DrinkMixerScraper {

	public static void main(String[] args) {
		scrapeDrinks();
	}

	/**
	 * Scrapes drinks from drinkoftheweek.com
	 */
	public static void scrapeDrinks() {
		//for (int i = 0; i < 12000; i++) {
			try {
				List<String> ingredients = ScrapingUtils.handleQuery("http://www.drinksmixer.com/drink10000.html", "span.ingredient");//" + i + ".html");
				System.out.println(ingredients.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}


}