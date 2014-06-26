package com.snappydb.sample.tests.api.helper;

import java.io.Serializable;

/**
 * We need an object without default constructor in order to test registration of custom serializer on kryo instance
 */
public class MyCustomObject implements Serializable {

    private final String myArg;

    public MyCustomObject(String myArg) {
        this.myArg = myArg;
    }

    public String getMyArg() {
        return myArg;
    }
}