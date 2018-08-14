package project.timweri.filter;

import org.opencv.core.Mat;

public class Filter {
    public interface Blend {
        void applyBlend(Mat inputFrame, boolean reset_cache);
    }
}
