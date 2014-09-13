package com.snappydb.sample.tests.api.helper;

/**
 * Created by Nabil on 10/09/14.
 */
public class Employee {
    private Address address;
    private String name;

    public Employee(String zipCode, String name) {
        this.address = new Address();
        this.address.zipCode = zipCode;
        this.name = name;
    }

    public String getZipCode() {
        return address.zipCode;
    }

    public String getName() {
        return name;
    }

    //Inner class
     class Address {
        String zipCode;
    }
}
