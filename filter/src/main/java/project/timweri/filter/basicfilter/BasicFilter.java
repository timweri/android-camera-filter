package project.timweri.filter.basicfilter;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.Size;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import project.timweri.filter.Filter;

public class BasicFilter extends Filter {
    static {
        System.loadLibrary("filter");
    }

    private Mat solid_color;

    public void solidBlendRGBA(Mat inputFrame, char R, char G, char B, float weight, boolean reset_cache) {
        if (reset_cache || solid_color.empty()) {
            solid_color = inputFrame.clone();
            solid_color = solid_color.setTo(new Scalar(R, G, B));
        }
        Core.addWeighted(inputFrame, 1 - weight, solid_color, weight, 0, inputFrame);
    }
    public native void solidBlendRGBA(long matAddrRGBA, char R, char G, char B, float weight, boolean reset_cache);

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
            solidBlendRGBA(inputFrame, R, G, B, w, reset_cache);
            // solidBlendRGBA(inputFrame.getNativeObjAddr(), R, G, B, reset_cache);
        }
    }

    // TODO: This is very slow. Implement ThreadPool to check if FPS improves.
    public void gaussianblurRGBA(Mat inputFrame, double sigmaX, double sigmaY, boolean reset_cache) {
        Imgproc.GaussianBlur(inputFrame, inputFrame, new Size(0, 0), sigmaX, sigmaY);
    }

    public native void gaussianblurRGBA(long matAddrRGBA, double sigmaX, double sigmaY, int borderType, boolean reset_cache);

    public class GaussianBlur implements BasicFilter.Blend {
        public double sX, sY;

        public GaussianBlur(double sigmaX, double sigmaY) {
            sX = sigmaX;
            sY = sigmaY;
        }

        @Override
        public void applyBlend(Mat inputFrame, boolean reset_cache) {
            gaussianblurRGBA(inputFrame, sX, sY, reset_cache);
            // gaussianblurRGBA(inputFrame.getNativeObjAddr(), sX, sY, reset_cache);
        }
    }
}
