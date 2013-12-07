Cookbook
========

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

## Create database
##### Create using the default name
```java
     DB snappydb = DBFactory.open(context);
```
##### Create with a given name
```java
     DB snappydb = DBFactory.open(context, "books");
```
SnappyDB use the internal storage to create your database. It create a directory containing all the necessary files Ex:
``
/data/data/com.snappydb/files/mydatabse
``

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
```java
     AtomicInteger myObject = snappyDB.get("atomic integer", AtomicInteger.class);
```

## Insert Array
```java
     Number[] array = {new AtomicInteger (42), new BigDecimal("10E8"), Double.valueOf(Math.PI)};
     
     snappyDB.put("array", array);
```

## Read Array
```java
     Number [] numbers = snappyDB.getArray("array", Number.class);
```
