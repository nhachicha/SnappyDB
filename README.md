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

For more recipes please take a look at the [Cookbook](#cookbook).

With SnappyDB you could seamlessly store and retrieve your object/array, it uses [Kryo](https://github.com/EsotericSoftware/kryo) serialization which is [faster](https://github.com/eishay/jvm-serializers/wiki) than the regular Java serialization.


Installation
------------
SnappyDB uses native code for performance, it's available as an [Android Library Project](http://tools.android.com/tech-docs/new-build-system/aar-format) `AAR`.

```groovy
dependencies {
    compile 'com.snappydb:snappydb-lib:0.4.+'
    compile 'com.esotericsoftware.kryo:kryo:2.24.0'
}
```

Cookbook
---------

Common tasks snippets

- [Open/Create database](#create-database)
- [Close database](#close-database)
- [Destroy database](#destroy-database)
- [Insert primitive types](#insert-primitive-types) 
- [Read primitive types](#read-primitive-types) 
- [Insert Serializable](#insert-serializable) 
- [Insert Object](#insert-object)
- [Read Serializable](#read-serializable) 
- [Read Object](#read-object)
- [Insert Array](#insert-array) 
- [Read Array](#read-array) 
- [Check key](#check-key)
- [Delete key](#delete-key)
- [Keys Search](#keys-search)


### Create database
###### Create using the default name
```java
 DB snappydb = DBFactory.open(context);
```
###### Create with a given name
```java
 DB snappydb = DBFactory.open(context, "books");
```
SnappyDB use the internal storage to create your database. It creates a directory containing all the necessary files Ex:
``
/data/data/com.snappydb/files/mydatabse
``
###### Using the builder pattern
```java
 DB snappyDB = new SnappyDB.Builder(context)
                    .directory(Environment.getExternalStorageDirectory().getAbsolutePath()) //optional
                    .name("books")//optional
                    .build();
```
`directory` Specify the location of the database (*sdcard* in this example)

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

double maxDouble  = snappyDB.getDouble("max_double");

float myFloat     = snappyDB.getFloat("myfloat");

boolean myBoolean = snappyDB.getBoolean("myboolean");
```
### Insert Serializable 
```java
AtomicInteger objAtomicInt = new AtomicInteger (42);
snappyDB.put("atomic integer", objAtomicInt);
```
### Insert Object 
```java
MyPojo pojo = new MyPojo ();
snappyDB.put("my_pojo", pojo);
```
*Note: `MyPojo` __doesn't have__ to implement `Serializable` interface*

### Read Serializable 
```java
 AtomicInteger myObject = snappyDB.get("atomic integer", AtomicInteger.class);
```

### Read Object 
```java
MyPojo myObject = snappyDB.getObject("non_serializable", MyPojo.class);
```
*Note: `MyPojo` __doesn't have__ to implement `Serializable` interface*

### Insert Array
```java
Number[] array = {new AtomicInteger (42), new BigDecimal("10E8"), Double.valueOf(Math.PI)};

snappyDB.put("array", array);
```

### Read Array
```java
Number [] numbers = snappyDB.getObjectArray("array", Number.class);
```
### Check Key
```java
boolean isKeyExist = snappyDB.exists("key");
```
### Delete Key
```java
snappyDB.del("key");
```
### Keys Search
###### By Prefix
```java
snappyDB.put("android:03", "Cupcake"); // adding 0 to maintain lexicographical order
snappyDB.put("android:04", "Donut");
snappyDB.put("android:05", "Eclair");
snappyDB.put("android:08", "Froyo");
snappyDB.put("android:09", "Gingerbread");
snappyDB.put("android:11", "Honeycomb");
snappyDB.put("android:14", "Ice Cream Sandwich");
snappyDB.put("android:16", "Jelly Bean");
snappyDB.put("android:19", "KitKat");

String [] keys = snappyDB.findKeys("android");
assert keys.length == 9;

keys = snappyDB.findKeys("android:0");
assert keys.length == 5;

assert snappyDB.get(keys[0]).equals("Cupcake");
assert snappyDB.get(keys[1]).equals("Donut");
assert snappyDB.get(keys[2]).equals("Eclair");
assert snappyDB.get(keys[3]).equals("Froyo");
assert snappyDB.get(keys[4]).equals("Gingerbread");

keys = snappyDB.findKeys("android:1");
assert keys.length == 4;

assert snappyDB.get(keys[0]).equals("Honeycomb");
assert snappyDB.get(keys[1]).equals("Ice Cream Sandwich");
assert snappyDB.get(keys[2]).equals("Jelly Bean");
assert snappyDB.get(keys[3]).equals("KitKat");

```
###### By Range [from .. to]
+ both 'FROM' & 'TO' keys exist
```java
keys = snappyDB.findKeysBetween("android:08", "android:11");
assertEquals(3, keys.length);
assertEquals("android:08", keys[0]);
assertEquals("android:09", keys[1]);
assertEquals("android:11", keys[2]);
```

+ 'FROM' key exist, but not the `TO
```java
keys = snappyDB.findKeysBetween("android:05", "android:10");
assertEquals(3, keys.length);
assertEquals("android:05", keys[0]);
assertEquals("android:08", keys[1]);
assertEquals("android:09", keys[2]);
```

+ 'FROM' key doesn't exist but the 'TO' key do
```java
keys = snappyDB.findKeysBetween("android:07", "android:09");
assertEquals(2, keys.length);
assertEquals("android:08", keys[0]);
assertEquals("android:09", keys[1]);
```

+ both 'FROM' & 'TO' keys doesn't exist
```java
keys = snappyDB.findKeysBetween("android:13", "android:99");
assertEquals(3, keys.length);
assertEquals("android:14", keys[0]);
assertEquals("android:16", keys[1]);
assertEquals("android:19", keys[2]);
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
