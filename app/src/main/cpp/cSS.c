#include <jni.h>
#include "keyCode.h"

JNIEXPORT void JNICALL
Java_com_example_SecretService_mainSS_CkeySplit(
                                                JNIEnv *env,
                                                jobject thiz,
                                                jstring secretString,
                                                jint tParam,
                                                jint kParam,
                                                jobjectArray keyList) {

    const char *nativeSecret = (*env)->GetStringUTFChars(env, secretString, 0);
    int nativeT = tParam;
    int nativeK = kParam;
    char **nativeKeys = NULL;
    nativeKeys = malloc((nativeK+1)*sizeof(char *));

    generateKeyset(nativeSecret, nativeT, nativeK, nativeKeys);

    for(int i=0;i<=kParam;i++) {
        (*env)->SetObjectArrayElement(env,
                keyList,i,(*env)->NewStringUTF(env, nativeKeys[i]));
    }
    return;
}

JNIEXPORT jstring JNICALL
Java_com_example_SecretService_mainSS_CgetSecret(
                                                JNIEnv *env,
                                                jobject thiz,
                                                jobjectArray keyList) {

    int numKeys = (*env)->GetArrayLength(env, keyList);
    char ** keys = malloc((numKeys+1)*sizeof(char*));
    for(int i=0;i<numKeys;i++) {
        jstring temp = (*env)->GetObjectArrayElement(env, keyList, 0);
        keys[i] = (*env)->GetStringUTFChars(env, temp, 0);
    }
    char *outSecret = decodeKeyset(keys, numKeys);
    return (*env)->NewStringUTF(env, outSecret);
}

JNIEXPORT jstring JNICALL
Java_com_example_SecretService_mainSS_bigMath(
                                                JNIEnv *env,
                                                jstring A,
                                                jstring B,
                                                jint op) {
    const char *Astr = (*env)->GetStringUTFChars(env, A, 0);
    const char *Bstr = (*env)->GetStringUTFChars(env, B, 0);
    char *out = doBigMath(A, B, op);
    return (*env)->NewStringUTF(env, out);
}