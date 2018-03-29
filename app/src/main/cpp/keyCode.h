//
// Created by menta on 25/03/2018.
//

#ifndef PASS_ANDROID_KEYCODE_H
#define PASS_ANDROID_KEYCODE_H

#include <time.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

#ifdef __cplusplus
#include <iostream>
#include <vector>
#include "mpir.h"
#include "mpirxx.h"
extern "C" {
#endif

void generateKeyset(const char *secret, int threshold, int keys, char ** keySet);
char * decodeKeyset(char ** keySet, int numKeys);
char * doBigMath(const char *A, const char *B, int op);

#ifdef __cplusplus
}
#endif


#endif //PASS_ANDROID_KEYCODE_H
