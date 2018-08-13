package project.timweri.basicfilter;

import project.timweri.Filter;

public class BasicFilter extends Filter {
    static {
        System.loadLibrary("blend");
    }

    public native void solidBlendRGBA(long matAddrRGBA, byte R, byte G, byte B, float weight, boolean reset_cache);
}
