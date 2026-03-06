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

extern "C"
JNIEXPORT void JNICALL
Java_com_example_app_1corte1_1demo_MainActivity_applyExoticGaussian(
        JNIEnv *env,
        jobject /* this */,
        jlong inputMatAddr,
        jlong outputMatAddr) {

    Mat &inputMat = *(Mat *) inputMatAddr;
    Mat &outputMat = *(Mat *) outputMatAddr;

    Mat blurred;
    Mat glowEffect;

    // Blur gaussiano fuerte
    GaussianBlur(inputMat, blurred, Size(31, 31), 15);

    // Mezclar imagen original con blur (efecto glow)
    addWeighted(inputMat, 1.2, blurred, -0.5, 0, glowEffect);

    // Ajuste de brillo
    glowEffect.convertTo(outputMat, -1, 1.0, 15);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_app_1corte1_1demo_CameraActivity_applySobel(
        JNIEnv *env,
        jobject /* this */,
        jlong inputMatAddr,
        jlong outputMatAddr) {

    Mat &inputMat = *(Mat *) inputMatAddr;
    Mat &outputMat = *(Mat *) outputMatAddr;

    Mat gray, grad_x, grad_y, abs_grad_x, abs_grad_y;

    cvtColor(inputMat, gray, COLOR_RGBA2GRAY);

    Sobel(gray, grad_x, CV_16S, 1, 0, 3);
    Sobel(gray, grad_y, CV_16S, 0, 1, 3);

    convertScaleAbs(grad_x, abs_grad_x);
    convertScaleAbs(grad_y, abs_grad_y);

    addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, gray);

    cvtColor(gray, outputMat, COLOR_GRAY2RGBA);
}