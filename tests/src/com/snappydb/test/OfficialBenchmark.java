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

package com.snappydb.test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.snappydb.test.utils.SimpleDatabase;

public class OfficialBenchmark extends AndroidTestCase{
	
	// 1000 insert of 100 Byte ==> 100 000 byte
	public void testReferenceSetup () throws SnappydbException, UnsupportedEncodingException {
		
		String keys [] = new String [1000];
		String values [] = new String [1000];
		byte[][] bytesValues = new byte [1000][];
		
		String key, value;
		for (int i=0; i<1000; i++)  {
			//Generate key
			key = new String (Math.random() + "" +  Math.random()).substring(0, 16);//16 bytes 
			
			//Generate value
			value =  new String (Math.random() 
									  + "" +  Math.random()
									  + "" +  Math.random()
									  + "" +  Math.random()
									  + "" +  Math.random()
									  + "" +  Math.random()).substring(0, 100); //100 bytes 
			keys[i]   = key;
			values[i] = value;
			bytesValues[i] = value.getBytes();
		}
		
		//check that each key is unique 
		HashMap<String, Boolean> keyMap = new HashMap<String, Boolean>(1000);
		
		for (String k : keys) {
			if (null == keyMap.get(k)) {
				keyMap.put(k, true);
			} else {
				fail("duplicate key");
				break;
			}
		}
		
		
		// Now we are ready to insert into SnappyDB [String]
		DB db = DBFactory.open(getContext(), "reference_bench_string");
		
		long beginWriteStr = System.currentTimeMillis();
		for (int i=0; i<1000; i++) {
			db.put(keys[i], values[i]);
		}
		long endWriteStr = System.currentTimeMillis(); 
		
		db.close();
		
		db = DBFactory.open(getContext(), "reference_bench_string");
		
		long beginReadStr = System.currentTimeMillis();
		for (int i=0; i<1000; i++) {
			db.get(keys[i]);
		}
		long endReadStr = System.currentTimeMillis();
		
		System.out.println("PERF - [testReferenceSetup] write string in " +  (endWriteStr-beginWriteStr) + " ms");
		System.out.println("PERF - [testReferenceSetup] read  string in " +  (endReadStr-beginReadStr) + " ms");
		
		db.close();

		// Now we are ready to insert into SnappyDB [Bytes]
		db = DBFactory.open(getContext(), "reference_bench_bytes");
		
		long beginWriteBin = System.currentTimeMillis();
		for (int i=0; i<1000; i++) {
			db.put(keys[i], bytesValues[i]);
		}
		long endWriteBin = System.currentTimeMillis(); 
		
		db.close();
		
		db = DBFactory.open(getContext(), "reference_bench_bytes");
		
		long beginReadBin = System.currentTimeMillis();
		for (int i=0; i<1000; i++) {
			db.getBytes(keys[i]);
		}
		long endReadBin = System.currentTimeMillis();
		
		System.out.println("PERF - [testReferenceSetup] write bytes in " +  (endWriteBin-beginWriteBin) + " ms");
		System.out.println("PERF - [testReferenceSetup] read  bytes in " +  (endReadBin-beginReadBin) + " ms");
		db.close();

		//======================== USING THE SAME DATA INSERT INTO SQLite
		
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "reference_bench_string");
		
		SimpleDatabase sqliteDB = new SimpleDatabase(context);
		String CREATE_TABLE = "CREATE TABLE T_STR (STR_KEY TEXT, STR_VALUE TEXT);";
		String INSERT_STRING = "INSERT INTO T_STR (STR_KEY, STR_VALUE) VALUES (?,?);";
		
		SQLiteDatabase sqlWrite = sqliteDB.getWritableDatabase();
		sqlWrite.execSQL(CREATE_TABLE);
		
		beginWriteStr = System.currentTimeMillis();
		for (int i=0; i<1000; i++) {
			sqlWrite.execSQL(INSERT_STRING, new String[]{keys[i], values[i]});
		}
		endWriteStr = System.currentTimeMillis();
		
		SQLiteDatabase sqlRead = sqliteDB.getReadableDatabase();
		
		beginReadStr = System.currentTimeMillis();
		for (int i=0; i<1000; i++) {
			Cursor cursor = sqlRead.query("T_STR", new String[]{"STR_VALUE"}, "STR_KEY=?", new String[]{keys[i]}, null, null, null);
			cursor.moveToFirst();
			cursor.getString(0);
		}
		
		endReadStr = System.currentTimeMillis();
		
		
		System.out.println("PERF - [testReferenceSetup] SQLite write string in " +  (endWriteStr-beginWriteStr) + " ms");
		System.out.println("PERF - [testReferenceSetup] SQLite read  string in " +  (endReadStr-beginReadStr) + " ms");
		
		
		// testing bytes
		context = new RenamingDelegatingContext(
				getContext(), "reference_bench_bytes");
		
		sqliteDB = new SimpleDatabase(context);
		String CREATE_TABLE_BYTES = "CREATE TABLE T_BIN (STR_KEY TEXT, BIN_VALUE BLOB);";
		String INSERT_STRING_BYTES = "INSERT INTO T_BIN (STR_KEY, BIN_VALUE) VALUES (?,?);";
		
		sqlWrite = sqliteDB.getWritableDatabase();
		sqlWrite.execSQL(CREATE_TABLE_BYTES);
		
		SQLiteStatement insertStmt = sqlWrite.compileStatement(INSERT_STRING_BYTES);
		
		beginWriteStr = System.currentTimeMillis();
		for (int i=0; i<1000; i++) {
			insertStmt.bindString(1, keys[i]);
			insertStmt.bindBlob(2, bytesValues[i]);
			insertStmt.executeInsert();
			
		}
		sqlWrite.close();
		insertStmt.close();
		endWriteStr = System.currentTimeMillis();
		
		sqlRead = sqliteDB.getReadableDatabase();
		
		beginReadStr = System.currentTimeMillis();
		for (int i=0; i<1000; i++) {
			Cursor cursor = sqlRead.query("T_BIN", new String[]{"BIN_VALUE"}, "STR_KEY=?", new String[]{keys[i]}, null, null, null);
			cursor.moveToFirst();
			cursor.getBlob(0);
			cursor.close();
		}
		
		endReadStr = System.currentTimeMillis();
		
		
		System.out.println("PERF - [testReferenceSetup] SQLite write bytes in " +  (endWriteStr-beginWriteStr) + " ms");
		System.out.println("PERF - [testReferenceSetup] SQLite read  bytes in " +  (endReadStr-beginReadStr) + " ms");

		sqliteDB.close();
		
 	}
}
