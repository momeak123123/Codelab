#include <jni.h>
#include <string>
#include <iostream>
#include <bitset>
#include <iomanip>
#include <cmath>
#include <opencv2\highgui\highgui.hpp>
#include <opencv2\imgproc\imgproc.hpp>
#include <opencv2\core\core.hpp>

using namespace std;
using namespace cv;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_momeak_codelab_MainActivity_stringFromJNI(JNIEnv *env, jobject instance) {

    // TODO
    string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}