#include <jni.h>
#include <opencv2/opencv.hpp>
#include <android/log.h>


using namespace cv;
using namespace std;


float interpolate(jfloat val, jfloat *buckets, jfloat *vals) {
    jint curbucket = (jint) (val / buckets[1]);
    return val;
    /*return (val - buckets[curbucket + 1])
           / (buckets[curbucket] - buckets[curbucket + 1])
           * (vals[curbucket] - vals[curbucket + 1])
           + vals[curbucket + 1];*/
}

extern "C"
void JNICALL Java_project_timweri_filter_basicfilter_BasicFilter_linearInterpolate(
        JNIEnv *env,
        jobject instance,
        jlong channelAddr,
        jfloatArray values
) {
    jsize val_len = env->GetArrayLength(values);
    jfloat *vals = env->GetFloatArrayElements(values, 0);
    jfloat buckets[val_len];

    {
        float acc = 0, inc = 1 / val_len;
        for (int i = 0; i < val_len - 1; ++i) {
            buckets[i] = acc;
            acc += inc;
        }
        buckets[val_len - 1] = 1;
    }

    Mat *inputFrame = (Mat *) channelAddr;

    CV_Assert(inputFrame->depth() == CV_8U);
    jint nRows = inputFrame->rows;
    jint nCols = inputFrame->cols;

    if (inputFrame->isContinuous()) {
        nCols *= nRows;
        nRows = 1;
    }

    uchar *p;
    for (int i = 0; i < nRows; ++i) {
        p = inputFrame->ptr<uchar>(i);
        for (int j = 0; j < nCols; ++j) {
            p[j] = (uchar) (255 * interpolate(p[j] / 255, buckets, vals));
        }
    }

    env->ReleaseFloatArrayElements(values, vals, 0);
}