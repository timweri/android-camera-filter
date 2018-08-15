#include <jni.h>
#include <opencv2/opencv.hpp>
#include <android/log.h>


using namespace cv;
using namespace std;


jdouble interpolate(jdouble val, jdouble *buckets, jdouble *vals) {
    jint curbucket = (jint) (val / buckets[1]);
    return (val - buckets[curbucket + 1])
           / (buckets[curbucket] - buckets[curbucket + 1])
           * (vals[curbucket] - vals[curbucket + 1])
           + vals[curbucket + 1];
}

extern "C"
void JNICALL Java_project_timweri_filter_basicfilter_BasicFilter_linearInterpolate(
        JNIEnv *env,
        jobject instance,
        jlong channelAddr,
        jdoubleArray values
) {
    jsize val_len = env->GetArrayLength(values);
    jdouble *vals = env->GetDoubleArrayElements(values, 0);
    jdouble buckets[val_len];

    {
        jdouble acc = 0, inc = (jdouble) 1 / (jdouble) val_len;
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
            p[j] = 255 * interpolate((jdouble) p[j] / (jdouble) 255, buckets, vals);
        }
    }

    env->ReleaseDoubleArrayElements(values, vals, 0);
}
}