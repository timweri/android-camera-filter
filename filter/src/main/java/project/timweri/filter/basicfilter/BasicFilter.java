package project.timweri.filter.basicfilter;

import project.timweri.filter.Filter;

public class BasicFilter extends Filter {
    static {
        System.loadLibrary("filter");
    }

    public native void solidBlendRGBA(long matAddrRGBA, byte R, byte G, byte B, float weight, boolean reset_cache);
}
