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

import com.esotericsoftware.kryo.Serializer;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;
import com.snappydb.sample.tests.api.helper.MyCustomObject;
import com.snappydb.sample.tests.api.helper.MyCustomObjectSerializer;

import java.io.File;
import java.math.BigDecimal;
import java.util.BitSet;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

import de.javakaffee.kryoserializers.BitSetSerializer;
import de.javakaffee.kryoserializers.GregorianCalendarSerializer;

public class BasicOperations extends AndroidTestCase {
    private static String dbName = "mysnappydb";
    private DB snappyDB = null;

    @SmallTest
    public void testCreateDB() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        snappyDB.close();

        snappyDB.destroy();
    }

    @SmallTest
    public void testCreateUsingCustomKryoSerializers() throws SnappydbException {

        String path = getContext().getFilesDir().getAbsolutePath() + File.separator + dbName;
        MyCustomObject customObject = new MyCustomObject("value");

        snappyDB = new SnappyDB.Builder(getContext())
                        .directory(path)
                        .registerSerializers(MyCustomObject.class, new MyCustomObjectSerializer())
                        .build();

        snappyDB.put("customObject", customObject);
        snappyDB.close();

        snappyDB = new SnappyDB.Builder(getContext())
                .directory(path)
                .build();
        //alternate way to register Serializer
        snappyDB.getKryoInstance().register(MyCustomObject.class, new MyCustomObjectSerializer());

        MyCustomObject retrievedCustomObject = snappyDB.get("customObject", MyCustomObject.class);
        assertEquals(customObject.getMyArg(), retrievedCustomObject.getMyArg());
        snappyDB.close();

        snappyDB.destroy();
    }

    @SmallTest
    public void testCreateDBUsingBuilderWithCustomKryoInstance() throws SnappydbException {
        snappyDB = new SnappyDB.Builder(getContext())
                .registerSerializers(BitSet.class, new BitSetSerializer())
                .registerSerializers(GregorianCalendar.class, new GregorianCalendarSerializer())
                .build();

        Serializer bitSetSerializer = snappyDB.getKryoInstance().getRegistration(BitSet.class).getSerializer();
        Serializer gregorianCalSerializer = snappyDB.getKryoInstance().getRegistration(GregorianCalendar.class).getSerializer();

        assertNotNull(snappyDB);
        assertNotNull(snappyDB.getKryoInstance());

        assertTrue(bitSetSerializer instanceof BitSetSerializer);
        assertTrue(gregorianCalSerializer instanceof GregorianCalendarSerializer);

        snappyDB.close();
        snappyDB.destroy();
    }
    
    @SmallTest
    public void testCreateDBUsingDefaultBuilder() throws SnappydbException {
        snappyDB = SnappyDB.with(getContext());
        assertNotNull(snappyDB);
        assertNotNull(snappyDB.getKryoInstance());

        snappyDB.put("name", "Jack Reacher");
        assertEquals("Jack Reacher", snappyDB.get("name"));
        snappyDB.close();
        snappyDB.destroy();
    }

    @SmallTest
    public void testDefaultBuilderIsSingleton() throws SnappydbException {
        DB dbInstance1 = SnappyDB.with(getContext());
        DB dbInstance2 = SnappyDB.with(getContext());

        assertEquals(dbInstance1, dbInstance2);

        dbInstance1.destroy();
    }

    @SmallTest
    public void testDefaultBuilder() throws SnappydbException {
        DB dbInstance = SnappyDB.with(getContext());

        assertTrue(dbInstance.isOpen());

        dbInstance.close();

        assertFalse(dbInstance.isOpen());

        assertTrue(SnappyDB.with(getContext()).isOpen());//create new instance

        SnappyDB.with(getContext()).destroy();
    }

    @SmallTest
    public void testCreateDBUsingBuilderWithDefaults() throws SnappydbException {
        snappyDB = new SnappyDB.Builder(getContext()).build();
        assertNotNull(snappyDB);

        snappyDB.close();
        snappyDB.destroy();
    }

    @SmallTest
    public void testCreateDBUsingBuilderWithName() throws SnappydbException {
        snappyDB = new SnappyDB.Builder(getContext())
                .name("db1")
                .build();
        assertNotNull(snappyDB);
        assertNotNull(snappyDB.getKryoInstance());

        snappyDB.close();
        snappyDB.destroy();
    }

    @SmallTest
    public void testCreateDBUsingBuilderWithDirectory() throws SnappydbException {
        snappyDB = new SnappyDB.Builder(getContext())
                .directory(getContext().getFilesDir().getAbsolutePath() + File.separator + "dir_db1")
                .build();
        assertNotNull(snappyDB);
        assertNotNull(snappyDB.getKryoInstance());
        snappyDB.close();
        snappyDB.destroy();
    }

    @SmallTest
    public void testCreateDBUsingBuilderWithNameAndDirectory() throws SnappydbException {
        snappyDB = new SnappyDB.Builder(getContext())
                .name("db1")
                .directory(getContext().getFilesDir().getAbsolutePath() + File.separator + "dir_db1")
                .build();
        assertNotNull(snappyDB);
        assertNotNull(snappyDB.getKryoInstance());

        snappyDB.close();
        snappyDB.destroy();
    }

    @SmallTest
    public void testTwoInstance() throws SnappydbException {
        DB db1 = DBFactory.open(getContext(), "db1");

        try {
            DB db2 = DBFactory.open(getContext(), "db2");
            fail("Should raise exception");
        } catch (SnappydbException e) {
        }

        db1.close();
        db1.destroy();
    }

    @SmallTest
    public void testDestroyDB() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        snappyDB.destroy();
    }

    @SmallTest
    public void testCreateWithDefaultName() throws SnappydbException {
        snappyDB = DBFactory.open(getContext());
        snappyDB.put("name", "SnappyDB");
        snappyDB.close();

        snappyDB = DBFactory.open(getContext());
        assertEquals("SnappyDB", snappyDB.get("name"));

        snappyDB.destroy();
    }

    @SmallTest
    public void testDestroyWithoutOpenDB() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        snappyDB.destroy();
    }

    @SmallTest
    public void testString() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        snappyDB.put("quote", "bazinga!");

        snappyDB.close();

        snappyDB = DBFactory.open(getContext(), dbName);

        String actual = snappyDB.get("quote");
        assertEquals("bazinga!", actual);

        snappyDB.destroy();
    }

    @SmallTest
    public void testShort() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        //final short val = (short) (2<<15);
        final short val = (short) 32768;

        snappyDB.putShort("myshort", val);

        snappyDB.close();

        snappyDB = DBFactory.open(getContext(), dbName);

        short actual = snappyDB.getShort("myshort");
        assertEquals(val, actual);

        snappyDB.destroy();
    }

    @SmallTest
    public void testInt() throws SnappydbException {
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
    public void testLong() throws SnappydbException {
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
    public void testDouble() throws SnappydbException {
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
    public void testFloat() throws SnappydbException {//TODO fix float precision
        snappyDB = DBFactory.open(getContext(), dbName);

        snappyDB.putFloat("myfloat", 10.30f);

        snappyDB.close();
        snappyDB = DBFactory.open(getContext(), dbName);

        float actual = snappyDB.getFloat("myfloat");
        assertEquals(10.30f, actual);

        snappyDB.destroy();
    }

    @SmallTest
    public void testBoolean() throws SnappydbException {
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
    public void testSerializable() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        AtomicInteger objAtomicInt = new AtomicInteger(42);
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
    public void testSerializableArray() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        Number[] array = {new AtomicInteger(42), new BigDecimal("10E8"), Double.valueOf(Math.PI)};
        snappyDB.put("array", array);
        snappyDB.close();
        snappyDB = DBFactory.open(getContext(), dbName);

        Number[] numbers = snappyDB.getArray("array", Number.class);
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
    public void testSerializableArrayFail() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        Number[] array = {new AtomicInteger(42), new BigDecimal("10E8"), Double.valueOf(Math.PI)};
        snappyDB.put("array", array);
        snappyDB.close();

        snappyDB = DBFactory.open(getContext(), dbName);

        try {
            Object obj = snappyDB.get("array", Number.class);
            fail("Should fail at this stage, should call getCollection instead of get");

        } catch (SnappydbException e) {
            snappyDB.close();

        }

        snappyDB.destroy();
    }

    @SmallTest
    public void testDelete() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        snappyDB.put("name", "Jack Reacher");
        snappyDB.del("name");
        try {
            snappyDB.get("name");
            fail("should raise SnappydbException string NotFound");
        } catch (SnappydbException e) {
        }

        snappyDB.close();

        snappyDB.destroy();
    }

    @SmallTest
    public void testOverrideValue() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        snappyDB.put("name", "Jack Reacher");
        snappyDB.put("name", "Lee Child");
        assertEquals("Lee Child", snappyDB.get("name"));

        snappyDB.putShort("short_key", (short) 7);
        snappyDB.putShort("short_key", (short) 19);
        assertEquals((short) 19, snappyDB.getShort("short_key"));

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
    public void testNoKey() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        try {
            snappyDB.get("UNKNOWN_STRING_KEY");
            fail("should raise SnappydbException string NotFound");
        } catch (SnappydbException e) {
        }

        snappyDB.close();
        snappyDB.destroy();
    }

    @SmallTest
    public void testKeyExists() throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        boolean exists = snappyDB.exists("UNKNOWN_STRING_KEY");
        if (exists) fail("key should not exists");

        snappyDB.put("name", "jack Reacher");
        exists = snappyDB.exists("name");
        exists = snappyDB.exists("name");
        if (!exists) fail("key should nexists");

        exists = snappyDB.exists("nam");
        if (exists) fail("similar key should not exists");

        snappyDB.del("name");
        exists = snappyDB.exists("name");
        if (exists) fail("deleted key should not exists");

        try {
            exists = snappyDB.exists("");
            fail("empty key should not be allowed");
        } catch (SnappydbException e) {
        }

        try {
            exists = snappyDB.exists(null);
            fail("null key should not be allowed");
        } catch (SnappydbException e) {
        }

        snappyDB.close();
        snappyDB.destroy();
    }

    public void testKeyRange () throws SnappydbException {
        snappyDB = DBFactory.open(getContext(), dbName);

        snappyDB.put("key:cat1:subcatg1", "value:cat1:subcatg1");
        snappyDB.put("key:cat1:subcatg2", "value:cat1:subcatg2");
        snappyDB.put("key:cat1:subcatg3", "value:cat1:subcatg3");

        snappyDB.put("key:cat2:subcatg1", "value:cat2:subcatg1");
        snappyDB.put("key:cat2:subcatg2", "value:cat2:subcatg2");
        snappyDB.put("key:cat2:subcatg3", "value:cat2:subcatg3");

        snappyDB.put("key:cat3:subcatg1", "value:cat3:subcatg1");
        snappyDB.put("key:cat3:subcatg2", "value:cat3:subcatg2");
        snappyDB.put("key:cat3:subcatg3", "value:cat3:subcatg3");

        // return all keys starting with "key" 9
        String [] keys = snappyDB.findKeys("key");
        assertEquals(9, keys.length);
        //suppose the result is sorted
        assertEquals("key:cat1:subcatg1", keys[0]);
        assertEquals(true, snappyDB.exists("key:cat1:subcatg1"));

        assertEquals("key:cat1:subcatg2", keys[1]);
        assertEquals(true, snappyDB.exists("key:cat1:subcatg2"));

        assertEquals("key:cat1:subcatg3", keys[2]);
        assertEquals(true, snappyDB.exists("key:cat1:subcatg3"));

        assertEquals("key:cat2:subcatg1", keys[3]);
        assertEquals(true, snappyDB.exists("key:cat2:subcatg1"));

        assertEquals("key:cat2:subcatg2", keys[4]);
        assertEquals(true, snappyDB.exists("key:cat2:subcatg2"));

        assertEquals("key:cat2:subcatg3", keys[5]);
        assertEquals(true, snappyDB.exists("key:cat2:subcatg3"));

        assertEquals("key:cat3:subcatg1", keys[6]);
        assertEquals(true, snappyDB.exists("key:cat3:subcatg1"));

        assertEquals("key:cat3:subcatg2", keys[7]);
        assertEquals(true, snappyDB.exists("key:cat3:subcatg2"));

        assertEquals("key:cat3:subcatg3", keys[8]);
        assertEquals(true, snappyDB.exists("key:cat3:subcatg3"));


        //return all keys starting with"key:cat" 9
        keys = snappyDB.findKeys("key:cat");
        assertEquals(9, keys.length);
        assertEquals("key:cat1:subcatg1", keys[0]);
        assertEquals("key:cat1:subcatg2", keys[1]);
        assertEquals("key:cat1:subcatg3", keys[2]);
        assertEquals("key:cat2:subcatg1", keys[3]);
        assertEquals("key:cat2:subcatg2", keys[4]);
        assertEquals("key:cat2:subcatg3", keys[5]);
        assertEquals("key:cat3:subcatg1", keys[6]);
        assertEquals("key:cat3:subcatg2", keys[7]);
        assertEquals("key:cat3:subcatg3", keys[8]);

        keys = snappyDB.findKeys("key:");
        assertEquals(9, keys.length);
        assertEquals("key:cat1:subcatg1", keys[0]);
        assertEquals("key:cat1:subcatg2", keys[1]);
        assertEquals("key:cat1:subcatg3", keys[2]);
        assertEquals("key:cat2:subcatg1", keys[3]);
        assertEquals("key:cat2:subcatg2", keys[4]);
        assertEquals("key:cat2:subcatg3", keys[5]);
        assertEquals("key:cat3:subcatg1", keys[6]);
        assertEquals("key:cat3:subcatg2", keys[7]);
        assertEquals("key:cat3:subcatg3", keys[8]);

        //return all keys starting with"key:cat1" 3
        keys = snappyDB.findKeys("key:cat1");
        assertEquals(3, keys.length);
        assertEquals("key:cat1:subcatg1", keys[0]);
        assertEquals("key:cat1:subcatg2", keys[1]);
        assertEquals("key:cat1:subcatg3", keys[2]);


//		//return all keys starting with"key:cat2" 3
        keys = snappyDB.findKeys("key:cat2");
        assertEquals(3, keys.length);
        assertEquals("key:cat2:subcatg1", keys[0]);
        assertEquals("key:cat2:subcatg2", keys[1]);
        assertEquals("key:cat2:subcatg3", keys[2]);

//		//return all keys starting with"key:cat3" 3
        keys = snappyDB.findKeys("key:cat3");
        assertEquals(3, keys.length);
        assertEquals("key:cat3:subcatg1", keys[0]);
        assertEquals("key:cat3:subcatg2", keys[1]);
        assertEquals("key:cat3:subcatg3", keys[2]);

//
//		//return all keys starting with"key:cat1:" 3
        keys = snappyDB.findKeys("key:cat1:");
        assertEquals(3, keys.length);
        assertEquals("key:cat1:subcatg1", keys[0]);
        assertEquals("key:cat1:subcatg2", keys[1]);
        assertEquals("key:cat1:subcatg3", keys[2]);

        keys = snappyDB.findKeys("key:cat1:sub");
        assertEquals(3, keys.length);
        assertEquals("key:cat1:subcatg1", keys[0]);
        assertEquals("key:cat1:subcatg2", keys[1]);
        assertEquals("key:cat1:subcatg3", keys[2]);

        //return all keys starting with"key:cat2:subcatg" 3
        keys = snappyDB.findKeys("key:cat2:subcatg");
        assertEquals(3, keys.length);
        assertEquals("key:cat2:subcatg1", keys[0]);
        assertEquals("key:cat2:subcatg2", keys[1]);
        assertEquals("key:cat2:subcatg3", keys[2]);

        //return all keys starting with"key:cat3:subcatg" 3
        keys = snappyDB.findKeys("key:cat3:subcatg");
        assertEquals(3, keys.length);
        assertEquals("key:cat3:subcatg1", keys[0]);
        assertEquals("key:cat3:subcatg2", keys[1]);
        assertEquals("key:cat3:subcatg3", keys[2]);


        keys = snappyDB.findKeys("UNDEFINED_KEY");
        assertEquals(0, keys.length);

        keys = snappyDB.findKeys("key:xxx");
        assertEquals(0, keys.length);

        keys = snappyDB.findKeys("xxx:key");
        assertEquals(0, keys.length);

        keys = snappyDB.findKeys("key:cat1:subcategory");
        assertEquals(0, keys.length);

        // return all keys starting between [arg1, arg2)
        keys = snappyDB.findKeysBetween("key:cat1:subcatg1", "key:cat1:subcatg3");
        assertEquals(2, keys.length);
        assertEquals("key:cat1:subcatg1", keys[0]);
        assertEquals("key:cat1:subcatg2", keys[1]);

        keys = snappyDB.findKeysBetween("key:cat1:", "key:cat2:subcatg2");
        assertEquals(4, keys.length);
        assertEquals("key:cat1:subcatg1", keys[0]);
        assertEquals("key:cat1:subcatg2", keys[1]);
        assertEquals("key:cat1:subcatg3", keys[2]);
        assertEquals("key:cat2:subcatg1", keys[3]);

        snappyDB.destroy();
    }

}
