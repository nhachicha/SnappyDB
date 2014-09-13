## Insert Object
```java
     class Employee {
         Address address;
         String name;
     }
     class Address {
         String zipCode;
     }
     // ...
     Address office = new Address("W1F 8HT");
     Employee nabil = new Employee("Android Engineer", office);
     snappyDB.put("employee", nabil);

```

## Read Object
```java
     Employee employee = snappyDB.getObject("employee", Employee.class);
     assert employee.getAddress().getZipCode().equals("W1F 8HT");
```
