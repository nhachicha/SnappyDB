package com.snappydb.internal;

import android.util.Log;

import com.snappydb.KeyIterator;
import com.snappydb.SnappydbException;

import java.io.IOException;
import java.util.NoSuchElementException;

class KeyIteratorImpl implements KeyIterator {

    private final DBImpl db;
    private final String endPrefix;
    private final boolean reverse;

    private long ptr;

    private String nextKey;

    protected KeyIteratorImpl(DBImpl db, long ptr, String endPrefix, boolean reverse) {
        this.db = db;
        this.ptr = ptr;
        this.endPrefix = endPrefix;
        this.reverse = reverse;

        nextKey = db.__iteratorKey(ptr, endPrefix, reverse);
    }

    @Override
    public void close() throws IOException {
        if (ptr != 0)
            db.__iteratorClose(ptr);
        ptr = 0;
    }

    @Override
    protected void finalize() throws Throwable {
        if (ptr != 0) {
            Log.w("KeyIterator", "SnappyDB iterators must be closed");
            close();
        }
        super.finalize();
    }

    @Override
    public boolean hasNext() {
        return nextKey != null;
    }

    @Override
    public String next() {
        if (nextKey == null) {
            throw new NoSuchElementException();
        }
        try {
            String key = nextKey;
            nextKey = db.__iteratorNextKey(ptr, endPrefix, reverse);
            return key;
        } catch (SnappydbException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String[] next(int max) {
        if (nextKey == null) {
            throw new NoSuchElementException();
        }
        try {
            String[] keys = db.__iteratorNextArray(ptr, endPrefix, reverse, max);
            nextKey = db.__iteratorKey(ptr, endPrefix, reverse);
            return keys;
        } catch (SnappydbException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
