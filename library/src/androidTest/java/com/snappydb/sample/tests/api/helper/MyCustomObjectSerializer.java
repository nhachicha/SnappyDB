package com.snappydb.sample.tests.api.helper;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class MyCustomObjectSerializer extends Serializer<MyCustomObject> {
    @Override
    public void write(Kryo kryo, Output output, MyCustomObject object) {
        output.writeString(object.getMyArg());
    }

    @Override
    public MyCustomObject read(Kryo kryo, Input input, Class type) {
        return new MyCustomObject(input.readString());
    }
}

