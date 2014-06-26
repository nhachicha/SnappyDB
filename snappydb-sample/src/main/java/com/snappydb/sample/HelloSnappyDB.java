/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.snappydb.sample;

import android.app.Activity;
import android.widget.TextView;
import android.os.Bundle;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class HelloSnappyDB extends Activity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try {
			DB snappydb = DBFactory.open(this);
			
			snappydb.put("name", "Jack Reacher");
			snappydb.putInt("age", 42);
			snappydb.putBoolean("single", true);
			snappydb.put("books", new String[]{"One Shot", "Tripwire", "61 Hours"});
			boolean exists = snappydb.exists("name");
			String 	 name   =  snappydb.get("name");
			int 	 age    =  snappydb.getInt("age");
			boolean  single =  snappydb.getBoolean("single");
			String[] books  =  snappydb.getArray("books", String.class);
			
			snappydb.close();

            StringBuilder message = new StringBuilder();
            message.append("Name:   ").append(name).append("\n\n");
            message.append("Age:    ").append(age).append("\n\n");
            message.append("Single: ").append(single).append("\n\n");
            message.append("Books:  ").append(books[0]).append("-").append(books[1]).append("-").append(books[2]);

			TextView  tv = new TextView(this);
	        tv.setText(message);
	        setContentView(tv);
        	
		} catch (SnappydbException e) {
            e.printStackTrace();
		}
    }

}
