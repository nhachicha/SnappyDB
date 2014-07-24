/*
 * Copyright (C) 2013 Nabil HACHICHA.
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

package com.snappydb.sample.tests.benchmark;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;
import com.snappydb.sample.tests.api.helper.SimpleDatabase;

public class OfficialBenchmark extends AndroidTestCase {
    private final static String TAG = "BENCHMARK";

    // 1000 insert of 100 byte ==> 100 000 bytes
    public void testReferenceSetup() throws SnappydbException, UnsupportedEncodingException {

        String keys[] = new String[1000];
        String values[] = new String[1000];
        byte[][] bytesValues = new byte[1000][];

        String key, value;
        for (int i = 0; i < 1000; i++) {
            //Generate key
            key = new StringBuilder()
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random()).substring(0, 16);//16 bytes

            //Generate value
            value = new StringBuilder()
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random()).substring(0, 100);//100 bytes

            keys[i] = key;
            values[i] = value;
            bytesValues[i] = value.getBytes();
        }

        //check that each key is unique
        HashMap<String, Boolean> keyMap = new HashMap<>((int) Math.ceil(1000 / 0.75));

        for (String k : keys) {
            if (null == keyMap.get(k)) {
                keyMap.put(k, true);
            } else {
                fail("duplicate key");
                break;
            }
        }


        // Now we are ready to insert into SnappyDB [String]
        DB db = new SnappyDB.Builder(getContext())
                .name("reference_bench_string")
                .build();

        long beginWriteStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            db.put(keys[i], values[i]);
        }
        long endWriteStr = System.nanoTime();

        db.close();

        db = new SnappyDB.Builder(getContext())
                .name("reference_bench_string")
                .build();

        long beginReadStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            db.get(keys[i]);
        }
        long endReadStr = System.nanoTime();

        report("[testReferenceSetup] 1000 write of String in ", endWriteStr - beginWriteStr);
        report("[testReferenceSetup] 1000 read  of String in ", endReadStr - beginReadStr);

        db.close();

        // Now we are ready to insert into SnappyDB [Bytes]
        db = DBFactory.open(getContext(), "reference_bench_bytes");

        long beginWriteBin = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            db.put(keys[i], bytesValues[i]);
        }
        long endWriteBin = System.nanoTime();

        db.close();

        db = DBFactory.open(getContext(), "reference_bench_bytes");

        long beginReadBin = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            db.getBytes(keys[i]);
        }
        long endReadBin = System.nanoTime();

        report("[testReferenceSetup] 1000 write of bytes in ", endWriteBin - beginWriteBin);
        report("[testReferenceSetup] 1000 read  of bytes in ", endReadBin - beginReadBin);
        db.close();

        //======================== USING THE SAME DATA, INSERT INTO SQLite

        RenamingDelegatingContext renamedContext = new RenamingDelegatingContext(
                getContext(), "reference_bench_string");

        SimpleDatabase sqliteDB = new SimpleDatabase(renamedContext);
        final String CREATE_TABLE = "CREATE TABLE T_STR (STR_KEY TEXT, STR_VALUE TEXT);";
        final String INSERT_STRING = "INSERT INTO T_STR (STR_KEY, STR_VALUE) VALUES (?,?);";

        SQLiteDatabase sqlWrite = sqliteDB.getWritableDatabase();
        sqlWrite.execSQL(CREATE_TABLE);

        beginWriteStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            sqlWrite.execSQL(INSERT_STRING, new String[]{keys[i], values[i]});
        }
        endWriteStr = System.nanoTime();

        SQLiteDatabase sqlRead = sqliteDB.getReadableDatabase();

        beginReadStr = System.nanoTime();
        Cursor cursor;
        for (int i = 0; i < 1000; i++) {
            cursor = sqlRead.query("T_STR", new String[]{"STR_VALUE"}, "STR_KEY=?", new String[]{keys[i]}, null, null, null);
            cursor.moveToFirst();
            cursor.getString(0);
            cursor.close();
        }

        endReadStr = System.nanoTime();

        report("[testReferenceSetup] SQLite 1000 write of String in ", endWriteStr - beginWriteStr);
        report("[testReferenceSetup] SQLite 1000 read  of String in ", endReadStr - beginReadStr);

        // testing bytes
        renamedContext = new RenamingDelegatingContext(
                getContext(), "reference_bench_bytes");

        sqliteDB = new SimpleDatabase(renamedContext);
        String CREATE_TABLE_BYTES = "CREATE TABLE T_BIN (STR_KEY TEXT, BIN_VALUE BLOB);";
        String INSERT_STRING_BYTES = "INSERT INTO T_BIN (STR_KEY, BIN_VALUE) VALUES (?,?);";

        sqlWrite = sqliteDB.getWritableDatabase();
        sqlWrite.execSQL(CREATE_TABLE_BYTES);

        SQLiteStatement insertStmt = sqlWrite.compileStatement(INSERT_STRING_BYTES);

        beginWriteStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            insertStmt.bindString(1, keys[i]);
            insertStmt.bindBlob(2, bytesValues[i]);
            insertStmt.executeInsert();

        }
        sqlWrite.close();
        insertStmt.close();
        endWriteStr = System.nanoTime();

        sqlRead = sqliteDB.getReadableDatabase();

        beginReadStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            cursor = sqlRead.query("T_BIN", new String[]{"BIN_VALUE"}, "STR_KEY=?", new String[]{keys[i]}, null, null, null);
            cursor.moveToFirst();
            cursor.getBlob(0);
            cursor.close();
        }

        endReadStr = System.nanoTime();


        report("[testReferenceSetup] SQLite 1000 write of bytes in ", endWriteStr - beginWriteStr);
        report("[testReferenceSetup] SQLite 1000 read  of bytes in ", endReadStr - beginReadStr);

        sqliteDB.close();

    }

    private static void report(String msg, long delay) {
        StringBuilder message = new StringBuilder(msg)
                .append("\t")
                .append(TimeUnit.MILLISECONDS.convert(delay, TimeUnit.NANOSECONDS))
                .append("ms");

        Log.i(TAG, message.toString());
    }
}
