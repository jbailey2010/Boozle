package com.bevinisaditch.theinebriator.Database;

import java.io.IOException;
import java.util.ArrayList;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;
import com.bevinisaditch.theinebriator.ClassFiles.TermFrequency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class TermFrequencyDatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "termFrequency";
 
    // Contacts table name
    private static final String TABLE_TERM_FREQ = "term_freq";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TERM = "term";
    private static final String KEY_FREQ = "frequency";
 
    public TermFrequencyDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TERM_FREQ + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TERM + " TEXT,"
                + KEY_FREQ + " REAL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERM_FREQ);
 
        // Create tables again
        onCreate(db);
		
	}
	

	/**
	 *  Add a term frequency instance to the database
	 * 
	 * @param termFreq instance of term frequency
	 * @return id of the row
	 */
	public Long addTermFreq(TermFrequency termFreq) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_TERM, termFreq.getTerm()); // Contact Name
	    values.put(KEY_FREQ, termFreq.getFrequency()); // Contact Phone Number
	 
	    // Inserting Row
	    long id = db.insert(TABLE_TERM_FREQ, null, values);
	    db.close(); // Closing database connection
	    return id;
	}
	 
	/**
	 * Get an instance of term frequency by name of the term
	 * 
	 * @param term - name of the term
	 * @return TermFrequency instance
	 */
	public TermFrequency getTermFrequency(String term) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		//Query for termFrqe
	    Cursor cursor = db.query(TABLE_TERM_FREQ, new String[] { KEY_ID,
	            KEY_TERM, KEY_FREQ }, KEY_TERM + "=?",
	            new String[] { term }, null, null, null, null);
	    
	    if (cursor != null) {
	        cursor.moveToFirst();
	    }
	 
	    //Term into data class
	    TermFrequency termFreq = new TermFrequency(Integer.parseInt(cursor.getString(0)),
	            cursor.getString(1), cursor.getFloat(2));
	    
	    return termFreq;
	}
	
	/**
	 * Overloaded method to get the term frequency by id
	 * 
	 * @param id - id of the row
	 * @return TermFrequency Instance
	 */
	public TermFrequency getTermFrequency(Long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		//Query for termFrqe
	    Cursor cursor = db.query(TABLE_TERM_FREQ, new String[] { KEY_ID,
	            KEY_TERM, KEY_FREQ }, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    
	    if (cursor != null) {
	        cursor.moveToFirst();
	    }
	 
	    //Term into data class
	    TermFrequency termFreq = new TermFrequency(Integer.parseInt(cursor.getString(0)),
	            cursor.getString(1), cursor.getFloat(2));
	    
	    return termFreq;
	}
	 
	/**
	 * Query for all the term frequencies in the database
	 * 
	 * @return ArrayList of all the term frequency objects
	 */
	public ArrayList<TermFrequency> getAllTermFreqs() {
		ArrayList<TermFrequency> termFreqList = new ArrayList<TermFrequency>();
	    
		// Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_TERM_FREQ;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            TermFrequency termFreq = new TermFrequency();
	            termFreq.setID(Integer.parseInt(cursor.getString(0)));
	            termFreq.setTerm(cursor.getString(1));
	            termFreq.setFrequency(cursor.getFloat(2));
	            // Adding contact to list
	            termFreqList.add(termFreq);
	        } while (cursor.moveToNext());
	    }
	 
	    // return term freq list
	    return termFreqList;
		
	}
	 
	// Getting contacts Count
	public int getTermFreqCount() {
		//TODO Implement
		return 0;
	}
	
	/**
	 * Updates the term frequency that already exists
	 * 
	 * @param termFreq
	 * @return unique db id of the term
	 */
	public int updateContact(TermFrequency termFreq) {
		//TODO: Implement
		
		return 0;
	}
	 
	// Deleting single contact
	public void deleteContact(TermFrequency termFreq) {
		//TODO: Implement
		
	}
	
}