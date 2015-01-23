package com.bevinisaditch.theinebriator.ClassFiles;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.devingotaswitch.theinebriator.R;
import com.socialize.EntityUtils;
import com.socialize.EntityUtils.SortOrder;
import com.socialize.entity.Entity;
import com.socialize.entity.ListResult;
import com.socialize.error.SocializeException;
import com.socialize.listener.entity.EntityListListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
/**
 * A little library to handle the rendering of the player view/like stats in a pop up
 * @author Jeff
 *
 */
public class DrinkInfo 
{
	public static TextView output;
	public static StringBuilder sb;

	/**
	 * Does the real work, fetching the data asynchronously, sorting it appropriately, and 
	 * displays it in a simple way on the pop up
	 * @param cont - to make the dialog appear. Android stuff.
	 */
	public static void displayStats(Context cont)
	{
		//Interface syntax stuff
		final Dialog dialog = new Dialog(cont, R.style.DialogBackground);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.one_line_text);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    dialog.getWindow().setAttributes(lp);
	    dialog.show();
	    Button close = (Button)dialog.findViewById(R.id.player_stats_close);
	    close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
	    });
	    output = (TextView)dialog.findViewById(R.id.textView1);
	    EntityUtils.getEntities((Activity) cont, 0, 500, SortOrder.TOTAL_ACTIVITY, new EntityListListener() {
			@Override
			public void onList(ListResult<Entity> result) {
				List<Entity> results = result.getItems();
				StringBuilder data = new StringBuilder(1000);
				if(results != null){
					//Sort the top <= 15 by views
					int i = 0;
					PriorityQueue<Entity> sorted = sortEntitiesViews(results);
					if(sorted.size() > 0) {
						data.append("Views:\n");
						while(!sorted.isEmpty() && i < 15) {
							Entity elem = sorted.poll();
							i++;
							data.append(i + ". "+ elem.getDisplayName().split("@%")[0] + " - " + elem.getEntityStats().getViews() + " views\n");
						}
					}
					//Sort hte top <= 15 by socialize comments
					PriorityQueue<Entity> sortedComments = sortEntitiesComments(results);
					while(!sortedComments.isEmpty() && i < 15) {
						if(i == 0) {
							data.append("Comments:\n");
						}
						Entity elem = sortedComments.poll();
						i++;
						data.append(i + ". "+ elem.getDisplayName().split("@%")[0] + " - " + elem.getEntityStats().getComments() + " likes\n");
					}
					//Sorts the top <= 15 by local thumbs ups
					i = 0;
					PriorityQueue<Entity> sortedThumbs = sortEntitiesThumbs(results);
					if(sortedThumbs.size() > 0){
						data.append("\n");
					}
					i = 0;
					while(!sortedThumbs.isEmpty() && i < 15){
						if(i == 0){
							data.append("Thumbs Upped:\n");
						}
						Entity elem = sortedThumbs.poll();
						i++;
						data.append(i + ". "+ elem.getDisplayName().split("@%")[0] + " - " + elem.getMetaData() + " times\n");
					}
				}
				else{
					data.append("An error occurred. Do you have an internet connection?");
				}
				output.setText(data.toString());
			}

			@Override
			public void onError(SocializeException error) {
				output.setText("An error occurred. Do you have an internet connection?");
			}
	    });
	}

	/**
	 * Sorts the results by views
	 * @param input - the list of entities to be sorted
	 * @return - the priority queue of entities by views
	 */
	public static PriorityQueue<Entity> sortEntitiesViews(List<Entity> input)
	{
		PriorityQueue<Entity> sorted = new PriorityQueue<Entity>(100, new Comparator<Entity>()
				{
					@Override
					public int compare(Entity a, Entity b)
					{
						if(a.getEntityStats().getViews() > b.getEntityStats().getViews())
						{
							return -1;
						}
						if(a.getEntityStats().getViews() < b.getEntityStats().getViews())
						{
							return 1;
						}
						return 0;
					}
				});
		for(Entity elem : input)
		{
			sorted.add(elem);
		}
		return sorted;
	}

	/**
	 * Sorts the results by comments
	 * @param input - the list of entities
	 * @return - Priority queue by socialize comments
	 */
	public static PriorityQueue<Entity> sortEntitiesComments(List<Entity> input)
	{
		PriorityQueue<Entity> sorted = new PriorityQueue<Entity>(100, new Comparator<Entity>()
				{
					@Override
					public int compare(Entity a, Entity b)
					{
						if(a.getEntityStats().getComments() > b.getEntityStats().getComments())
						{
							return -1;
						}
						if(a.getEntityStats().getComments() < b.getEntityStats().getComments())
						{
							return 1;
						}
						return 0;
					}
				});
		for(Entity elem : input)
		{
			if(elem.getEntityStats().getComments() > 0)
			{
				sorted.add(elem);
			}
		}
		return sorted;
	}
	
	/**
	 * Sorts the input based on the amount of times thumbs upped
	 * @param input - the list of entities
	 * @return - Priority Queue sorted by times thumbs upped
	 */
	public static PriorityQueue<Entity> sortEntitiesThumbs(List<Entity> input)
	{
		PriorityQueue<Entity> sorted = new PriorityQueue<Entity>(100, new Comparator<Entity>()
				{
					@Override
					public int compare(Entity a, Entity b)
					{
						if(Integer.parseInt(a.getMetaData()) > Integer.parseInt(b.getMetaData()))
						{
							return -1;
						}
						if(Integer.parseInt(a.getMetaData()) < Integer.parseInt(b.getMetaData()))
						{
							return 1;
						}
						return 0;
					}
				});
		for(Entity elem : input)
		{
			try{
				if(elem.getMetaData()!= null && elem.getMetaData().length() > 0 && Integer.parseInt(elem.getMetaData()) > 0)
				{
					sorted.add(elem);
				}
			} catch(NumberFormatException e){
				continue;
			}
		}
		return sorted;
	}

}