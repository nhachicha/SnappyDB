## Prefix keys search
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
## Range keys search

+ both 'FROM' & 'TO' keys exist
```java
keys = snappyDB.findKeysBetween("android:08", "android:11");
assertEquals(3, keys.length);
assertEquals("android:08", keys[0]);
assertEquals("android:09", keys[1]);
assertEquals("android:11", keys[2]);
```

+ `FROM` key exist, but not the `TO
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

