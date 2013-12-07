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
import java.math.BigDecimal;
import java.util.Date;

import android.test.AndroidTestCase;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class ErrorHandlingTest extends AndroidTestCase {
	private DB snappyDB = null;
	private static String dbName = "kvdb";

	public void testEveryBodyShouldRaise() {
		try {
			snappyDB = DBFactory.open(getContext(), dbName);
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		// double close
		try {
			snappyDB.close();

		} catch (SnappydbException e) {
			e.printStackTrace();
			fail("first close should not raise an exception");
		}

		try {
			snappyDB.close();
			fail("second close should raise an exception");
		} catch (SnappydbException e) {
			e.printStackTrace();
		}

		// double open
		try {
			snappyDB = DBFactory.open(getContext(), dbName);
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		try {
			snappyDB = DBFactory.open(getContext(), dbName + "another db");
			fail("Should raise exception");

		} catch (SnappydbException e) {
			e.printStackTrace();
		}

		try {
			// re opening the same database again will not throw an exception
			snappyDB = DBFactory.open(getContext(), dbName);

		} catch (SnappydbException e) {
			e.printStackTrace();
			fail(e.getMessage());

		}

		// double destroy

		try {
			snappyDB.destroy();
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		try {
			snappyDB.destroy();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}

		// operations on destroyed db

		try {
			snappyDB.put("bytes_key", new byte[] {});
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.put("serializable_key", new Date());
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.put("array_serialiazable_key", new BigDecimal[] {});
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.put("string_key", "");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putBoolean("boolean_key", false);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putDouble("double_key", Double.NaN);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putFloat("float_key", Float.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putInt("int_key", 1337);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putLong("long_key", Long.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putShort("short_key", Short.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.get("string_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.get("serializable_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("array_serialiazable_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("boolean_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBytes("bytes_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getDouble("double_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getFloat("float_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getInt("int_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("long_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("short_key");
			fail();
		} catch (SnappydbException e) {
		}

		// operations on closed db

		try {
			snappyDB = DBFactory.open(getContext(), dbName);
			snappyDB.close();
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		try {
			snappyDB.put("bytes_key", new byte[] {});
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.put("serializable_key", new Date());
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.put("array_serialiazable_key", new BigDecimal[] {});
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.put("string_key", "");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putBoolean("boolean_key", false);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putDouble("double_key", Double.NaN);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putFloat("float_key", Float.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putInt("int_key", 1337);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putLong("long_key", Long.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putShort("short_key", Short.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.get("string_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.get("serializable_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("array_serialiazable_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("boolean_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBytes("bytes_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getDouble("double_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getFloat("float_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getInt("int_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("long_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("short_key");
			fail();
		} catch (SnappydbException e) {
		}

		// destroy closed db
		try {
			snappyDB.destroy();
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	public void testInvalideEntries() {

		try {
			snappyDB = DBFactory.open(getContext(), dbName);
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		try {
			snappyDB.put("", new byte[] {});
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.put(null, new byte[] {});
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.put("", new Date());
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.put(null, new Date());
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.put("", new BigDecimal[] {});
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.put(null, new BigDecimal[] {});
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.put("", "");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.put(null, "");
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.putBoolean("", false);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putBoolean(null, false);
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.putDouble("", Double.NaN);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putDouble(null, Double.NaN);
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.putFloat("", Float.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putFloat(null, Float.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.putInt("", 1337);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putInt(null, 1337);
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.putLong("", Long.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putLong(null, Long.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.putShort("", Short.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.putShort(null, Short.MAX_VALUE);
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.destroy();
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	public void testWrongGetType() throws SnappydbException,
			UnsupportedEncodingException {
		try {
			snappyDB = DBFactory.open(getContext(), dbName);
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		snappyDB.put("bytes_key", new byte[] { 1, 2, 3 });
		snappyDB.put("serializable_key", new Date());
		snappyDB.put("array_serialiazable_key", new BigDecimal[] {
				BigDecimal.ONE, BigDecimal.ZERO });
		snappyDB.put("string_key", "Everybody Loves Hypnotoad!");
		snappyDB.putBoolean("boolean_key", false);
		snappyDB.putDouble("double_key", Double.NaN);
		snappyDB.putFloat("float_key", Float.MAX_VALUE);
		snappyDB.putInt("int_key", 1337);
		snappyDB.putLong("long_key", Long.MAX_VALUE);
		snappyDB.putShort("short_key", Short.MAX_VALUE);

		// try every get with string
		snappyDB.get("string_key"); // don't fail(get bytes as string)
		try {
			snappyDB.get("string_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("string_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("string_key");
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.getBytes("string_key");// should not fail, as we are getting
										// the String as bytes
		snappyDB.getDouble("string_key");
		snappyDB.getFloat("string_key");
		try {
			snappyDB.getInt("string_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("string_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("string_key");
			fail();
		} catch (SnappydbException e) {
		}

		// try every get with serializable
		try {
			snappyDB.get("serializable_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.get("serializable_key");// don't fail(get bytes as string)
		try {
			snappyDB.getArray("serializable_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("serializable_key");
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.getBytes("serializable_key"); // don't fail reading byte array
												// of serialization
		snappyDB.getDouble("serializable_key");
		snappyDB.getFloat("serializable_key");
		try {
			snappyDB.getInt("serializable_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("serializable_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("serializable_key");
			fail();
		} catch (SnappydbException e) {
		}

		// try every get with serializable array
		snappyDB.getArray("array_serialiazable_key", BigDecimal.class);
		snappyDB.get("array_serialiazable_key");
		snappyDB.get("array_serialiazable_key", BigDecimal.class);// does not
																	// fail but
																	// return
																	// garbage
		try {
			snappyDB.getBoolean("array_serialiazable_key");
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.getBytes("array_serialiazable_key");
		snappyDB.getDouble("array_serialiazable_key");
		snappyDB.getFloat("array_serialiazable_key");
		try {
			snappyDB.getInt("array_serialiazable_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("array_serialiazable_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("array_serialiazable_key");
			fail();
		} catch (SnappydbException e) {
		}

		// try every get with boolean
		snappyDB.getBoolean("boolean_key");
		snappyDB.get("boolean_key");
		try {
			snappyDB.get("boolean_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("boolean_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.getBytes("boolean_key");
		snappyDB.getDouble("boolean_key");
		snappyDB.getFloat("boolean_key");
		try {
			snappyDB.getInt("boolean_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("boolean_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("boolean_key");
			fail();
		} catch (SnappydbException e) {
		}

		// try every get with bytes
		snappyDB.getBytes("bytes_key");
		snappyDB.get("bytes_key");
		try {
			snappyDB.get("bytes_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("bytes_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("bytes_key");
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.getDouble("bytes_key");
		snappyDB.getFloat("bytes_key");
		try {
			snappyDB.getInt("bytes_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("bytes_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("bytes_key");
			fail();
		} catch (SnappydbException e) {
		}

		// try every get with double
		snappyDB.getDouble("double_key");
		snappyDB.get("double_key");
		try {
			snappyDB.get("double_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("double_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("double_key");
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.getBytes("double_key");
		snappyDB.getFloat("double_key");// should not fail, because we can't use
										// data.length() here to make sure of
										// the size of float since it was
										// encoded as string
		try {
			snappyDB.getInt("double_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("double_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("double_key");
			fail();
		} catch (SnappydbException e) {
		}

		// try every get with float
		snappyDB.getFloat("float_key");
		snappyDB.get("float_key");
		try {
			snappyDB.get("float_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("float_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("float_key");
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.getBytes("float_key");
		snappyDB.getDouble("float_key");
		try {
			snappyDB.getInt("float_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("float_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("float_key");
			fail();
		} catch (SnappydbException e) {
		}

		// try every get with int
		snappyDB.getInt("int_key");
		snappyDB.get("int_key");
		try {
			snappyDB.get("int_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("int_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("int_key");
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.getBytes("int_key");
		snappyDB.getDouble("int_key");
		snappyDB.getFloat("int_key");
		try {
			snappyDB.getLong("int_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("int_key");
			fail();
		} catch (SnappydbException e) {
		}

		// try every get with long
		snappyDB.getLong("long_key");
		snappyDB.get("long_key");
		try {
			snappyDB.get("long_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("long_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("long_key");
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.getBytes("long_key");
		snappyDB.getDouble("long_key");
		snappyDB.getFloat("long_key");
		try {
			snappyDB.getInt("long_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("long_key");
			fail();
		} catch (SnappydbException e) {
		}

		// try every get with short
		snappyDB.getShort("short_key");
		snappyDB.get("short_key");
		try {
			snappyDB.get("short_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("short_key", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("short_key");
			fail();
		} catch (SnappydbException e) {
		}
		snappyDB.getBytes("short_key");
		snappyDB.getDouble("short_key");
		snappyDB.getFloat("short_key");
		try {
			snappyDB.getInt("short_key");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("short_key");
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.destroy();
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	public void testInvalideKeys() {
		try {
			snappyDB = DBFactory.open(getContext(), dbName);
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		try {
			snappyDB.get("");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.get("", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getArray("", BigDecimal.class);
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBoolean("");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getBytes("");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getDouble("");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getFloat("");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getInt("");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getLong("");
			fail();
		} catch (SnappydbException e) {
		}
		try {
			snappyDB.getShort("");
			fail();
		} catch (SnappydbException e) {
		}

		try {
			snappyDB.destroy();
		} catch (SnappydbException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
}
