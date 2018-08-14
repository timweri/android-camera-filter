#include <jni.h>
#include <opencv2/opencv.hpp>

using namespace cv;
using namespace std;


extern "C"
void JNICALL Java_project_timweri_filter_basicfilter_BasicFilter_solidBlendRGBA(JNIEnv *env, jobject instance,
                                                                         jlong matAddrRBGA,
                                                                         jbyte R,
                                                                         jbyte G,
                                                                         jbyte B,
                                                                         jfloat weight,
                                                                         jboolean reset_cache
) {
    Mat *inputFrame = (Mat *) matAddrRBGA;
    Scalar color = Scalar(R, G, B);
    static Mat solid_color;
    if (reset_cache) solid_color = Mat(inputFrame->rows, inputFrame->cols, CV_8UC3, color);
    addWeighted(*inputFrame, 1 - weight, solid_color, weight, 0.0, *inputFrame);
}