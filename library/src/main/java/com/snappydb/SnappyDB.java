package com.snappydb;

import android.content.Context;
import android.text.TextUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;

import java.io.File;

/**
 * Created by Nabil on 12/06/14.
 */
public class SnappyDB {
    private static volatile DB singleton = null;

    public static DB with(Context context) throws SnappydbException {
        if (singleton == null || !singleton.isOpen()) {//add check if DB is closed recreate (open) a new db (isOpen)
            synchronized (SnappyDB.class) {
                // double-checked locking.
                // while we were waiting for the lock, another thread may have instantiated the instance
                if (singleton == null || !singleton.isOpen()) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }

    /**
     * Fluent API for creating {@link com.snappydb.DB} instances.
     */
    public static class Builder {
        private final Context context;

        /**
         * The database name's
         */
        private String name;

        /**
         * The directory (absolute path) where the database files are stored.
         */
        private String dir;

        private Kryo kryo;

        /**
         * Start building a new {@link com.snappydb.DB} instance.
         */
        public Builder(Context context) {
            if (null == context) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
            this.kryo = new Kryo();
            this.kryo.setAsmEnabled(true);
        }

        public Builder name(String dbName) {
            if (TextUtils.isEmpty(dbName)) {
                throw new IllegalArgumentException("Database name must not be empty or null.");
            }
            this.name = dbName;
            return this;
        }

        public Builder directory(String absolutePath) {
            if (TextUtils.isEmpty(absolutePath)) {
                throw new IllegalArgumentException("Database directory must not be empty or null.");
            }
            this.dir = absolutePath;
            return this;
        }

        public Builder registerSerializers(Class type, Serializer serializer) {
            if (null == type) {
                throw new IllegalArgumentException("Class type must not be null.");
            }
            if (null == serializer) {
                throw new IllegalArgumentException("Serializer must not be null.");
            }
            this.kryo.register(type, serializer);
            return this;
        }

        /**
         * Create the {@link com.snappydb.DB} instance.
         */
        public DB build() throws SnappydbException {
            if (null != dir) {
                File f = new File(dir);
                if((f.mkdirs() || f.isDirectory()) && f.canWrite()) {
                    if (null != name) {
                        return DBFactory.open(dir, name, kryo);
                    } else {
                        return DBFactory.open(dir, kryo);
                    }
                } else {
                    throw new IllegalStateException("Can't create or access directory " + dir);
                }

            } else {
                if (null != name) {// use default location
                    return DBFactory.open(context, name, kryo);

                } else {//use default name & location
                    return DBFactory.open(context, kryo);
                }
            }
        }

    }
}
