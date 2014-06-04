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

package com.snappydb.internal;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;

import android.text.TextUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

public class DBImpl implements DB {
	private String dbPath;

	public DBImpl(String path) throws SnappydbException {
		dbPath = path;
		__open(dbPath);
	}

	// ***********************
	// *     DB MANAGEMENT
	// ***********************

	@Override
	public void close() {
		__close();
	}
	
	public void destroy() throws SnappydbException {
		__destroy(dbPath);
	}

	
	// ***********************
	// *       CREATE
	// ***********************
	@Override
	public void put(String key, String value) throws SnappydbException {
		checkArgs(key, value);
		
		__put(key, value);
	}

	@Override
	public void put(String key, Serializable value) throws SnappydbException {
		checkArgs(key, value);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Kryo kryo = new Kryo();
		kryo.setAsmEnabled(true);
		kryo.register(value.getClass());
		
		Output output = new Output(stream);
		try {
			kryo.writeObject(output, value);
			output.close();
		
			__put(key, stream.toByteArray());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SnappydbException (e.getMessage());
		} 
	}

	@Override
	public void put(String key, byte[] value) throws SnappydbException {
		checkArgs(key, value);
		
		__put(key, value);
	}


	@Override
	public void put(String key, Serializable[] value) throws SnappydbException {
		checkArgs(key, value);		
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Kryo kryo = new Kryo();
		kryo.setAsmEnabled(true);
		kryo.register(value.getClass());
		
		Output output = new Output(stream);
		try {
			kryo.writeObject(output, value);
			output.close();
		
			__put(key, stream.toByteArray());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SnappydbException ("Kryo exception " + e.getMessage());
		} 
	}

	@Override
	public void putShort(String key, short val) throws SnappydbException {
		checkArgs(key);
		
		__putShort(key, val);
	}


	@Override
	public void putInt(String key, int val) throws SnappydbException {
		checkArgs(key);
		
		__putInt(key, val);
	}

	

	@Override
	public void putBoolean(String key, boolean val) throws SnappydbException {
		checkArgs(key);

		__putBoolean(key, val);
	}

	@Override
	public void putDouble(String key, double val) throws SnappydbException {
		checkArgs(key);
		
		__putDouble(key, val);
	}

	@Override
	public void putFloat(String key, float val) throws SnappydbException {
		checkArgs(key);
		
		__putFloat(key, val);
	}

	@Override
	public void putLong(String key, long val) throws SnappydbException {
		checkArgs(key);
		
		__putLong(key, val);
	}
	
	// ***********************
	// *      DELETE
	// ***********************

	@Override
	public void del(String key) throws SnappydbException {
		checkArgs(key);
		
		__del(key);
	}

	// ***********************
	// *       RETRIEVE
	// ***********************
	
	@Override
	public <T extends Serializable> T get(String key, Class<T> className)
			throws SnappydbException {
		checkArgs(key, className);
		
		if (className.isArray()) {
			throw new SnappydbException(
					"You should call getArray instead");
		}
		
		byte[] data = getBytes(key);
		
		Kryo kryo = new Kryo();
		kryo.setAsmEnabled(true);
		kryo.register(className);
		
		Input input = new Input(data);
		Object obj = null;
		try {
			obj = kryo.readObject(input, className);
			return className.cast(obj);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SnappydbException ("Maybe you tried to retrive an array using this method ? please use getArray instead " + e.getMessage());
		} finally {
			input.close();
		}
	}

	@Override
	public <T extends Serializable> T[] getArray(String key, Class<T> className)
			throws SnappydbException {
		checkArgs(key, className);
		
		byte[] data = __getBytes(key);
		
		Kryo kryo = new Kryo();
		kryo.setAsmEnabled(true);
		kryo.register(className);
		
		Input input = new Input(data);
		T[] obj = null;
		T[] array = (T[]) Array.newInstance(className, 0);
		
		try {
			obj = (T[]) kryo.readObject(input, array.getClass());
			return obj;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SnappydbException ("Maybe you tried to retrive an array using this method ? please use getArray instead " + e.getMessage());
		} finally {
			input.close();
		}
	}
	
	@Override
	public byte[] getBytes(String key) throws SnappydbException {
		checkArgs(key);
		
		return __getBytes(key);
	}

	@Override
	public String get(String key) throws SnappydbException {
		checkArgs(key);
		
		return __get(key);
	}

	@Override
	public short getShort(String key) throws SnappydbException {
		checkArgs(key);
		
		return __getShort(key);
	}
	
	@Override
	public int getInt(String key) throws SnappydbException {
		checkArgs(key);

		return __getInt (key);
	}
	
	@Override
	public boolean getBoolean(String key) throws SnappydbException {
		checkArgs(key);
		
		return __getBoolean (key);
	}
	
	@Override
	public double getDouble(String key) throws SnappydbException {
		checkArgs(key);
		
		return __getDouble(key);
	}
	
	@Override
	public long getLong(String key) throws SnappydbException {
		checkArgs(key);
		
		return __getLong(key);
	}
	
	@Override
	public float getFloat(String key) throws SnappydbException {
		checkArgs(key);
		
		return __getFloat(key);
	}

	//****************************
	//*      KEYS OPERATIONS 
	//****************************
	@Override
	public boolean exists (String key) throws SnappydbException {
		checkArgs(key);
		
		return __exists(key);
	}
	
	// ***********************
	// *      UTILS
	// ***********************

	private void checkArgs (String key, Object value) throws SnappydbException {
		if (TextUtils.isEmpty(key)) {
			throw new SnappydbException ("Key must not be empty");
		}
		
		if (null == value) {
			throw new SnappydbException ("Value must not be empty");
		}
	}
	
	private void checkArgs (String key) throws SnappydbException {
		if (TextUtils.isEmpty(key)) {
			throw new SnappydbException ("Key must not be empty");
		}
	}
	
	// native code
	private native void __close();
	private native void __open(String dbName) throws SnappydbException;
	private native void __destroy(String dbName) throws SnappydbException;
	
	private native void __put(String key, byte[] value) throws SnappydbException;
	private native void __put(String key, String value) throws SnappydbException;
	private native void __putShort(String key, short val) throws SnappydbException;
	private native void __putInt(String key, int val) throws SnappydbException;
	private native void __putBoolean(String key, boolean val) throws SnappydbException;
	private native void __putDouble(String key, double val) throws SnappydbException;
	private native void __putFloat(String key, float val) throws SnappydbException;
	private native void __putLong(String key, long val) throws SnappydbException;
	
	private native void __del(String key) throws SnappydbException;
	
	private native byte[] __getBytes(String key) throws SnappydbException;
	private native String __get(String key) throws SnappydbException;
	private native short __getShort(String key) throws SnappydbException;
	private native int __getInt(String key) throws SnappydbException;
	private native boolean __getBoolean(String key) throws SnappydbException;
	private native double __getDouble(String key) throws SnappydbException; 
	private native long __getLong(String key) throws SnappydbException;
	private native float __getFloat(String key) throws SnappydbException;
	
	private native boolean __exists (String key) throws SnappydbException;
	
}
