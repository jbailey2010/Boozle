package com.bevinisaditch.theinebriator.Database;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bevinisaditch.theinebriator.ClassFiles.TermFrequency;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class TermFrequencyDatabaseHandlerTest extends AndroidTestCase {
	TermFrequencyDatabaseHandler handler;
	TermFrequency termFreq1;
	TermFrequency termFreq2;
	TermFrequency termFreq3;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		//RenamingDelegatingContext context  = new RenamingDelegatingContext(getContext(), "test_");
		handler = new TermFrequencyDatabaseHandler(getContext());
		termFreq1 = new TermFrequency("test1", .1f);
		termFreq2 = new TermFrequency("test2", .3f);
		termFreq3 = new TermFrequency("test3", .6f);
	}

	@Test
	public void testAddTermFreq() {
		Long id = handler.addTermFreq(termFreq1);
		
		TermFrequency get1 = handler.getTermFrequency(id);
		assertEquals(termFreq1, get1);
	}

}
