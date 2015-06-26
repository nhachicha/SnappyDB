package com.snappydb;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class WriteBatch {

    /**
     * We need to maintain a pointer to this specific WriteBatch object
     * We cannot use a global WriteBatch in the C++ because more than one
     * could exist at a time
     */
    private long ptr;
    private Kryo kryo;
    private boolean isOpen;

    public WriteBatch() {
        ptr = __create();
        isOpen = true;
        this.kryo = new Kryo();
        this.kryo.setAsmEnabled(true);
    }

    public void close() {
        ptr = __close(ptr);
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void checkOpen() throws SnappydbException {
        if (!isOpen) {
            throw new SnappydbException("Batch is closed!");
        }
    }

    public void put(String key, byte[] data) throws SnappydbException {
        checkOpen();
        __put(ptr, key, data);
    }

    public void put(String key, String value) throws SnappydbException {
        checkOpen();
        __put(ptr, key, value);
    }

    public void put(String key, Serializable value) throws SnappydbException {
        checkOpen();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        kryo.register(value.getClass());

        Output output = new Output(stream);
        try {
            kryo.writeObject(output, value);
            output.close();

            __put(ptr, key, stream.toByteArray());

        } catch (Exception e) {
            throw new SnappydbException(e.getMessage());
        }
    }

    public void put(String key, Serializable[] value) throws SnappydbException {
        checkOpen();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        kryo.register(value.getClass());

        Output output = new Output(stream);
        try {
            kryo.writeObject(output, value);
            output.close();

            __put(ptr, key, stream.toByteArray());

        } catch (Exception e) {
            throw new SnappydbException(e.getMessage());
        }
    }

    public void put(String key, Object value) throws SnappydbException {
        checkOpen();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        kryo.register(value.getClass());

        Output output = new Output(stream);
        try {
            kryo.writeObject(output, value);
            output.close();

            __put(ptr, key, stream.toByteArray());

        } catch (Exception e) {
            throw new SnappydbException(e.getMessage());
        }
    }

    public void put(String key, Object[] object) throws SnappydbException {
        checkOpen();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        kryo.register(object.getClass());

        Output output = new Output(stream);
        try {
            kryo.writeObject(output, object);
            output.close();

            __put(ptr, key, stream.toByteArray());

        } catch (Exception e) {
            throw new SnappydbException("Kryo exception " + e.getMessage());
        }
    }

    public void putInt(String key, int val) throws SnappydbException {
        checkOpen();
        __putInt(ptr, key, val);
    }

    public void putShort(String key, short val) throws SnappydbException {
        checkOpen();
        __putShort(ptr, key, val);
    }

    public void putBoolean(String key, boolean val) throws SnappydbException {
        checkOpen();
        __putBoolean(ptr, key, val);
    }

    public void putDouble(String key, double val) throws SnappydbException {
        checkOpen();
        __putDouble(ptr, key, val);
    }

    public void putFloat(String key, float val) throws SnappydbException {
        checkOpen();
        __putFloat(ptr, key, val);
    }

    public void putLong(String key, long val) throws SnappydbException {
        checkOpen();
        __putLong(ptr, key, val);
    }

    public void delete(String key) throws SnappydbException {
        checkOpen();
        __delete(ptr, key);
    }

    public void clear() throws SnappydbException {
        checkOpen();
        __clear(ptr);
    }

    public long getPointer() {
        return ptr;
    }

    private native long __create();

    private native void __put(long ptr, String key, byte[] value) throws SnappydbException;

    private native void __put(long ptr, String key, String value) throws SnappydbException;

    private native void __putShort(long ptr, String key, short val) throws SnappydbException;

    private native void __putInt(long ptr, String key, int val) throws SnappydbException;

    private native void __putBoolean(long ptr, String key, boolean val) throws SnappydbException;

    private native void __putDouble(long ptr, String key, double val) throws SnappydbException;

    private native void __putFloat(long ptr, String key, float val) throws SnappydbException;

    private native void __putLong(long ptr, String key, long val) throws SnappydbException;

    private native void __delete(long ptr, String key) throws SnappydbException;

    private native void __clear(long ptr) throws SnappydbException;

    private native long __close(long ptr);
}
