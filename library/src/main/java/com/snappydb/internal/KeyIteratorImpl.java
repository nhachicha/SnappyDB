package com.snappydb.internal;

import android.util.Log;

import com.snappydb.KeyIterator;
import com.snappydb.SnappydbException;

import java.util.Iterator;
import java.util.NoSuchElementException;

class KeyIteratorImpl implements KeyIterator {

    private final DBImpl db;
    private final String endPrefix;
    private final boolean reverse;

    private long ptr;
    private boolean isNextValid;

    protected KeyIteratorImpl(DBImpl db, long ptr, String endPrefix, boolean reverse) {
        this.db = db;
        this.ptr = ptr;
        this.endPrefix = endPrefix;
        this.reverse = reverse;

        isNextValid = db.__iteratorIsValid(ptr, endPrefix, reverse);
    }

    @Override
    public void close() {
        if (ptr != 0) {
            db.__iteratorClose(ptr);
        }
        ptr = 0;
        isNextValid = false;
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
        return isNextValid;
    }

    @Override
    public String[] next(int max) {
        if (!isNextValid) {
            throw new NoSuchElementException();
        }
        try {
            String[] keys = db.__iteratorNextArray(ptr, endPrefix, reverse, max);
            isNextValid = db.__iteratorIsValid(ptr, endPrefix, reverse);
            if (!isNextValid) {
                close();
            }
            return keys;
        } catch (SnappydbException e) {
            throw new RuntimeException(e);
        }
    }

    private class BatchIterableImpl implements Iterable<String[]>, Iterator<String[]> {

        private int size;

        private BatchIterableImpl(int size) {
            this.size = size;
        }

        @Override
        public Iterator<String[]> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return KeyIteratorImpl.this.hasNext();
        }

        @Override
        public String[] next() {
            return KeyIteratorImpl.this.next(size);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterable<String[]> byBatch(int size) {
        return new BatchIterableImpl(size);
    }
}
