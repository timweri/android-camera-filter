#include <jni.h>
#include <opencv2/opencv.hpp>
#include <android/log.h>
#include <cstring>


using namespace cv;
using namespace std;


extern "C"
void JNICALL Java_project_timweri_filter_basicfilter_BasicFilter_solidBlendRGBA(JNIEnv *env, jobject instance,
                                                                         jlong mat_addr_RBGA,
                                                                         jchar R,
                                                                         jchar G,
                                                                         jchar B,
                                                                         jfloat weight,
                                                                         jboolean reset_cache
) {
    Mat *inputFrame = (Mat *) mat_addr_RBGA;
    static Mat solid_color;
    if (reset_cache || solid_color.empty()) {
        __android_log_print(ANDROID_LOG_INFO, "Info", "Rebuild solid_color");
        solid_color = inputFrame->clone();
        solid_color = solid_color.setTo(Scalar(R,G,B));
    }
    addWeighted(*inputFrame, 1 - weight, solid_color, weight, 0.0, *inputFrame);
}