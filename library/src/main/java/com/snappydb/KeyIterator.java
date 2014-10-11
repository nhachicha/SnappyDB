package com.snappydb;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

public interface KeyIterator extends Iterable<String>, Iterator<String>, Closeable {

    public String[] next(int max);

}
