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
#include <math.h>
#define random(x)(rand()%x)

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
Java_com_example_momeak_codelab_Bitmap_1dispose_Harriss(JNIEnv *env, jobject instance,
                                                        jlong Rgba) {

    // TODO
    srand((int)time(0));

    Mat& mRgb = *(Mat*)Rgba;
    Mat GRab;
    cvtColor(mRgb, GRab, COLOR_BGRA2GRAY);
    Mat dst, norm_dst, normScaleDsr;
    dst = Mat::zeros( mRgb.size(),CV_32FC1);
    int blockSize = 2;
    int Ksize = 3;
    double k = 0.04;
    int thresh = 80;
    cornerHarris( GRab,dst,blockSize,Ksize,k,BORDER_DEFAULT);
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
                circle(mRgb,Point(col,row),4,Scalar(random(255),random(255),random(255)),-1,8,0);
            }
            currenrRow++;
        }
    }

}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_Shits(JNIEnv *env, jobject instance,  jlong Rgba) {

    // TODO
    srand((int)time(0));

    Mat& mRgb = *(Mat*)Rgba;
    Mat GRab;
    cvtColor(mRgb, GRab, COLOR_BGRA2GRAY);
    vector<Point2f> corners;
    double qualityLevel=0.01;//角点检测可接受的最小特征值
    double minDistance=10;//角点之间的最小距离
    int blockSize=3;//计算导数自相关矩阵时指定的领域范围
    double k=0.04;//权重系数

    goodFeaturesToTrack( GRab,corners, 33, qualityLevel, minDistance, Mat(), blockSize, false, k);
    for(unsigned int i=0;i<corners.size();i++)
    {
        circle(mRgb,corners[i],4,Scalar(random(255),random(255),random(255)),-1,8,0);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_fasts(JNIEnv *env, jclass type,  jlong Rgba) {

    // TODO
    srand((int)time(0));
    Mat& mRgb = *(Mat*)Rgba;
    Mat GRab;
    cvtColor(mRgb, GRab, COLOR_BGRA2GRAY);
    vector<KeyPoint> v;
    Ptr<FeatureDetector> detector = FastFeatureDetector::create();
    detector->detect( GRab, v);
    for(  int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 4, Scalar(random(255),random(255),random(255)),-1,8,0);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_stars(JNIEnv *env, jclass type,  jlong Rgba) {

    // TODO
    srand((int)time(0));
     
    Mat& mRgb = *(Mat*)Rgba;
    Mat GRab;
    cvtColor(mRgb, GRab, COLOR_BGRA2GRAY);
    vector<KeyPoint> v;
    Ptr<FeatureDetector> detector = StarDetector::create();
    detector->detect( GRab, v);
    for(  int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 4, Scalar(random(255),random(255),random(255)),-1,8,0);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_sifts(JNIEnv *env, jclass type,  jlong Rgba) {

    // TODO
    srand((int)time(0));
     
    Mat& mRgb = *(Mat*)Rgba;
    Mat GRab;
    cvtColor(mRgb, GRab, COLOR_BGRA2GRAY);
    vector<KeyPoint> v;
    Ptr<FeatureDetector> detector = SIFT::create();
    detector->detect( GRab, v);
    for(  int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 4, Scalar(random(255),random(255),random(255)),-1,8,0);
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_surfs(JNIEnv *env, jclass type,  jlong Rgba) {

    // TODO
    srand((int)time(0));
     
    Mat& mRgb = *(Mat*)Rgba;
    Mat GRab;
    cvtColor(mRgb, GRab, COLOR_BGRA2GRAY);
    vector<KeyPoint> v;
    Ptr<FeatureDetector> detector = SURF::create();
    detector->detect(GRab, v);
    for(  int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 4, Scalar(random(255),random(255),random(255)),-1,8,0);
    }

}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_orbs(JNIEnv *env, jclass type, jlong Rgba) {

    // TODO
    srand((int)time(0));
    Mat& mRgb = *(Mat*)Rgba;
    Mat GRab;
    cvtColor(mRgb, GRab, COLOR_BGRA2GRAY);
    vector<KeyPoint> v;
    Ptr<FeatureDetector> detector = ORB::create();
    detector->detect( GRab, v);
    for(  int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 4, Scalar(random(255),random(255),random(255)),-1,8,0);
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_brisks(JNIEnv *env, jclass type, jlong Rgba) {

    // TODO
    srand((int)time(0));
    Mat& mRgb = *(Mat*)Rgba;
    Mat GRab;
    cvtColor(mRgb, GRab, COLOR_BGRA2GRAY);
    vector<KeyPoint> v;
    Ptr<FeatureDetector> detector = BRISK::create();
    detector->detect( GRab, v);
    for(  int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 4, Scalar(random(255),random(255),random(255)),-1,8,0);
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_outline(JNIEnv *env, jclass type, jlong Rgba) {

    // TODO
    srand((int)time(0));
    Mat grayMat,cannyEdges;
    Mat& mRgb = *(Mat*)Rgba;
    cvtColor(mRgb, grayMat, COLOR_BGR2GRAY);
    Canny(grayMat, cannyEdges, 10, 100);

    vector<std::vector<Point>> contours;
    vector<Vec4i> hierarchy;

    // 检测轮廓
    findContours(cannyEdges, contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE, Point(0, 0));

    for (int i = 0; i < contours.size(); i++) {
        drawContours(mRgb, contours, i, Scalar(random(255),random(255),random(255)), -1);
    }

}extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_momeak_codelab_Bitmap_1dispose_getMSSIM(JNIEnv *env, jclass type, jlong Rga,
                                                         jlong Rba) {

    // TODO
    Mat& i1 = *(Mat*)Rga;
    Mat& i2 = *(Mat*)Rba;
    const double C1 = 6.5025, C2 = 58.5225;
    /***************************** INITS **********************************/
    int d = CV_32F;

    Mat I1, I2;
    i1.convertTo(I1, d);           // cannot calculate on one byte large values
    i2.convertTo(I2, d);

    Mat I2_2 = I2.mul(I2);        // I2^2
    Mat I1_2 = I1.mul(I1);        // I1^2
    Mat I1_I2 = I1.mul(I2);        // I1 * I2

    /*************************** END INITS **********************************/

    Mat mu1, mu2;   // PRELIMINARY COMPUTING
    GaussianBlur(I1, mu1, Size(11, 11), 1.5);
    GaussianBlur(I2, mu2, Size(11, 11), 1.5);

    Mat mu1_2 = mu1.mul(mu1);
    Mat mu2_2 = mu2.mul(mu2);
    Mat mu1_mu2 = mu1.mul(mu2);

    Mat sigma1_2, sigma2_2, sigma12;

    GaussianBlur(I1_2, sigma1_2, Size(11, 11), 1.5);
    sigma1_2 -= mu1_2;

    GaussianBlur(I2_2, sigma2_2, Size(11, 11), 1.5);
    sigma2_2 -= mu2_2;

    GaussianBlur(I1_I2, sigma12, Size(11, 11), 1.5);
    sigma12 -= mu1_mu2;

    ///////////////////////////////// FORMULA ////////////////////////////////
    Mat t1, t2, t3;

    t1 = 2 * mu1_mu2 + C1;
    t2 = 2 * sigma12 + C2;
    t3 = t1.mul(t2);              // t3 = ((2*mu1_mu2 + C1).*(2*sigma12 + C2))

    t1 = mu1_2 + mu2_2 + C1;
    t2 = sigma1_2 + sigma2_2 + C2;
    t1 = t1.mul(t2);               // t1 =((mu1_2 + mu2_2 + C1).*(sigma1_2 + sigma2_2 + C2))

    Mat ssim_map;
    divide(t3, t1, ssim_map);      // ssim_map =  t3./t1;

    Scalar mssim = mean(ssim_map); // mssim = average of ssim map
    return (mssim[0] + mssim[1] + mssim[2]) / 3;

}