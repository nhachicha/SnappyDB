## Create/Open database
##### Using the default name/location
```java
     DB snappydb = DBFactory.open(context);
```
##### With a given name
```java
     DB snappydb = DBFactory.open(context, "books");
```
SnappyDB use the internal storage to create your database.
It create a directory containing all the necessary files Ex:
``
/data/data/com.snappydb/files/mydatabse
``
##### Using builder pattern
```java
     DB snappyDB = new SnappyDB.Builder(context)
                        .directory(Environment.getExternalStorageDirectory().getAbsolutePath()) //optional
                        .name("books")//optional
                        .build();
```
`directory` Specify the location of the database (*sdcard* in this example)
## Close database
```java
     snappydb.close();
```

## Destroy database
```java
     snappydb.destroy();
```