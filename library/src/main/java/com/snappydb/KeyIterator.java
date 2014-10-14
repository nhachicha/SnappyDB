package com.snappydb;

import java.io.Closeable;

public interface KeyIterator extends Closeable {

    public boolean hasNext();

    public String[] next(int max);

    public Iterable<String[]> byBatch(int size);

    void close();

}
