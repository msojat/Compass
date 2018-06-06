package hr.tvz.msojat.compass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import hr.tvz.msojat.compass.view.ArSurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.ar_view);

        ArSurfaceView arSurfaceView = new ArSurfaceView(this, this);
        frameLayout.addView(arSurfaceView);

        OverlayView overlayView = new OverlayView(getApplicationContext());
        frameLayout.addView(overlayView);
    }
}
