package com.snappydb.test;

import android.content.Context;
import android.test.AndroidTestCase;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

// demo code used to illustrate common task 
public class SampleCode extends AndroidTestCase{

	public void testSampleCode1 () {
		Context context = getContext();
		
		try {
			DB snappydb = DBFactory.open(context);
			
			snappydb.put("name", "Jack Reacher");
			snappydb.putInt("age", 42);
			snappydb.putBoolean("single", true);
			snappydb.put("books", new String[]{"One Shot", "Tripwire", "61 Hours"});
			
			String 	 name   =  snappydb.get("name");
			int 	 age    =  snappydb.getInt("age");
			boolean  single =  snappydb.getBoolean("single");
			String[] books  =  snappydb.getArray("books", String.class);
			
		} catch (SnappydbException e) {
		}
	}

	public void testSnippet () {
		
	}
}
