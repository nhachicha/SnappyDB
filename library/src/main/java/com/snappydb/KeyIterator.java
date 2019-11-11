package com.snappydb;

import java.io.Closeable;

public interface KeyIterator extends Closeable {

    boolean hasNext();

    String[] next(int max);

    Iterable<String[]> byBatch(int size);

    void close();

}
