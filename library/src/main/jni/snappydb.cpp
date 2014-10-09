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


#include <jni.h>
#include <string.h>
#include <sstream>
#include <iomanip>
#include <vector>
#include <stdlib.h>
#include "com_snappydb_internal_DBImpl.h"
#include "leveldb/db.h"
#include "leveldb/options.h"
#include "debug.h";

leveldb::DB* db;
bool isDBopen;
char* databasePath;


//***************************
//*   Internal utilities
//***************************

void throwException(JNIEnv *env, const char* msg) {
	LOGE("throwException %s", msg);
	jclass snappydbExceptionClazz = env->FindClass("com/snappydb/SnappydbException");
	if ( NULL == snappydbExceptionClazz) {
		// FindClass already threw an exception such as NoClassDefFoundError.
		env->Throw(env->ExceptionOccurred());
		return;
	}
	 env->ThrowNew(snappydbExceptionClazz, msg);
}

//***********************
//*      DB MANAGEMENT
//***********************

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	LOGI("JVM is loading");

	delete db;
	isDBopen = false;
	free(databasePath);
	databasePath = NULL;
	return JNI_VERSION_1_6;
}

// the class loader containing the native library is garbage collected,  perform cleanup operations
void JNI_OnUnload(JavaVM *vm, void *reserved) {
	LOGI("JVM is unloading");

	delete db;

	isDBopen = false;
	free(databasePath);
	databasePath = NULL;
}

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1open(JNIEnv * env,
		jobject thiz, jstring dbpath) {

	LOGI("Opening database");

	const char* path = env->GetStringUTFChars(dbpath, 0);

	if (isDBopen) {
		if (NULL != databasePath && 0 != strcmp(databasePath, path)) {
			// Trying to open different db
			throwException(env, "Your database is still open, please close it before");
		} else {
			LOGI("database was already open %s", path);
		}

		env->ReleaseStringUTFChars(dbpath, path);
		return;
	}

	leveldb::Options options;
	options.create_if_missing = true;
	options.compression = leveldb::kSnappyCompression;
	leveldb::Status status = leveldb::DB::Open(options, path, &db);

	if (status.ok()) {
		isDBopen = true;
		if ((databasePath = strdup(path)) != NULL) {
			env->ReleaseStringUTFChars(dbpath, path);
		} else {
			throwException(env, "OutOfMemory when saving the database name");
		}

	} else {
		LOGE("Failed to open database");
		isDBopen = false;
		free(databasePath);
		databasePath = NULL;
		std::string err("Failed to open/create database: " + status.ToString());
		throwException (env, err.c_str());
	}
}

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1close(JNIEnv *env,
		jobject thiz) {

	LOGI("Closing database %s", databasePath);

	if (isDBopen) {
		delete db;
		isDBopen = false;
		free(databasePath);
		databasePath = NULL;

	} else {
		throwException (env, "Database was already closed");
	}
}


JNIEXPORT jboolean JNICALL Java_com_snappydb_internal_DBImpl__1_1isOpen
  (JNIEnv * env, jobject thiz) {
    LOGI("Is database open");

    if (isDBopen) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
  }

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1destroy(
		JNIEnv * env, jobject thiz, jstring dbpath) {

	LOGI("Destroying database %s", databasePath);

	const char* path = env->GetStringUTFChars(dbpath, 0);

	if (isDBopen) {
		delete db;
		isDBopen = false;
		free(databasePath);
		databasePath = NULL;
	}

	leveldb::Options options;
	leveldb::Status status = DestroyDB(path, options);

	env->ReleaseStringUTFChars(dbpath, path);

	if (status.ok()) {
		free(databasePath);
		databasePath = NULL;
		isDBopen = false;

	} else {
		isDBopen = false;
		std::string err("Failed to destroy database: " + status.ToString());
		throwException (env, err.c_str());
	}
}

//***********************
//*      CREATE
//***********************

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1del(JNIEnv *env,
		jobject thiz, jstring jKey) {

	LOGI("Deleting entry");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);

	leveldb::Status status = db->Delete(leveldb::WriteOptions(), key);
	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Successfully delete");

	} else {
		std::string err("Failed to delete: " + status.ToString());
		throwException (env, err.c_str());
	}
}

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1put__Ljava_lang_String_2Ljava_lang_String_2(
		JNIEnv *env, jobject thiz, jstring jKey, jstring jValue) {

	LOGI("Putting a String ");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);
	const char* value = env->GetStringUTFChars(jValue, 0);

	leveldb::Status status = db->Put(leveldb::WriteOptions(), key, value);
	env->ReleaseStringUTFChars(jValue, value);
	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Successfully storing a String");

	} else {
		std::string err("Failed to put a String: " + status.ToString());
		throwException (env, err.c_str());

	}
}

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1put__Ljava_lang_String_2_3B(
		JNIEnv *env, jobject thiz, jstring jKey, jbyteArray arr) { //TODO add control to check if database is open, error otherwise

	LOGI("Putting a Serializable ");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return;
	}

	int len = env->GetArrayLength(arr);
	jbyte* data =  (jbyte*)env->GetPrimitiveArrayCritical(arr, 0);
	if (data == NULL) {
	    /* out of memory exception thrown */
		throwException(env, "OutOfMemory when trying to get bytes array for Serializable");
		return;
	}

	const char* key(env->GetStringUTFChars(jKey, 0));
	leveldb::Slice value(reinterpret_cast<char*>(data), len);

	leveldb::Status status = db->Put(leveldb::WriteOptions(), key, value);

	env->ReleasePrimitiveArrayCritical(arr, data, 0);
	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Successfully writing a Serializable");

	} else {
		std::string err("Failed to put a Serializable: " + status.ToString());
		throwException(env, err.c_str());
	}
}

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1putLong(
		JNIEnv *env, jobject thiz, jstring jKey, jlong jVal) {

	LOGI("Putting a long ");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return;
	}

	const char* key(env->GetStringUTFChars(jKey, 0));
	leveldb::Slice value((char*) &jVal, sizeof(jlong));

	leveldb::Status status = db->Put(leveldb::WriteOptions(), key, value);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Successfully writing a long");

	} else {
		std::string err("Failed to put a long: " + status.ToString());
		throwException(env, err.c_str());
	}
}

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1putInt(JNIEnv *env,
		jobject thiz, jstring jKey, jint jVal) {

	LOGI("Putting an int");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return;
	}

	const char* key(env->GetStringUTFChars(jKey, 0));
	leveldb::Slice value((char*) &jVal, sizeof(jint));

	leveldb::Status status = db->Put(leveldb::WriteOptions(), key, value);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Successfully writing an int");

	} else {
		std::string err("Failed to put an int: " + status.ToString());
		throwException(env, err.c_str());
	}
}

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1putShort(
		JNIEnv *env, jobject thiz, jstring jKey, jshort jValue) {

	LOGI("Putting a short");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return;
	}

	const char* key(env->GetStringUTFChars(jKey, 0));
	leveldb::Slice value((char*) &jValue, sizeof(jshort));

	leveldb::Status status = db->Put(leveldb::WriteOptions(), key, value);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Successfully writing a short");

	} else {
		std::string err("Failed to put a short: " + status.ToString());
		throwException(env, err.c_str());
	}
}

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1putBoolean(
		JNIEnv *env, jobject thiz, jstring jKey, jboolean jValue) {

	LOGI("Putting a boolean");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return;
	}

	const char* key(env->GetStringUTFChars(jKey, 0));
	leveldb::Slice value((char*) &jValue, sizeof(jboolean));

	leveldb::Status status = db->Put(leveldb::WriteOptions(), key, value);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Successfully writing a boolean");

	} else {
		std::string err("Failed to put a boolean: " + status.ToString());
		throwException(env, err.c_str());
	}
}

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1putDouble(
		JNIEnv *env, jobject thiz, jstring jKey, jdouble jVal) {

	LOGI("Putting a double");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return;
	}
	const char* key(env->GetStringUTFChars(jKey, 0));

	std::ostringstream oss;
	oss << std::setprecision(17) << jVal;
	std::string value = oss.str();
	leveldb::Status status = db->Put(leveldb::WriteOptions(), key, value);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Successfully writing a double");

	} else {
		std::string err("Failed to put a double: " + status.ToString());
		throwException(env, err.c_str());

	}
}

JNIEXPORT void JNICALL Java_com_snappydb_internal_DBImpl__1_1putFloat(
		JNIEnv *env, jobject thiz, jstring jKey, jfloat jValue) {

	LOGI("Putting a float");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return;
	}

	const char* key(env->GetStringUTFChars(jKey, 0));
	std::ostringstream oss;
	oss << std::setprecision(16) << jValue;
	std::string value = oss.str();

	leveldb::Status status = db->Put(leveldb::WriteOptions(), key, value);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Successfully writing a float");

	} else {
		std::string err("Failed to put a float: " + status.ToString());
		throwException(env, err.c_str());
	}
}

//***********************
//*      RETRIEVE
//***********************

JNIEXPORT jlong JNICALL Java_com_snappydb_internal_DBImpl__1_1getLong(JNIEnv *env,
		jobject thiz, jstring jKey) {

	LOGI("Getting a long");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return NULL;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);
	std::string data;
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &data);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		if (8 == data.length()) {
			LOGI("Successfully reading a long");
			const char* bytes = data.data();
			long long ret = 0;

			ret = bytes[7];
			ret = (ret << 8) + (unsigned char)bytes[6];
			ret = (ret << 8) + (unsigned char)bytes[5];
			ret = (ret << 8) + (unsigned char)bytes[4];
			ret = (ret << 8) + (unsigned char)bytes[3];
			ret = (ret << 8) + (unsigned char)bytes[2];
			ret = (ret << 8) + (unsigned char)bytes[1];
			ret = (ret << 8) + (unsigned char)bytes[0];
			return ret;

		} else {
			throwException(env, "Failed to get a long");
			return NULL;
		}

	} else {
		std::string err("Failed to get a long: " + status.ToString());
		throwException(env, err.c_str());
		return NULL;
	}
}

JNIEXPORT jint JNICALL Java_com_snappydb_internal_DBImpl__1_1getInt(JNIEnv *env,
		jobject thiz, jstring jKey) {

	LOGI("Getting an int");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return NULL;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);
	std::string data;
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &data);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		if (4 == data.length()) {
			LOGI("Successfully reading an int");

			const char* bytes = data.data();
			int ret = 0;
			ret = (unsigned char)bytes[3];
			ret = (ret << 8) + (unsigned char)bytes[2];
			ret = (ret << 8) + (unsigned char)bytes[1];
			ret = (ret << 8) + (unsigned char)bytes[0];

			return ret;

		} else {
			throwException(env, "Failed to get an int");
			return NULL;
		}

	} else {
		std::string err("Failed to get an int: " + status.ToString());
		throwException(env, err.c_str());
		return NULL;
	}
}

JNIEXPORT jdouble JNICALL Java_com_snappydb_internal_DBImpl__1_1getDouble(
		JNIEnv *env, jobject thiz, jstring jKey) {

	LOGI("Getting a double");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return NULL;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);
	std::string data;
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &data);

	env->ReleaseStringUTFChars(jKey, key);



	if (status.ok()) {// we can't use data.length() here to make sure of the size of float since it was encoded as string
		double d = atof(data.c_str());
		LOGI("Successfully reading a double");
		return d;

	} else {
		std::string err("Failed to get a double: " + status.ToString());
		throwException(env, err.c_str());
		return NULL;
	}
}

JNIEXPORT jshort JNICALL Java_com_snappydb_internal_DBImpl__1_1getShort(JNIEnv *env,
		jobject thiz, jstring jKey) {

	LOGI("Getting a short");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return NULL;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);
	std::string data;
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &data);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		if (2 == data.length()) {
			LOGI("Successfully reading a short");

			const char* bytes = data.data();
			short ret = 0;
			ret = bytes[1];
			ret = (ret << 8) + bytes[0];

			return ret;

		} else {
			throwException(env, "Failed to get a short");
			return NULL;
		}

	} else {
		std::string err("Failed to get a short: " + status.ToString());
		throwException(env, err.c_str());
		return NULL;
	}
}

JNIEXPORT jboolean JNICALL Java_com_snappydb_internal_DBImpl__1_1getBoolean(
		JNIEnv *env, jobject thiz, jstring jKey) {

	LOGI("Getting a boolean");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return NULL;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);
	std::string data;
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &data);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		if (1 == data.length()) {
			LOGI("Successfully reading a boolean");
			return data.data()[0];

		} else {
			throwException(env, "Failed to get a boolean");
			return NULL;
		}


	} else {
		std::string err("Failed to get a boolean: " + status.ToString());
		throwException(env, err.c_str());
		return NULL;
	}
}

JNIEXPORT jstring JNICALL Java_com_snappydb_internal_DBImpl__1_1get(JNIEnv *env,
		jobject thiz, jstring jKey) {

	LOGI("Getting a String");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return NULL;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);
	std::string value;
	leveldb::ReadOptions();
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &value);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Successfully reading a String");
		const char* re = value.c_str();
		return env->NewStringUTF(re);

	} else {
		std::string err("Failed to get a String: " + status.ToString());
		throwException(env, err.c_str());
		return NULL;
	}
}

JNIEXPORT jbyteArray JNICALL Java_com_snappydb_internal_DBImpl__1_1getBytes(
		JNIEnv *env, jobject thiz, jstring jKey) {

	LOGI("Getting a byte array");

	if (!isDBopen) {
		throwException(env, "database is not open");
		return NULL;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);
	std::string data;
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &data);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		int size = data.size();

		char* elems = const_cast<char*>(data.data());
		jbyteArray array = env->NewByteArray(size * sizeof(jbyte));
		env->SetByteArrayRegion(array, 0, size, reinterpret_cast<jbyte*>(elems));

		LOGI("Successfully reading a byte array");
		return array;

	} else {
		std::string err("Failed to get a byte array: " + status.ToString());
		throwException(env, err.c_str());
		return NULL;
	}
}

JNIEXPORT jfloat JNICALL Java_com_snappydb_internal_DBImpl__1_1getFloat(JNIEnv *env,
		jobject thiz, jstring jKey) {

	LOGI("Getting a float");

	if (!isDBopen) {
		throwException(env, "database is not open");
		return NULL;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);
	std::string data;
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &data);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {// we can't use data.length() here to make sure of the size of float since it was encoded as string
			LOGI("Successfully reading a float");
			float f = atof(data.c_str());
			return f;

	} else {
		std::string err("Failed to get a float: " + status.ToString());
		throwException(env, err.c_str());
		return NULL;
	}
}


//****************************
//*      KEYS OPERATIONS
//****************************

JNIEXPORT jboolean JNICALL Java_com_snappydb_internal_DBImpl__1_1exists
  (JNIEnv *env, jobject thiz, jstring jKey) {

	LOGI("does key exists");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return NULL;
	}

	const char* key = env->GetStringUTFChars(jKey, 0);
	std::string value;
	leveldb::Status status = db->Get(leveldb::ReadOptions(), key, &value);

	env->ReleaseStringUTFChars(jKey, key);

	if (status.ok()) {
		LOGI("Key Found ");
		return JNI_TRUE;

	} else if (status.IsNotFound()) {
		LOGI("Key Not Found ");
		return JNI_FALSE;

	} else {
		std::string err("Failed to check if a key exists: " + status.ToString());
		throwException(env, err.c_str());
		return NULL;
	}
}


JNIEXPORT jobjectArray JNICALL Java_com_snappydb_internal_DBImpl__1_1findKeys
  (JNIEnv *env, jobject thiz, jstring jPrefix, jint limit) {

	LOGI("find keys");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return NULL;
	}

	const char* prefix = env->GetStringUTFChars(jPrefix, 0);

	std::vector<std::string> result;
	leveldb::Iterator* it = db->NewIterator(leveldb::ReadOptions());

	int count = 0;
	for (it->Seek(prefix); count++ < limit && it->Valid() && it->key().starts_with(prefix);
			it->Next()) {
		result.push_back(it->key().ToString());
	}

	std::vector<std::string>::size_type n = result.size();
	jobjectArray ret= (jobjectArray)env->NewObjectArray(n,
		         env->FindClass("java/lang/String"),
		         env->NewStringUTF(""));

	jstring str;
	for (int i=0; i<n ; i++) {
		str = env->NewStringUTF(result[i].c_str());
		env->SetObjectArrayElement(ret, i, str);
		env->DeleteLocalRef(str);
	}

	env->ReleaseStringUTFChars(jPrefix, prefix);
	delete it;

	return ret;
}

JNIEXPORT jobjectArray JNICALL Java_com_snappydb_internal_DBImpl__1_1findKeysBetween
  (JNIEnv *env, jobject thiz, jstring jStartPrefix, jstring jEndPrefix, jint limit)  {

	LOGI("find keys between range");

	if (!isDBopen) {
		throwException (env, "database is not open");
		return NULL;
	}

	const char* startPrefix = env->GetStringUTFChars(jStartPrefix, 0);
	const char* endPrefix = env->GetStringUTFChars(jEndPrefix, 0);

	std::vector<std::string> result;
	leveldb::Iterator* it = db->NewIterator(leveldb::ReadOptions());

	int count = 0;
	for (it->Seek(startPrefix); count++ < limit && it->Valid() && it->key().compare(endPrefix) <= 0;
			it->Next()) {
		result.push_back(it->key().ToString());
	}

	std::vector<std::string>::size_type n = result.size();
	jobjectArray ret= (jobjectArray)env->NewObjectArray(n,
		         env->FindClass("java/lang/String"),
		         env->NewStringUTF(""));

	jstring str;
	for (int i=0; i<n ; i++) {
		str = env->NewStringUTF(result[i].c_str());
		env->SetObjectArrayElement(ret, i, str);
		env->DeleteLocalRef(str);
	}

	env->ReleaseStringUTFChars(jStartPrefix, startPrefix);
	env->ReleaseStringUTFChars(jEndPrefix, endPrefix);
	delete it;

	return ret;
}


