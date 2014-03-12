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

```xml
<dependency>
  <groupId>com.snappydb</groupId>
  <artifactId>snappydb-api</artifactId>
  <version>0.1.0</version>
</dependency>

<!-- ARM libraries  -->
<dependency>
  <groupId>com.snappydb</groupId>
  <artifactId>snappydb-native</artifactId>
  <version>0.1.0</version>
  <classifier>armeabi</classifier>
  <type>so</type>
</dependency>
<dependency>
  <groupId>com.snappydb</groupId>
  <artifactId>snappydb-native</artifactId>
  <version>0.1.0</version>
  <classifier>armeabi-v7a</classifier>
  <type>so</type>
</dependency>

<!-- x86 library  -->            
<dependency>
  <groupId>com.snappydb</groupId>
  <artifactId>snappydb-native</artifactId>
  <version>0.1.0</version>
  <classifier>x86</classifier>
  <type>so</type>
</dependency>

<!-- MIPS library  -->            
<dependency>
  <groupId>com.snappydb</groupId>
  <artifactId>snappydb-native</artifactId>
  <version>0.1.0</version>
  <classifier>mips</classifier>
  <type>so</type>
</dependency>
```

__For non maven users__, You need to [download](http://snappydb.com/snappydb-0.1.0.zip) a zip containing the native libraries (.so) and the api.copy them under your libs directory

![nomaven](http://snappydb.com/img/snappydb_installation_nomaven.png)

Cookbook
---------

Common tasks snippets

- [Create database](#create-database)
- [Open database](#open-database)
- [Close database](#close-database)
- [Destroy database](#destroy-database)
- [Insert primitive types](#insert-primitive-types) 
- [Read primitive types](#read-primitive-types) 
- [Insert Serializable](#insert-serializable) 
- [Read Serializable](#read-serializable) 
- [Insert Array](#insert-array) 
- [Read Array](#read-array) 
- [Check key](#check-key)
- [Delete key](#delete-key)

### Create database
###### Create using the default name
```java
     DB snappydb = DBFactory.open(context);
```
###### Create with a given name
```java
     DB snappydb = DBFactory.open(context, "books");
```
SnappyDB use the internal storage to create your database. It create a directory containing all the necessary files Ex:
``
/data/data/com.snappydb/files/mydatabse
``

### Open database
###### Open using the default name
```java
     DB snappydb = DBFactory.open(context);
```
###### Open using a given name
```java
     DB snappydb = DBFactory.open(context, "books");
```

### Close database
```java
     snappydb.close();
```

### Destroy database
```java
     snappydb.destroy();
```

### Insert primitive types
```java
     snappyDB.put("quote", "bazinga!");
     
     snappyDB.putShort("myshort", (short)32768);
     
     snappyDB.putInt("max_int", Integer.MAX_VALUE);
     
     snappyDB.putLong("max_long", Long.MAX_VALUE);
     
     snappyDB.putDouble("max_double", Double.MAX_VALUE);
     
     snappyDB.putFloat("myfloat", 10.30f);
     
     snappyDB.putBoolean("myboolean", true);
```

### Read primitive types
```java
     String quote      = snappyDB.get("quote");
     
     short myshort     = snappyDB.getShort("myshort");
     
     int maxInt        = snappyDB.getInt("max_int");
     
     long maxLong      = snappyDB.getLong("max_long");
     
     double maxDouble  = snappyDB.getDouble("max_double", Double.MAX_VALUE);
     
     float myFloat     = snappyDB.getFloat("myfloat");
     
     boolean myBoolean = snappyDB.getBoolean("myboolean");
```
### Insert Serializable 
```java
     AtomicInteger objAtomicInt = new AtomicInteger (42);
     snappyDB.put("atomic integer", objAtomicInt);
```

### Read Serializable 
```java
     AtomicInteger myObject = snappyDB.get("atomic integer", AtomicInteger.class);
```

### Insert Array
```java
     Number[] array = {new AtomicInteger (42), new BigDecimal("10E8"), Double.valueOf(Math.PI)};
     
     snappyDB.put("array", array);
```

### Read Array
```java
     Number [] numbers = snappyDB.getArray("array", Number.class);
```
### Check Key
```java
     boolean isKeyExists = snappyDB.exists("key");
```
### Delete Key
```java
     snappyDB.del("key");
```


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
    
    
<a href="https://twitter.com/nabil_hachicha" class="twitter-follow-button" data-show-count="false">Follow @nabil_hachicha</a>
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-46288191-1', 'github.com');
  ga('send', 'pageview');

</script>    
