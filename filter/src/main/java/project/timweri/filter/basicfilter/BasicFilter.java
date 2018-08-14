package project.timweri.filter.basicfilter;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.Size;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

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

    // public native void gaussianblurRGBA(long matAddrRGBA, double sigmaX, double sigmaY, int borderType, boolean reset_cache);

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

    public native void linearInterpolate(long channelAddr, float[] values);

    public class LinearInterpolate implements BasicFilter.Blend {
        public float[] vals_R = {0, 1};
        public float[] vals_G = {0, 1};
        public float[] vals_B = {0, 1};

        public LinearInterpolate(float[] values_R, float[] values_G, float[] values_B) {
            if (values_R != null) vals_R = values_R;
            if (values_G != null) vals_G = values_G;
            if (values_B != null) vals_B = values_B;
        }

        @Override
        public void applyBlend(Mat inputFrame, boolean reset_cache) {
            List<Mat> channels = new ArrayList(3);
            Core.split(inputFrame, channels);

            //linearInterpolate(channels.get(0).getNativeObjAddr(), vals_R);
            linearInterpolate(channels.get(1).getNativeObjAddr(), vals_G);
            linearInterpolate(channels.get(2).getNativeObjAddr(), vals_B);

            Core.merge(channels, inputFrame);
        }
    }
}
