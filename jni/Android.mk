LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libcrypto-prebuilt
LOCAL_SRC_FILES := $(LOCAL_PATH)/openssl/lib/$(TARGET_ARCH_ABI)/libcrypto.so
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/openssl/include
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_CFLAGS    := -DHAVE_CONFIG_H
LOCAL_MODULE    := scrypt

# Can only use SSE on x86.
LOCAL_SRC_FILES := scrypt/c/crypto_scrypt-nosse.c \
                   scrypt/c/sha256.c \
									 scrypt_jni.c
LOCAL_C_INCLUDES := $(LOCAL_PATH) \
	                  $(LOCAL_PATH)/scrypt/include \

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := mozopenssl
LOCAL_SRC_FILES := mozopenssl.c
LOCAL_C_INCLUDES := $(LOCAL_PATH) \
	                  $(LOCAL_PATH)/openssl/include
LOCAL_STATIC_LIBRARIES := libcrypto-prebuilt

include $(BUILD_SHARED_LIBRARY)