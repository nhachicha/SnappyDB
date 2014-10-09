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
	//***********************
	//*      DB MANAGEMENT
	//***********************

    public void close ()  throws SnappydbException;

	public void destroy ()  throws SnappydbException;

    public boolean isOpen ()  throws SnappydbException;

	//***********************
	//*      CREATE
	//***********************
	public void put (String key, byte[] data) throws SnappydbException;

	public void put (String key, String value) throws SnappydbException;
	
	public void put (String key, Serializable value) throws SnappydbException;
	
	public void put (String key, Serializable [] value) throws SnappydbException;

    public void put (String key, Object object) throws SnappydbException;

    public void put (String key, Object [] object) throws SnappydbException;
	
	public void putInt (String key, int val) throws SnappydbException;
	
	public void putShort (String key, short val) throws SnappydbException;
	
	public void putBoolean (String key, boolean val) throws SnappydbException;
	
	public void putDouble (String key, double val) throws SnappydbException;
	
	public void putFloat (String key, float val) throws SnappydbException;
		
	public void putLong (String key, long val) throws SnappydbException;

	//***********************
	//*      DELETE 
	//***********************
	public void del (String key)  throws SnappydbException;
	
	//***********************
	//*      RETRIEVE 
	//***********************	
	public String get(String key)  throws SnappydbException;
	
	public byte[] getBytes(String key)  throws SnappydbException;
	
	public <T extends Serializable> T get(String key, Class<T> className) throws SnappydbException;

    public <T> T getObject(String key, Class<T> className) throws SnappydbException;

	public <T extends Serializable> T[] getArray (String key, Class<T> className) throws SnappydbException;

    public <T> T[] getObjectArray (String key, Class<T> className) throws SnappydbException;
	
	public short getShort(String key)  throws SnappydbException;
	
	public int getInt(String key)  throws SnappydbException;
	
	public boolean getBoolean(String key)  throws SnappydbException;
	
	public double getDouble(String key)  throws SnappydbException;
	
	public long getLong(String key)  throws SnappydbException;
	
	public float getFloat(String key)  throws SnappydbException;
	
	//****************************
	//*      KEYS OPERATIONS 
	//****************************	
	public boolean exists (String key) throws SnappydbException;

    public String[] findKeys(String prefix) throws SnappydbException;
    public String[] findKeys(String prefix, int offset) throws SnappydbException;
    public String[] findKeys(String prefix, int offset, int limit) throws SnappydbException;

    public int countKeys(String prefix) throws SnappydbException;

    public String[] findKeysBetween(String startPrefix, String endPrefix) throws SnappydbException;
    public String[] findKeysBetween(String startPrefix, String endPrefix, int offset) throws SnappydbException;
    public String[] findKeysBetween(String startPrefix, String endPrefix, int offset, int limit) throws SnappydbException;

    public int countKeysBetween(String startPrefix, String endPrefix) throws SnappydbException;

    //*********************************
    //*      KRYO SERIALIZATION
    //*********************************

    // Allow the user to access the Kryo instance, for eventual customization
    public Kryo getKryoInstance ();
}

