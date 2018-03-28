/*
 * Copyright (C) 2016 The Android Open Source Project
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
 *
 */

#include <jni.h>
#include "keyCode.h"


/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   hello-jni/app/src/main/java/com/example/SecretService/mainSS.java
 */

JNIEXPORT jstring JNICALL
Java_com_example_SecretService_mainSS_stringFromJNI(JNIEnv *env,
                                                 jobject thiz) {
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a/NEON (hard-float)"
#else
#define ABI "armeabi-v7a/NEON"
#endif
#else
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a (hard-float)"
#else
#define ABI "armeabi-v7a"
#endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
#define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif

    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");
}

JNIEXPORT void JNICALL
Java_com_example_SecretService_mainSS_encryptKey(
                                                JNIEnv *env,
                                                jobject thiz,
                                                jstring secretString,
                                                jint tParam,
                                                jint kParam,
                                                jobjectArray keyList) {
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a/NEON (hard-float)"
#else
#define ABI "armeabi-v7a/NEON"
#endif
#else
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a (hard-float)"
#else
#define ABI "armeabi-v7a"
#endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
#define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif

    const char *nativeSecret = (*env)->GetStringUTFChars(env, secretString, 0);
    int nativeT = tParam;
    int nativeK = kParam;
    char **nativeKeys = NULL;
    nativeKeys = malloc((nativeK+1)*sizeof(char *));

    /*
    *outKeys=(char**)malloc(n*sizeof(char*));
    for(int i=0;i<n;i++)
        *outKeys[i]=(char*)malloc(m*sizeof(char));

    char * numChar = (char *)alloca(nParam + 1);
    memcpy(numChar, numStr.c_str(), nParam + 1);
    keySet[i] = numChar ; //Calculate and appends next key to list
    */

    generateKeyset(nativeSecret, nativeT, nativeK, nativeKeys);

    for(int i=0;i<=kParam;i++) {
        (*env)->SetObjectArrayElement(env,
                keyList,i,(*env)->NewStringUTF(env, nativeKeys[i]));
    }
    return;
}