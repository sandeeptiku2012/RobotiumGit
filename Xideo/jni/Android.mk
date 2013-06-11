LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := imageeffects
LOCAL_SRC_FILES := imageeffects.c
LOCAL_LDLIBS    := -llog -ljnigraphics
include $(BUILD_SHARED_LIBRARY)
