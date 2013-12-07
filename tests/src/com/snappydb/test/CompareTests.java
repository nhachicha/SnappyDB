package com.snappydb.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.snappydb.test.utils.SimpleDatabase;

public class CompareTests extends AndroidTestCase {
	private static final String STRING_OF_256_CHARS = "Lorem ipsum dolor sit amet, audiam saperet interpretaris eam id. Ex luptatum ocurreret sit, ex eam tation laoreet. Decore putant debitis mei eu, ea cum veritus offendit adolescens, modus facete ad usu. No mel nulla vituperata, nostro maiestatis argumentum ";
	public  static StringBuffer TEST_REPORT = new StringBuffer();
	private final int one_million = 1000 ;
    private final int NB_TESTS = 9; //including testAndroidTestCaseSetupPropertly
	private static int runTests = 0;
	
	@Override
	protected void setUp() throws Exception {
		runTests++;
	}

	@Override
	protected void tearDown() throws Exception {
		if (NB_TESTS == runTests) {
			PrintWriter out = new PrintWriter ("/sdcard/benchmark.txt");
			out.print(TEST_REPORT.toString());
			out.flush();
			out.close();
		}
	}

	public void testSDB_FourMegOfText () throws SnappydbException, IOException {
		File file  = new File ("/sdcard/four_meg_text.txt");
		int length = (int) file.length();
		byte[] buffer = new byte[length];
		FileInputStream fis = new FileInputStream(file);
		int nbRead = fis.read(buffer);
		
		assertEquals(length, nbRead);
		
		DB snappyDB = DBFactory.open(getContext(), "four_meg_of_text");
		
		long beginWrite = System.currentTimeMillis();
		snappyDB.put("text", new String (buffer, "UTF-8"));
		long endWrite = System.currentTimeMillis();
		
		snappyDB.close();
		fis.close();
		
		
		snappyDB = DBFactory.open(getContext(), "four_meg_of_text");
		
		long beginRead = System.currentTimeMillis();
		String data = snappyDB.get("text");
		long endRead = System.currentTimeMillis();
		
		snappyDB.close();
		//snappyDB.destroy();
		
		assertEquals(length, data.getBytes("UTF-8").length);
		
		TEST_REPORT.append("[testSDB_FourMegOfText] write in ").append((endWrite-beginWrite)).append("ms\n");
		TEST_REPORT.append("[testSDB_FourMegOfText] read  in ").append((endRead-beginRead)).append("ms\n");
		
		System.out.println("PERF - [testSDB_FourMegOfText] write in " +  (endWrite-beginWrite) + " ms");
		System.out.println("PERF - [testSDB_FourMegOfText] read  in " +  (endRead-beginRead) + " ms");
		
	}
	
	public void testSQLite_FourMegOfText () throws IOException {
		File file  = new File ("/sdcard/four_meg_text.txt");
		int length = (int) file.length();
		byte[] buffer = new byte[length];
		FileInputStream fis = new FileInputStream(file);
		int nbRead = fis.read(buffer);
		fis.close();
		
		assertEquals(length, nbRead);
		
		
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "four_meg_of_text");
		
		SimpleDatabase sqliteDB = new SimpleDatabase(context);
		String CREATE_TABLE = "CREATE TABLE T_STR (STR_VALUE TEXT);";
		String INSERT_STRING = "INSERT INTO T_STR (STR_VALUE) VALUES (?);";
		
		sqliteDB.getWritableDatabase().execSQL(CREATE_TABLE);
		
		long beginWrite = System.currentTimeMillis();
		sqliteDB.getWritableDatabase().execSQL(INSERT_STRING, new String[]{new String (buffer, "UTF-8")});
		long endWrite = System.currentTimeMillis();
		
		/*long beginRead = System.currentTimeMillis();
		Cursor cursor = sqliteDB.getReadableDatabase().query("T_STR", new String[]{"STR_VALUE"}, null, null, null, null, null);
		cursor.moveToFirst();
		String sqliteString = cursor.getString(0);
		long endRead = System.currentTimeMillis();*/
		
		TEST_REPORT.append("[testSQLite_FourMegOfText] write in ").append((endWrite-beginWrite)).append("ms\n");
		TEST_REPORT.append("[testSQLite_FourMegOfText] read  N/A\n");
		
		System.out.println("PERF - [testSQLite_FourMegOfText] write in " +  (endWrite-beginWrite) + " ms");
		System.out.println("PERF - [testSQLite_insertFourMegOfText] read  N/A ");
		
//		assertEquals(length, sqliteString.getBytes("UTF-8").length);
		
		sqliteDB.close();
	}
	
	public void testSDB_OneMillionInt () throws SnappydbException {//4Bytes per int ~ 4MB
		DB db = DBFactory.open(getContext(), "one_million_int");
		
		long beginWrite = System.currentTimeMillis();
		for (int i=0; i<one_million; i++) {
			db.putInt("int_"+i, Integer.MAX_VALUE);
		}
		long endWrite= System.currentTimeMillis();
		db.close();
		
		db = DBFactory.open(getContext(), "one_million_int");
		long beginRead = System.currentTimeMillis();
		for (int i=0; i<one_million; i++) {
			db.getInt("int_"+i);
		}
		long endRead= System.currentTimeMillis();
		
		TEST_REPORT.append("[testSDB_OneMillionInt] write in ").append((endWrite-beginWrite)).append("ms\n");
		TEST_REPORT.append("[testSDB_OneMillionInt] read  in ").append((endRead-beginRead)).append("ms\n");
		
		
		System.out.println("PERF - [testSDB_OneMillionInt] write in " +  (endWrite-beginWrite) + " ms");
		System.out.println("PERF - [testSDB_OneMillionInt] read  in " +  (endRead-beginRead) + " ms");

		db.close();
		
	}
	
	public void testSQLite_OneMillionInt () throws SnappydbException, IOException {//read take forever maybe test first insert time
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "one_million_int");
		
		SimpleDatabase sqliteDB = new SimpleDatabase(context);
		String CREATE_TABLE = "CREATE TABLE T_INT (INT_VALUE TEXT);";
		String INSERT_STRING = "INSERT INTO T_INT (INT_VALUE) VALUES (?);";
		
		sqliteDB.getWritableDatabase().execSQL(CREATE_TABLE);
		
		SQLiteDatabase sql = sqliteDB.getWritableDatabase();
		
		long beginWrite = System.currentTimeMillis();
		for (int i=0; i<one_million; i++) {
			sql.execSQL(INSERT_STRING, new Integer[]{Integer.MAX_VALUE});
		}
		long endWrite = System.currentTimeMillis();
		
		long beginRead = System.currentTimeMillis();
		Cursor cursor = sqliteDB.getReadableDatabase().query("T_INT", new String[]{"INT_VALUE"}, null, null, null, null, null);
		int nbRows = cursor.getCount();
		
		assertEquals(one_million, nbRows);
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			cursor.getInt(0);
		}
		long endRead = System.currentTimeMillis();
		
		TEST_REPORT.append("[testSQLite_OneMillionInt] write in ").append((endWrite-beginWrite)).append("ms\n");
		TEST_REPORT.append("[testSQLite_OneMillionInt] read  in ").append((endRead-beginRead)).append("ms\n");
		
		System.out.println("PERF - [testSQLite_insertOneMillionInt] write in " +  (endWrite-beginWrite) + " ms");
		System.out.println("PERF - [testSQLite_insertOneMillionInt] read  in " +  (endRead-beginRead) + " ms");
		
		sqliteDB.close();
	}
	
	public void testSDB_OneMillionStrings () throws SnappydbException {// String length are 256 ~ 512 bytes 5.12 MB
		DB db = DBFactory.open(getContext(), "one_million_string");
		
		long beginWrite = System.currentTimeMillis();
		for (int i=0; i<one_million; i++) {
			db.put("str_"+i, STRING_OF_256_CHARS);
		}
		long endWrite= System.currentTimeMillis();
		db.close();
		
		db = DBFactory.open(getContext(), "one_million_string");
		long beginRead = System.currentTimeMillis();
		for (int i=0; i<one_million; i++) {
			db.get("str_"+i);
		}
		long endRead= System.currentTimeMillis();
		
		TEST_REPORT.append("[testSDB_OneMillionStrings] write in ").append((endWrite-beginWrite)).append("ms\n");
		TEST_REPORT.append("[testSDB_OneMillionStrings] read  in ").append((endRead-beginRead)).append("ms\n");
		
		System.out.println("PERF - [testSDB_OneMillionStrings] write in " +  (endWrite-beginWrite) + " ms");
		System.out.println("PERF - [testSDB_OneMillionStrings] read  in " +  (endRead-beginRead) + " ms");

		db.close();
	}
	
	public void testSQLite_OneMillionStrings () throws SnappydbException {
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "one_million_string");
		
		SimpleDatabase sqliteDB = new SimpleDatabase(context);
		String CREATE_TABLE = "CREATE TABLE T_STR (STR_VALUE TEXT);";
		String INSERT_STRING = "INSERT INTO T_STR (STR_VALUE) VALUES (?);";
		
		sqliteDB.getWritableDatabase().execSQL(CREATE_TABLE);
		
		SQLiteDatabase sql = sqliteDB.getWritableDatabase();
		
		long beginWrite = System.currentTimeMillis();
		for (int i=0; i<one_million; i++) {
			sql.execSQL(INSERT_STRING, new String[]{STRING_OF_256_CHARS});
		}
		long endWrite = System.currentTimeMillis();
		
		long beginRead = System.currentTimeMillis();
		Cursor cursor = sqliteDB.getReadableDatabase().query("T_STR", new String[]{"STR_VALUE"}, null, null, null, null, null);
		int nbRows = cursor.getCount();
		
		assertEquals(one_million, nbRows);
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			cursor.getInt(0);
		}
		long endRead = System.currentTimeMillis();
		
		TEST_REPORT.append("[testSQLite_OneMillionStrings] write in ").append((endWrite-beginWrite)).append("ms\n");
		TEST_REPORT.append("[testSQLite_OneMillionStrings] read  in ").append((endRead-beginRead)).append("ms\n");
		
		System.out.println("PERF - [testSQLite_OneMillionStrings] write in " +  (endWrite-beginWrite) + " ms");
		System.out.println("PERF - [testSQLite_OneMillionStrings] read  in " +  (endRead-beginRead) + " ms");
		
		sqliteDB.close();
	}

	public void testSDB_10MegOfbytesArray() throws IOException, SnappydbException {//inserting 10 byte[] each 1024*1024 bytes
		//512 KB of binary data
		File file  = new File ("/sdcard/half_meg_text.txt");
		int length = (int) file.length();
		byte[] buffer = new byte[length];
		FileInputStream fis = new FileInputStream(file);
		int nbRead = fis.read(buffer);
		fis.close();
		
		assertEquals(524288, nbRead);
		
		DB snappyDB = DBFactory.open(getContext(), "10_meg_of_bytes");
		
		long beginWrite = System.currentTimeMillis();
		snappyDB.put("binary1", buffer);
		snappyDB.put("binary2", buffer);
		snappyDB.put("binary3", buffer);
		snappyDB.put("binary4", buffer);
		snappyDB.put("binary5", buffer);
		snappyDB.put("binary6", buffer);
		snappyDB.put("binary7", buffer);
		snappyDB.put("binary8", buffer);
		snappyDB.put("binary9", buffer);
		snappyDB.put("binary10", buffer);
		snappyDB.put("binary11", buffer);
		snappyDB.put("binary12", buffer);
		snappyDB.put("binary13", buffer);
		snappyDB.put("binary14", buffer);
		snappyDB.put("binary15", buffer);
		snappyDB.put("binary16", buffer);
		snappyDB.put("binary17", buffer);
		snappyDB.put("binary18", buffer);
		snappyDB.put("binary19", buffer);
		snappyDB.put("binary20", buffer);
		long endWrite = System.currentTimeMillis();
		
		snappyDB.close();
		
		snappyDB = DBFactory.open(getContext(), "10_meg_of_bytes");
		
		long beginRead = System.currentTimeMillis();
		snappyDB.getBytes("binary1");
		snappyDB.getBytes("binary2");
		snappyDB.getBytes("binary3");
		snappyDB.getBytes("binary4");
		snappyDB.getBytes("binary5");
		snappyDB.getBytes("binary6");
		snappyDB.getBytes("binary7");
		snappyDB.getBytes("binary8");
		snappyDB.getBytes("binary9");
		snappyDB.getBytes("binary10");
		snappyDB.getBytes("binary11");
		snappyDB.getBytes("binary12");
		snappyDB.getBytes("binary13");
		snappyDB.getBytes("binary14");
		snappyDB.getBytes("binary15");
		snappyDB.getBytes("binary16");
		snappyDB.getBytes("binary17");
		snappyDB.getBytes("binary18");
		snappyDB.getBytes("binary19");
		snappyDB.getBytes("binary20");
		long endRead = System.currentTimeMillis();
		
		snappyDB.close();
		
		TEST_REPORT.append("[testSDB_10MegOfbytesArray] write in ").append((endWrite-beginWrite)).append("ms\n");
		TEST_REPORT.append("[testSDB_10MegOfbytesArray] read  in ").append((endRead-beginRead)).append("ms\n");

		System.out.println("PERF - [testSDB_10MegOfbytesArray] write in " +  (endWrite-beginWrite) + " ms");
		System.out.println("PERF - [testSDB_10MegOfbytesArray] read  in " +  (endRead-beginRead) + " ms");
		
		
	}
	
	public void testSQLite_10MegOfbytesArray() throws IOException {//inserting 10 byte[] each 1024*1024 bytes (since the limit is 3 meg I think?)
		
		File file  = new File ("/sdcard/half_meg_text.txt");
		int length = (int) file.length();
		byte[] buffer = new byte[length];
		FileInputStream fis = new FileInputStream(file);
		int nbRead = fis.read(buffer);
		fis.close();
		
		assertEquals(524288, nbRead);
		
		RenamingDelegatingContext context = new RenamingDelegatingContext(
				getContext(), "10_meg_of_bytes");
		
		SimpleDatabase sqliteDB = new SimpleDatabase(context);
		String CREATE_TABLE = "CREATE TABLE T_BIN (BIN_VALUE BLOB);";
		String INSERT_STRING = "INSERT INTO T_BIN (BIN_VALUE) VALUES (?);";
		
		sqliteDB.getWritableDatabase().execSQL(CREATE_TABLE);
		
		SQLiteDatabase sql = sqliteDB.getWritableDatabase();
		SQLiteStatement insertStmt = sql.compileStatement(INSERT_STRING);
		
		long beginWrite = System.currentTimeMillis();
		for (int i=0; i<20; i++) {
			insertStmt.bindBlob(1, buffer);
			insertStmt.executeInsert();
		}
		long endWrite = System.currentTimeMillis();
		
		long beginRead = System.currentTimeMillis();
		Cursor cursor = sqliteDB.getReadableDatabase().query("T_BIN", new String[]{"BIN_VALUE"}, null, null, null, null, null);
		int nbRows = cursor.getCount();
		
		assertEquals(20, nbRows);
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			cursor.getBlob(0);
		}
		long endRead = System.currentTimeMillis();
		
		TEST_REPORT.append("[testSQLite_10MegOfbytesArray] write in ").append((endWrite-beginWrite)).append("ms\n");
		TEST_REPORT.append("[testSQLite_10MegOfbytesArray] read  in ").append((endRead-beginRead)).append("ms\n");

		System.out.println("PERF - [testSQLite_10MegOfbytesArray] write in " +  (endWrite-beginWrite) + " ms");
		System.out.println("PERF - [testSQLite_10MegOfbytesArray] read  in " +  (endRead-beginRead) + " ms");
		
		sqliteDB.close();
	}
	
	
	
}
