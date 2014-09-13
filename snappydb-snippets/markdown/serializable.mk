## Insert Serializable
```java
     AtomicInteger objAtomicInt = new AtomicInteger (42);
     snappyDB.put("atomic integer", objAtomicInt);

     public class Employee implements Serializable {
         AtomicInteger id;
         Address address;
     }
     // ..
     public class Address implements Serializable {
         String zipCode;
     }
     // ..

```

## Read Serializable
```java
     AtomicInteger myObject = snappyDB.get("atomic integer", AtomicInteger.class);
```
