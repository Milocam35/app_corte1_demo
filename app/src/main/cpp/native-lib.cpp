#include <jni.h>
#include <opencv2/opencv.hpp>

using namespace cv;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_app_1corte1_1demo_MainActivity_convertToGray(
        JNIEnv *env,
        jobject /* this */,
        jlong inputMatAddr,
        jlong outputMatAddr) {

    Mat &inputMat = *(Mat *) inputMatAddr;
    Mat grayMat;

    cvtColor(inputMat, grayMat, COLOR_RGBA2GRAY);
    cvtColor(grayMat, *(Mat*)outputMatAddr, COLOR_GRAY2RGBA);
}