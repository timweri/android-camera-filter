package project.timweri.androidcamerafilter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Spinner;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.Map;

import project.timweri.filter.Filter;
import project.timweri.filter.basicfilter.BasicFilter;


public class CameraTest extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;

    private Mat mRgba, mHsv;

    private static final int CAMERA_REQUEST_CODE = 1;

    private boolean reset_cache = true;

    private String filter_id;

    Filter.Blend currentFilter;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (ContextCompat.checkSelfPermission(CameraTest.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

        setContentView(R.layout.activity_camera_test);

        mOpenCvCameraView = findViewById(R.id.camera_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        // Check what filter was chosen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            filter_id = extras.getString("filter_id");
        } else {
            filter_id = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reset_cache = true;
        setupFilter();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableCamera();
    }

    private void disableCamera() {
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mHsv = new Mat(height, width, CvType.CV_8UC3);
    }

    @Override
    public void onCameraViewStopped() {
        mHsv.release();
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(final CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        final int[] position = new int[1];
        // get each frame from camera
        mRgba = inputFrame.rgba();
        currentFilter.applyBlend(mRgba, reset_cache);
        reset_cache = false;
        return mRgba;
    }

    private void setupFilter() {
        BasicFilter basicfilter = new BasicFilter();

        switch (filter_id) {
            case "solid_blend":
                currentFilter = basicfilter.new SolidBlend((char) 255, (char) 0, (char) 0, (float) 0.5);
                break;
            case "gaussian_blur":
                currentFilter = basicfilter.new GaussianBlur(5, 5);
                break;
            case "linear_interp":
                currentFilter = basicfilter.new LinearInterpolate(new float[]{0, 1},null, null);
                break;
            default:
                Log.e("FilterSelection", "Invalid filter chosen");
        }
    }
}
