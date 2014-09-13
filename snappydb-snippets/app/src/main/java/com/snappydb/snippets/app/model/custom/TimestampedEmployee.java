/*
 * Copyright (C) 2014 Nabil HACHICHA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.snappydb.snippets.app.model.custom;

// POJO used by a custom Kryo Serializer
public class TimestampedEmployee {
    private String name;
    //fields set By the custom Kryo deserializer to indicate the time of serialization/deserialization
    private long serializationDate;
    private long deserializationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSerializationDate() {
        return serializationDate;
    }

    public long getDeserializationDate() {
        return deserializationDate;
    }

    public void setSerializationDate(long serializationDate) {
        this.serializationDate = serializationDate;
    }

    public void setDeserializationDate(long deserializationDate) {
        this.deserializationDate = deserializationDate;
    }
}
