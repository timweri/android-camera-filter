package project.timweri.androidcamerafilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FilterSelection extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_selection);
    }

    protected void showSolidBlend() {
        Intent intent = new Intent(this, CameraTest.class);
        startActivity(intent);
    }
}
