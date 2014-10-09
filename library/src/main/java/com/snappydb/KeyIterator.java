package com.snappydb;

import java.io.Closeable;
import java.util.Iterator;

public interface KeyIterator extends Iterator<String>, Closeable {

    public String[] next(int max);

}
