cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_VERBOSE_MAKEFILE on)
include_directories (${OPENCV_DIR}/sdk/native/jni/include)
add_library(filter
            SHARED
            src/main/cpp/basicfilter/blend.cpp
            src/main/cpp/basicfilter/blur.cpp
            src/main/cpp/basicfilter/color.cpp)

add_library(lib_opencv SHARED IMPORTED)

set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${OPENCV_DIR}/sdk/native/libs/${ANDROID_ABI}/libopencv_java3.so)

target_link_libraries(filter
                      lib_opencv
                      log)