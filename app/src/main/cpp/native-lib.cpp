#include <jni.h>
#include <string>
#include <iostream>
#include <opencv2\highgui\highgui.hpp>
#include <opencv2\imgproc\imgproc.hpp>
#include <opencv2\core\core.hpp>
#include <opencv2\features2d.hpp>
#include <opencv2\xfeatures2d.hpp>
#include <opencv2\features2d\features2d.hpp>
#include <opencv2\xfeatures2d\nonfree.hpp>
#include <opencv2\opencv.hpp>
#include <android\bitmap.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

class rand;

using namespace cv::xfeatures2d;
using namespace std;
using namespace cv;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_momeak_codelab_MainActivity_stringFromJNI(JNIEnv *env, jobject instance) {

    // TODO
    string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_statics_fasts(JNIEnv *env, jobject instance, jlong RGr,
                                              jlong Rgba) {

    // TODO
    Mat& mGr  = *(Mat*)RGr;
    Mat& mRgb = *(Mat*)Rgba;
    vector<KeyPoint> v;
    Ptr<FeatureDetector> detector = FastFeatureDetector::create(80);
    detector->detect(mGr, v);
    for(  int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 4, Scalar(255,0,0,255),-1,8,0);
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_Harriss(JNIEnv *env, jobject instance, jlong RGr,
                                                        jlong Rgba) {

    // TODO
    Mat& mGr  = *(Mat*)RGr;
    Mat& mRgb = *(Mat*)Rgba;
    Mat dst, norm_dst, normScaleDsr;
    dst = Mat::zeros(mGr.size(),CV_32FC1);
    int blockSize = 2;
    int Ksize = 3;
    double k = 0.04;
    int thresh = 80;
    cornerHarris(mGr,dst,blockSize,Ksize,k,BORDER_DEFAULT);
    normalize(dst,norm_dst,0,255,NORM_MINMAX,CV_32FC1,Mat());//归一化
    convertScaleAbs(norm_dst,normScaleDsr);

    for (int row = 0; row < mRgb.rows; row++)
    {
        uchar* currenrRow = normScaleDsr.ptr(row);
        for (int col = 0; col < mRgb.cols; col++)
        {
            int value = (int)* currenrRow;
            if (value>thresh)
            {
                circle(mRgb,Point(col,row),4,Scalar(255,0,0,255),-1,8,0);
            }
            currenrRow++;
        }
    }

}extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_Shits(JNIEnv *env, jobject instance, jlong RGr,
                                                      jlong Rgba) {

    // TODO
    Mat& mGr  = *(Mat*)RGr;
    Mat& mRgb = *(Mat*)Rgba;
    vector<Point2f> corners;
    double qualityLevel=0.01;//角点检测可接受的最小特征值
    double minDistance=10;//角点之间的最小距离
    int blockSize=3;//计算导数自相关矩阵时指定的领域范围
    double k=0.04;//权重系数

    goodFeaturesToTrack(mGr,
                        corners,
                        33,
                        qualityLevel,
                        minDistance,
                        Mat(),
                        blockSize,
                        false,
                        k);
    for(unsigned int i=0;i<corners.size();i++)
    {
        circle(mRgb,corners[i],4,Scalar(255,0,0,255),-1,8,0);
    }
}