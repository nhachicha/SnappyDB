/*
 * Copyright (C) 2013 Nabil HACHICHA.
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

package com.snappydb;

import com.esotericsoftware.kryo.Kryo;

import java.io.Serializable;

public interface DB {
    //******************************************************************************************************************
    //*      DB MANAGEMENT
    //******************************************************************************************************************

    /**
     * Closes database.
     *
     *  @throws SnappydbException
     */
    void close ()  throws SnappydbException;

    /**
     * Destroys database
     *
     *  @throws SnappydbException
     */
    void destroy ()  throws SnappydbException;

    /**
     * Checks if database is open.
     *
     * @return {@code true} if database is open.
     */
    boolean isOpen ()  throws SnappydbException;

    //******************************************************************************************************************
    //*      CREATE
    //******************************************************************************************************************

    /**
     * Puts the byte array data for the key.
     *
     * @param key not null.
     * @param data not null.
     * @throws SnappydbException if the key or data is null.
     */
    void put (String key, byte[] data) throws SnappydbException;

    /**
     * Puts the {@link String} value for the key.
     *
     * @param key not null.
     * @param value not null.
     * @throws SnappydbException if the key or value is null.
     */
    void put (String key, String value) throws SnappydbException;

    /**
     * Puts the {@link Serializable} value for the key.
     *
     * @param key not null.
     * @param value not null.
     * @throws SnappydbException if the key or value is null.
     */
    void put (String key, Serializable value) throws SnappydbException;

    /**
     * Puts the {@link Serializable} value for the key.
     *
     * @param key not null.
     * @param value not null.
     * @throws SnappydbException if the key or value is null.
     */
    void put (String key, Serializable [] value) throws SnappydbException;

    /**
     * Puts the {@link Object} for the key.
     *
     * @param key not null.
     * @param object not null.
     * @throws SnappydbException if the key or object is null.
     */
    void put (String key, Object object) throws SnappydbException;

    /**
     * Puts the {@link Object} array for the key.
     *
     * @param key not null.
     * @param object not null.
     * @throws SnappydbException if the key is null.
     */
    void put (String key, Object [] object) throws SnappydbException;

    /**
     * Puts the primitive integer for the key.
     *
     * @param key not null.
     * @param val
     * @throws SnappydbException if the key is null.
     */
    void putInt (String key, int val) throws SnappydbException;

    /**
     * Puts the short short for the key.
     *
     * @param key not null.
     * @param val
     * @throws SnappydbException if the key is null.
     */
    void putShort (String key, short val) throws SnappydbException;

    /**
     * Puts the primitive boolean for the key.
     *
     * @param key not null.
     * @param val
     * @throws SnappydbException if the key is null.
     */
    void putBoolean (String key, boolean val) throws SnappydbException;

    /**
     * Puts the primitive double for the key.
     *
     * @param key not null.
     * @param val
     * @throws SnappydbException if the key is null.
     */
    void putDouble (String key, double val) throws SnappydbException;

    /**
     * Puts the primitive float for the key.
     *
     * @param key not null.
     * @param val
     * @throws SnappydbException if the key is null.
     */
    void putFloat (String key, float val) throws SnappydbException;

    /**
     * Puts the primitive long for the key.
     *
     * @param key not null.
     * @param val
     * @throws SnappydbException if the key is null.
     */
    void putLong (String key, long val) throws SnappydbException;

    //******************************************************************************************************************
    //*      DELETE
    //******************************************************************************************************************
    /**
     * Deletes value for the key.
     *
     * @param key not null.
     * @throws SnappydbException if the key is null.
     */
    void del (String key)  throws SnappydbException;

    //******************************************************************************************************************
    //*      RETRIEVE
    //******************************************************************************************************************
    String get(String key)  throws SnappydbException;

    byte[] getBytes(String key)  throws SnappydbException;

    <T extends Serializable> T get(String key, Class<T> className) throws SnappydbException;

    <T> T getObject(String key, Class<T> className) throws SnappydbException;

    <T extends Serializable> T[] getArray (String key, Class<T> className) throws SnappydbException;

    <T> T[] getObjectArray (String key, Class<T> className) throws SnappydbException;

    short getShort(String key)  throws SnappydbException;

    int getInt(String key)  throws SnappydbException;

    boolean getBoolean(String key)  throws SnappydbException;

    double getDouble(String key)  throws SnappydbException;

    long getLong(String key)  throws SnappydbException;

    float getFloat(String key)  throws SnappydbException;

    //******************************************************************************************************************
    //*      KEYS OPERATIONS
    //******************************************************************************************************************
    boolean exists (String key) throws SnappydbException;

    String[] findKeys(String prefix) throws SnappydbException;
    String[] findKeys(String prefix, int offset) throws SnappydbException;
    String[] findKeys(String prefix, int offset, int limit) throws SnappydbException;

    int countKeys(String prefix) throws SnappydbException;

    String[] findKeysBetween(String startPrefix, String endPrefix) throws SnappydbException;
    String[] findKeysBetween(String startPrefix, String endPrefix, int offset) throws SnappydbException;
    String[] findKeysBetween(String startPrefix, String endPrefix, int offset, int limit) throws SnappydbException;

    int countKeysBetween(String startPrefix, String endPrefix) throws SnappydbException;

    //******************************************************************************************************************
    //*      ITERATORS
    //******************************************************************************************************************
    KeyIterator allKeysIterator() throws SnappydbException;
    KeyIterator allKeysReverseIterator() throws SnappydbException;

    KeyIterator findKeysIterator(String prefix) throws SnappydbException;
    KeyIterator findKeysReverseIterator(String prefix) throws SnappydbException;

    KeyIterator findKeysBetweenIterator(String startPrefix, String endPrefix) throws SnappydbException;
    KeyIterator findKeysBetweenReverseIterator(String startPrefix, String endPrefix) throws SnappydbException;

    //******************************************************************************************************************
    //*      KRYO SERIALIZATION
    //******************************************************************************************************************

    /**
     * @return an instance of {@link Kryo}. This is to allow users to customize the {@link Kryo} instance.
     */
    Kryo getKryoInstance ();
}
