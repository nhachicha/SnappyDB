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

package com.snappydb.sample.tests.api;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicOperations extends AndroidTestCase {
	private DB snappyDB = null;
	private static String dbName = "mysnappydb";

    @SmallTest
	public void testCreateDB () throws SnappydbException  {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		snappyDB.close();
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testTwoInstance () throws SnappydbException {
		DB db1 = DBFactory.open(getContext(), "db1");
		
		try {
			DB db2 = DBFactory.open(getContext(), "db2");
			fail("Should raise exception");
		} catch (SnappydbException e){}

		db1.close();
		db1.destroy();
	}

    @SmallTest
	public void testDestroyDB ()  throws SnappydbException  {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testCreateWithDefaultName ()  throws SnappydbException  {
		snappyDB = DBFactory.open(getContext());
		snappyDB.put("name", "SnappyDB");
		snappyDB.close();
		
		snappyDB = DBFactory.open(getContext());
		assertEquals("SnappyDB", snappyDB.get("name"));
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testDestroyWithoutOpenDB ()  throws SnappydbException {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testString () throws SnappydbException  {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		snappyDB.put("quote", "bazinga!");
		
		snappyDB.close();
		
		snappyDB = DBFactory.open(getContext(), dbName);
		
		String actual = snappyDB.get("quote");
		assertEquals("bazinga!", actual);
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testShort () throws SnappydbException  {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		//final short val = (short) (2<<15);
		final short val = (short)32768;
		
		snappyDB.putShort("myshort", val);
		
		snappyDB.close();
		
		snappyDB = DBFactory.open(getContext(), dbName);
		
		short actual = snappyDB.getShort("myshort");
		assertEquals(val, actual);
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testInt () throws SnappydbException  {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		snappyDB.putInt("max_int", Integer.MAX_VALUE);
		snappyDB.putInt("min_int", Integer.MIN_VALUE);
		snappyDB.putInt("42", 42);
		snappyDB.putInt("7", 7);
		snappyDB.putInt("0", 0);
		snappyDB.putInt("-121324", -121324);
		snappyDB.putInt("121324", 121324);
		
		snappyDB.close();
		
		snappyDB = DBFactory.open(getContext(), dbName);
		
		int max = snappyDB.getInt("max_int");
		int min = snappyDB.getInt("min_int");
		int int_42 = snappyDB.getInt("42");
		int int_7 = snappyDB.getInt("7");
		int int_0 = snappyDB.getInt("0");
		int int_n_121324 = snappyDB.getInt("-121324");
		int int_121324 = snappyDB.getInt("121324");
		
		assertEquals(Integer.MAX_VALUE, max);
		assertEquals(Integer.MIN_VALUE, min);
		assertEquals(42, int_42);
		assertEquals(7, int_7);
		assertEquals(0, int_0);
		assertEquals(-121324, int_n_121324);
		assertEquals(121324, int_121324);
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testLong () throws SnappydbException  {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		snappyDB.putLong("max_long", Long.MAX_VALUE);
		snappyDB.putLong("min_long", Long.MIN_VALUE);
		
		snappyDB.close();
		
		snappyDB = DBFactory.open(getContext(), dbName);
		
		long max = snappyDB.getLong("max_long");
		long min = snappyDB.getLong("min_long");
		
		assertEquals(Long.MAX_VALUE, max);
		assertEquals(Long.MIN_VALUE, min);
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testDouble ()  throws SnappydbException {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		snappyDB.putDouble("max_double", Double.MAX_VALUE);
		snappyDB.putDouble("min_double", Double.MIN_VALUE);
		
		snappyDB.close();
		
		snappyDB = DBFactory.open(getContext(), dbName);
		
		double max = snappyDB.getDouble("max_double");
		double min = snappyDB.getDouble("min_double");
		assertEquals(Double.MAX_VALUE, max, 10e-15);
		assertEquals(Double.MIN_VALUE, min, 10e-15);
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testFloat () throws SnappydbException  {//TODO fix float precision
		snappyDB = DBFactory.open(getContext(), dbName);
		
		snappyDB.putFloat("myfloat", 10.30f);
		
		snappyDB.close();
		snappyDB = DBFactory.open(getContext(), dbName);
		
		float actual = snappyDB.getFloat("myfloat");
		assertEquals(10.30f, actual);
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testBoolean () throws SnappydbException  {
		try {
			snappyDB = DBFactory.open(getContext(), dbName);
			
			snappyDB.putBoolean("myboolean", true);
			
			snappyDB.close();
			
			snappyDB = DBFactory.open(getContext(), dbName);
			
			boolean actual = snappyDB.getBoolean("myboolean");
			assertEquals(true, actual);
			
			snappyDB.destroy();
			
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
	}

    @SmallTest
	public void testSerializable () throws SnappydbException  {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		AtomicInteger objAtomicInt = new AtomicInteger (42);
		BigDecimal objBigdecimal = new BigDecimal("10E8");
		Double objDouble = Double.valueOf(Math.PI);
		snappyDB.put("atomic integer", objAtomicInt);
		snappyDB.put("big decimal", objBigdecimal);
		snappyDB.put("double", objDouble);
		
		snappyDB.close();
		snappyDB = DBFactory.open(getContext(), dbName);
		
		AtomicInteger actualAi = snappyDB.get("atomic integer", AtomicInteger.class);
		BigDecimal actualBd = snappyDB.get("big decimal", BigDecimal.class);
		Double actualDb = snappyDB.get("double", Double.class);
		
		assertEquals(42, actualAi.intValue());
		assertEquals(1000000000, actualBd.intValue());
		assertEquals(Math.PI, actualDb.doubleValue(), 10e-14);
		
		snappyDB.destroy();
		
	}

    @SmallTest
	public void testSerializableArray () throws SnappydbException  {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		Number[] array = {new AtomicInteger (42), new BigDecimal("10E8"), Double.valueOf(Math.PI)};
		snappyDB.put("array", array);
		snappyDB.close();
		snappyDB = DBFactory.open(getContext(), dbName);
		
		Number [] numbers = snappyDB.getArray("array", Number.class);
		assertNotNull(numbers);
		assertEquals(3, numbers.length);
		
		assertEquals(true, (numbers[0] instanceof AtomicInteger));
		assertEquals(42, numbers[0].intValue());
		assertEquals(true, (numbers[1] instanceof BigDecimal));
		assertEquals(1000000000, numbers[1].intValue());
		assertEquals(true, (numbers[2] instanceof Double));
		assertEquals(Math.PI, numbers[2].doubleValue(), 10e-14);
		
		snappyDB.destroy();
	}
	
	@SmallTest
	public void testSerializableArrayFail () throws SnappydbException {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		Number[] array = {new AtomicInteger (42), new BigDecimal("10E8"), Double.valueOf(Math.PI)};
		snappyDB.put("array", array);
		snappyDB.close();
		
		snappyDB = DBFactory.open(getContext(), dbName);
		
		try {
			Object obj = snappyDB.get("array", Number.class);
			fail ("Should fail at this stage, should call getCollection instead of get");
			
		} catch (SnappydbException e) {
			snappyDB.close();

		}
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testDelete ()  throws SnappydbException {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		snappyDB.put("name", "Jack Reacher");
		snappyDB.del("name");
		try {
			snappyDB.get("name");
			fail("should raise SnappydbException string NotFound");
		} catch (SnappydbException e) {}
		
		snappyDB.close();
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testOverrideValue ()  throws SnappydbException {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		snappyDB.put("name", "Jack Reacher");
		snappyDB.put("name", "Lee Child");
		assertEquals("Lee Child", snappyDB.get("name"));
		
		snappyDB.putShort("short_key", (short)7);
		snappyDB.putShort("short_key", (short)19);
		assertEquals((short)19, snappyDB.getShort("short_key"));
		
		snappyDB.putInt("int_key", 19);
		snappyDB.putInt("int_key", 7);
		assertEquals(7, snappyDB.getInt("int_key"));
		
		snappyDB.putFloat("float_key", 19);
		snappyDB.putFloat("float_key", 7);
		assertEquals(7.0f, snappyDB.getFloat("float_key"));
		
		snappyDB.putDouble("double_key", 19);
		snappyDB.putDouble("double_key", 7);
		assertEquals(7.0d, snappyDB.getDouble("double_key"), 10e-3);
		
		snappyDB.putBoolean("boolean_key", true);
		snappyDB.putBoolean("boolean_key", false);
		assertEquals(false, snappyDB.getBoolean("boolean_key"));
		
		snappyDB.put("serializable_key", new AtomicInteger(19));
		snappyDB.put("serializable_key", new AtomicInteger(7));
		assertEquals(7, snappyDB.get("serializable_key", AtomicInteger.class).intValue());
		
		snappyDB.close();
		
		snappyDB.destroy();
	}

    @SmallTest
	public void testNoKey ()  throws SnappydbException {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		try {
			snappyDB.get("UNKNOWN_STRING_KEY");
			fail("should raise SnappydbException string NotFound");
		} catch (SnappydbException e) {}

		snappyDB.close();
		snappyDB.destroy();
	}

    @SmallTest
	public void testKeyExists ()  throws SnappydbException {
		snappyDB = DBFactory.open(getContext(), dbName);
		
		boolean exists = snappyDB.exists("UNKNOWN_STRING_KEY");
		if (exists) fail("key should not exists");
		
		snappyDB.put("name", "jack Reacher");
		exists = snappyDB.exists("name");
		exists = snappyDB.exists("name");
		if(!exists) fail("key should nexists");
		
		exists = snappyDB.exists("nam");
		if (exists) fail("similar key should not exists");
		
		snappyDB.del("name");
		exists = snappyDB.exists("name");
		if (exists) fail("deleted key should not exists");
		
		try {
			exists = snappyDB.exists("");
			fail("empty key should not be allowed");
		} catch (SnappydbException e){}
		
		try {
			exists = snappyDB.exists(null);
			fail("null key should not be allowed");
		} catch (SnappydbException e){}
		
		snappyDB.close();
		snappyDB.destroy();
	}	
}
