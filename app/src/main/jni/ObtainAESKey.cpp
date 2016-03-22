//
// Created by SHAU-LOK on 3/22/16.
//


#include "com_shootloking_secretmessager_utility_JniUtils.h"
#include "stdio.h"


JNIEXPORT jstring JNICALL Java_com_shootloking_secretmessager_utility_JniUtils_getAESKey
        (JNIEnv *env, jobject obj) {

    return env->NewStringUTF("AES-128 SecretMessager");

}

