package project.timweri.filter.basicfilter;

import org.opencv.core.Mat;

import project.timweri.filter.Filter;

public class BasicFilter extends Filter {
    static {
        System.loadLibrary("filter");
    }

    public class SolidBlend implements BasicFilter.Blend {
        public char R, G, B;
        public float w;

        public SolidBlend(char red, char green, char blue, float weight) {
            R = red;
            G = green;
            B = blue;
            w = weight;
        }

        @Override
        public void applyBlend(Mat inputFrame, boolean reset_cache) {
            solidBlendRGBA(inputFrame.getNativeObjAddr(), R, G, B, w, reset_cache);
        }
    }

    public native void solidBlendRGBA(long matAddrRGBA, char R, char G, char B, float weight, boolean reset_cache);
}
