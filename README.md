SnappyDB
========

SnappyDB is a __key-value__ database for Android 
it's an alternative for _SQLite_ if you want to use a __NoSQL__ approach.

It allows you to store and get _primitive types_, but also a _Serializable_ object or array in a __type-safe__ way.

SnappyDB can outperform _SQLite_ in read/write operations.
![benchmark](http://snappydb.com/img/benchmark_sqlite_with_transaction.png)


SnappyDB is based on [leveldb](https://code.google.com/p/leveldb/) and use [snappy compression](https://code.google.com/p/snappy/) algorithm, on redundant content you could achieve a good compression ratio 




Usage
-----

 ```java
try {
	DB snappydb = DBFactory.open(context); //create or open an existing databse using the default name
	
	snappydb.put("name", "Jack Reacher"); 
	snappydb.putInt("age", 42); // 
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

For more recipes please take a look at the [Cookbook](./Cookbook).

With SnappyDB you could seamlessly store and retrieve your object/array, it use [Kryo](https://github.com/EsotericSoftware/kryo) serialization which it [faster](https://github.com/eishay/jvm-serializers/wiki) than regular Java serialization.


Installation
------------
SnappyDB use native code for performance, it's available for the three main architecture of Android: ARM, x86 and mips.
if you want, you can download one archive that contain the libraries for all platforme (arm, x86 and mips).

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
