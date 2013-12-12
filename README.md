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
if you want, you can download one archive that contain the libraries for all platforme (arm, x86 and mips).

* [__SnappyDB for all platform__](https://github.com/nhachicha/SnappyDB/releases/tag/0.1.0)
* [__SnappyDB for ARM__](https://github.com/nhachicha/SnappyDB/releases/tag/0.1.0_arm)

![installation](http://snappydb.com/img/snappydb_installation.png)



Copy the jars and the native files under your libs directory. you're good to go!

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
