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

import java.io.File;

import android.content.Context;

import com.snappydb.internal.DBImpl;

public class DBFactory {
	private final static String DEFAULT_DBNAME = "snappydb";

	//Return the Database with the given name, if it doesn't exist create it
	public static DB open (Context ctx, String dbName) throws SnappydbException {
		String dbFilePath = ctx.getFilesDir().getAbsolutePath() + File.separator + dbName;
		return new DBImpl(dbFilePath);
	}
	
	public static DB open (Context ctx) throws SnappydbException {
			return open(ctx, DEFAULT_DBNAME);
	}
	
	static {
		System.loadLibrary("snappydb-native");
	}
	
}

