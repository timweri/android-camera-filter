package project.timweri.androidcamerafilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FilterSelection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_selection);
    }

    protected void showSolidBlend(View view) {
        Intent intent = new Intent(this, CameraTest.class);
        intent.putExtra("filter_id", "solid_blend");
        startActivity(intent);
    }

    protected void showGaussianBlur(View view) {
        Intent intent = new Intent(this, CameraTest.class);
        intent.putExtra("filter_id", "gaussian_blur");
        startActivity(intent);
    }

    protected void showLinearInterpolate(View view) {
        Intent intent = new Intent(this, CameraTest.class);
        intent.putExtra("filter_id", "linear_interp");
        startActivity(intent);
    }

    protected void showAddToChannel(View view) {
        Intent intent = new Intent(this, CameraTest.class);
        intent.putExtra("filter_id", "add_to_channel");
        startActivity(intent);
    }
}
