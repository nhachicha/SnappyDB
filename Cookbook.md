Cookbook
========

Common tasks snippets

## Create database

SnappyDB use the internal storage to create your database. It create a directory containing all the necessary files Ex:
``
/data/data/com.snappydb/files/mydatabse
``
##### Create using the default name
```java
     DB snappydb = DBFactory.open(context);
```
##### Create with a given name
```java
     DB snappydb = DBFactory.open(context, "books");
```

## Open database
##### Open using the default name
```java
     DB snappydb = DBFactory.open(context);
```
##### Open using a given name
```java
     DB snappydb = DBFactory.open(context, "books");
```

## Close database
```java
     snappydb.close();
```

## Destroy database
```java
     snappydb.destroy();
```

## Insert primitive types
```java
     snappyDB.put("quote", "bazinga!");
     
     snappyDB.putShort("myshort", (short)32768);
     
     snappyDB.putInt("max_int", Integer.MAX_VALUE);
     
     snappyDB.putLong("max_long", Long.MAX_VALUE);
     
     snappyDB.putDouble("max_double", Double.MAX_VALUE);
     
     snappyDB.putFloat("myfloat", 10.30f);
     
     snappyDB.putBoolean("myboolean", true);
```

## Read primitive types
```java
     String quote      = snappyDB.get("quote");
     
     short myshort     = snappyDB.getShort("myshort");
     
     int maxInt        = snappyDB.getInt("max_int");
     
     long maxLong      = snappyDB.getLong("max_long");
     
     double maxDouble  = snappyDB.getDouble("max_double", Double.MAX_VALUE);
     
     float myFloat     = snappyDB.getFloat("myfloat");
     
     boolean myBoolean = snappyDB.getBoolean("myboolean");
```
## Insert Serializable 
```java
     AtomicInteger objAtomicInt = new AtomicInteger (42);
     snappyDB.put("atomic integer", objAtomicInt);
```

## Read Serializable 
AtomicInteger actualAi = snappyDB.get("atomic integer", AtomicInteger.class);
## Insert Array

## Read Array

Usage
-----

 ```java
try {
	DB snappydb = DBFactory.open(context); //create or open an existing databse using the default name
	
	snappydb.put("name", "Jack Reacher"); 
	snappydb.putInt("age", 42);  
	snappydb.putBoolean("single", true);
	snappydb.put("books", new String[]{"One Shot", "Tripwire", "61 Hours"}); 
	
	String 	 name   =  snappydb.get("name");
	int 	 age    =  snappydb.getInt("age");
	boolean  single =  snappydb.getBoolean("single");
	String[] books  =  snappydb.getArray("books", String.class);// get array of string
		
	snappydb.close();
	
	} catch (SnappydbException e) {
	}
 ```

For more recipes please take a look at the [Cookbook](./Cookbook.md).

With SnappyDB you could seamlessly store and retrieve your object/array, it use [Kryo](https://github.com/EsotericSoftware/kryo) serialization which it [faster](https://github.com/eishay/jvm-serializers/wiki) than regular Java serialization.


Installation
------------
SnappyDB use native code for performance, it's available for the three main architecture of Android: ARM, x86 and mips.
if you want, you can download one archive that contain the libraries for all platforme (arm, x86 and mips).

* [__SnappyDB for all platform__](https://github.com/nhachicha/SnappyDB/releases/tag/0.1.0)
* [__SnappyDB for ARM__](https://github.com/nhachicha/SnappyDB/releases/tag/0.1.0_arm)

![installation](http://snappydb.com/img/snappydb_installation.png)



Copy the jars and the native files under your libs directory. your good to go!

_SnappyDB is an Adroid Library project, you can also import it as a library dependency_



License
--------
SnappyDB is opensource, contribution and feedback are welcomed

    Copyright 2013 Nabil HACHICHA.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
