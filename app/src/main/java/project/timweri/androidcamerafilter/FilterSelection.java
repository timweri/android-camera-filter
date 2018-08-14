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
        startActivity(intent);
    }
}
