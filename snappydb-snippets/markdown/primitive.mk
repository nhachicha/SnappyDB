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
     String  quote     = snappyDB.get("quote");
     short   myshort   = snappyDB.getShort("myshort");
     int     maxInt    = snappyDB.getInt("max_int");
     long    maxLong   = snappyDB.getLong("max_long");
     double  maxDouble = snappyDB.getDouble("max_double");
     float   myFloat   = snappyDB.getFloat("myfloat");
     boolean myBoolean = snappyDB.getBoolean("myboolean");
```
## Check Key existence
```java
     boolean isKeyExists = snappyDB.exists("key");
```
## Delete Key
```java
     snappyDB.del("key");
```
