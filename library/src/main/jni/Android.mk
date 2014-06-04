
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
include $(LOCAL_PATH)/common.mk

LOCAL_MODULE := snappydb-native
LOCAL_C_INCLUDES := $(C_INCLUDES)
LOCAL_CFLAGS := -DLEVELDB_PLATFORM_ANDROID -std=gnu++0x -g -w
LOCAL_SRC_FILES := $(SOURCES) ./port/port_android.cc snappydb.cpp
LOCAL_LDLIBS +=  -llog -ldl
include $(BUILD_SHARED_LIBRARY)
