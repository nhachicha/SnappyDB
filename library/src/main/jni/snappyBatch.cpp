#include <jni.h>
#include <string.h>
#include <sstream>
#include <iomanip>
#include <vector>
#include <stdlib.h>
#include "leveldb/db.h"
#include "leveldb/options.h"
#include "debug.h";
#include "leveldb/write_batch.h"
#include "leveldb/slice.h"

#include "com_snappydb_WriteBatch.h"

leveldb::WriteBatch *batch;

// Helper functions
leveldb::WriteBatch* batchFromPtr(jlong ptr);
template <typename T>
void putPrimitive(JNIEnv *env, jlong ptr, jstring jKey, T jValue);
void throwBatchException(JNIEnv *env, const char* msg);

JNIEXPORT jlong JNICALL Java_com_snappydb_WriteBatch__1_1create(JNIEnv *env, jobject thiz) {
    leveldb::WriteBatch *batch = new leveldb::WriteBatch();
    return reinterpret_cast<jlong>(batch);
}

JNIEXPORT void JNICALL Java_com_snappydb_WriteBatch__1_1put__JLjava_lang_String_2_3B(JNIEnv *env, jobject thiz, jlong ptr, jstring jKey, jbyteArray jValue) {
    if (ptr == NULL) {
        throwBatchException(env, "Batch is not open");
        return;
    }

    const char *key = env->GetStringUTFChars(jKey, 0);
    int len = env->GetArrayLength(jValue);
    jbyte* data =  (jbyte*)env->GetPrimitiveArrayCritical(jValue, 0);

    leveldb::WriteBatch *batch = batchFromPtr(ptr);

    leveldb::Slice keySlice = leveldb::Slice(key);
    leveldb::Slice valSlice = leveldb::Slice(reinterpret_cast<char*>(data), len);

    batch->Put(keySlice, valSlice);

    env->ReleasePrimitiveArrayCritical(jValue, data, 0);
    env->ReleaseStringUTFChars(jKey, key);
}

JNIEXPORT void JNICALL Java_com_snappydb_WriteBatch__1_1put__JLjava_lang_String_2Ljava_lang_String_2(JNIEnv *env, jobject thiz, jlong ptr, jstring jKey, jstring jValue) {
    if (ptr == NULL) {
        throwBatchException(env, "Batch is not open");
        return;
    }

    const char *key = env->GetStringUTFChars(jKey, 0);
    const char *val = env->GetStringUTFChars(jValue, 0);

    leveldb::Slice keySlice = leveldb::Slice(key);
    leveldb::Slice valSlice = leveldb::Slice(val);

    leveldb::WriteBatch *batch = batchFromPtr(ptr);

    batch->Put(keySlice, valSlice);

    env->ReleaseStringUTFChars(jKey, key);
    env->ReleaseStringUTFChars(jValue, val);
}

JNIEXPORT void JNICALL Java_com_snappydb_WriteBatch__1_1putShort(JNIEnv *env, jobject thiz, jlong ptr, jstring jKey, jshort jValue) {
    putPrimitive(env, ptr, jKey, jValue);
}

JNIEXPORT void JNICALL Java_com_snappydb_WriteBatch__1_1putInt(JNIEnv *env, jobject thiz, jlong ptr, jstring jKey, jint jValue) {
    putPrimitive(env, ptr, jKey, jValue);
}

JNIEXPORT void JNICALL Java_com_snappydb_WriteBatch__1_1putBoolean(JNIEnv *env, jobject thiz, jlong ptr, jstring jKey, jboolean jValue) {
    putPrimitive(env, ptr, jKey, jValue);
}

JNIEXPORT void JNICALL Java_com_snappydb_WriteBatch__1_1putDouble(JNIEnv *env, jobject thiz, jlong ptr, jstring jKey, jdouble jValue) {
    if (ptr == NULL) {
        throwBatchException(env, "Batch is not open");
        return;
    }

    const char *key = env->GetStringUTFChars(jKey, 0);

    std::ostringstream oss;
    oss << std::setprecision(17) << jValue;
    std::string val = oss.str();
    leveldb::Slice keySlice = leveldb::Slice(key);
    leveldb::Slice valSlice = leveldb::Slice(val);

    leveldb::WriteBatch *batch = batchFromPtr(ptr);

    batch->Put(keySlice, valSlice);

    env->ReleaseStringUTFChars(jKey, key);
}

JNIEXPORT void JNICALL Java_com_snappydb_WriteBatch__1_1putFloat(JNIEnv *env, jobject thiz, jlong ptr, jstring jKey, jfloat jValue) {
    if (ptr == NULL) {
        throwBatchException(env, "Batch is not open");
        return;
    }

    const char *key = env->GetStringUTFChars(jKey, 0);

    std::ostringstream oss;
    oss << std::setprecision(16) << jValue;
    std::string val = oss.str();
    leveldb::Slice keySlice = leveldb::Slice(key);
    leveldb::Slice valSlice = leveldb::Slice(val);

    leveldb::WriteBatch *batch = batchFromPtr(ptr);

    batch->Put(keySlice, valSlice);

    env->ReleaseStringUTFChars(jKey, key);
}

JNIEXPORT void JNICALL Java_com_snappydb_WriteBatch__1_1putLong(JNIEnv *env, jobject thiz, jlong ptr, jstring jKey, jlong jValue) {
    putPrimitive(env, ptr, jKey, jValue);
}

JNIEXPORT void JNICALL Java_com_snappydb_WriteBatch__1_1delete(JNIEnv *env, jobject thiz, jlong ptr, jstring jKey) {
    if (ptr == NULL) {
        throwBatchException(env, "Batch is not open");
        return;
    }

    const char *key = env->GetStringUTFChars(jKey, 0);
    leveldb::Slice keySlice = leveldb::Slice(key);
    leveldb::WriteBatch *batch = batchFromPtr(ptr);

    batch->Delete(keySlice);

    env->ReleaseStringUTFChars(jKey, key);
}

JNIEXPORT jlong JNICALL Java_com_snappydb_WriteBatch__1_1close(JNIEnv *env, jobject thiz, jlong ptr) {
    leveldb::WriteBatch *batch = batchFromPtr(ptr);
    delete batch;
    return NULL;
}

JNIEXPORT void JNICALL Java_com_snappydb_WriteBatch__1_1clear(JNIEnv *env, jobject thiz, jlong ptr) {
    if (ptr == NULL) {
        throwBatchException(env, "Batch is not open");
        return;
    }

    leveldb::WriteBatch *batch = batchFromPtr(ptr);
    batch->Clear();
}

// Helper Functions
leveldb::WriteBatch* batchFromPtr(jlong ptr) {
    return reinterpret_cast<leveldb::WriteBatch*>(ptr);
}

template <typename T>
void putPrimitive(JNIEnv *env, jlong ptr, jstring jKey, T jValue) {
    if (ptr == NULL) {
        throwBatchException(env, "Batch is not open");
        return;
    }

    const char *key = env->GetStringUTFChars(jKey, 0);
    leveldb::Slice keySlice = leveldb::Slice(key);
    leveldb::Slice valSlice = leveldb::Slice((char *) &jValue, sizeof(T));

    leveldb::WriteBatch *batch = batchFromPtr(ptr);

    batch->Put(keySlice, valSlice);

    env->ReleaseStringUTFChars(jKey, key);
}

void throwBatchException(JNIEnv *env, const char* msg) {
	LOGE("throwException %s", msg);
	jclass snappydbExceptionClazz = env->FindClass("com/snappydb/SnappydbException");
	if ( NULL == snappydbExceptionClazz) {
		// FindClass already threw an exception such as NoClassDefFoundError.
		env->Throw(env->ExceptionOccurred());
		return;
	}
	 env->ThrowNew(snappydbExceptionClazz, msg);
}