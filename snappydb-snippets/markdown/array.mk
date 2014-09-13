## Insert Array
```java
     Number[] array = {new AtomicInteger (42), new BigDecimal("10E8"), Double.valueOf(Math.PI)};
     
     snappyDB.put("array", array);
```

## Read Array
```java
     Number [] numbers = snappyDB.getObjectArray("array", Number.class);
