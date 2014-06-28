package com.snappydb.sample.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.snappydb.DB;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            DB snappydb = SnappyDB.with(this);

            snappydb.put("name", "Jack Reacher");
            snappydb.putInt("age", 42);
            snappydb.putBoolean("single", true);
            snappydb.put("books", new String[]{"One Shot", "Tripwire", "61 Hours"});

            String name    = snappydb.get("name");
            int age        = snappydb.getInt("age");
            boolean single = snappydb.getBoolean("single");
            String[] books = snappydb.getArray("books", String.class);

            snappydb.close();

            StringBuilder message = new StringBuilder();
            message.append("Name:   ").append(name).append("\n\n");
            message.append("Age:    ").append(age).append("\n\n");
            message.append("Single: ").append(single).append("\n\n");
            message.append("Books:  ").append(books[0]).append("-").append(books[1]).append("-").append(books[2]);

            TextView tv = new TextView(this);
            tv.setText(message);
            setContentView(tv);

        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}
