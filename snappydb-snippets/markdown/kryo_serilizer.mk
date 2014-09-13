## Register a custom Kryo [Serializer](https://github.com/EsotericSoftware/kryo#default-serializers)
```java
     // Using the Builder constructor
     snappyDB = new SnappyDB.Builder(context)
                 .name("dbName")
                 .registerSerializers(Employee.class, new Serializer<Employee>() {

                     @Override
                     public void write(Kryo kryo, Output output, Employee object) {
                         output.writeString(object.getName());
                     }

                     @Override
                     public Employee read(Kryo kryo, Input input, Class<Employee> type) {
                         Employee emp = new Employee(input.readString());
                         return emp;
                     }
                 }).build();

        Employee employee = new Employee("Dr Zoidberg");
        snappyDB.put("employee", employee);

     // Using an existing SnappyDB connection
     snappyDB.getKryoInstance().register(Employee.class, new Serializer<Employee>() {

                 @Override
                 public void write(Kryo kryo, Output output, Employee object) {
                     output.writeString(object.getName());
                 }

                 @Override
                 public Employee read(Kryo kryo, Input input, Class<Employee> type) {
                     Employee emp = new Employee(input.readString());
                     return emp;
                 }
             });
```
Additional serializers are available [here](https://github.com/magro/kryo-serializers)