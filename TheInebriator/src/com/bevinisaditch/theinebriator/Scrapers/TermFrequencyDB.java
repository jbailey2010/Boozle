package com.bevinisaditch.theinebriator.Scrapers;

import java.io.IOException;
import java.util.ArrayList;

import com.bevinisaditch.theinebriator.ClassFiles.Drink;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class TermFrequencyDB extends SQLiteOpenHelper {

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "termFrequency";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "term_freq";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TERM = "term";
    private static final String KEY_FREQ = "frequency";
 
    public TermFrequencyDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TERM + " TEXT,"
                + KEY_FREQ + " REAL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
 
        // Create tables again
        onCreate(db);
		
	}
	
}
