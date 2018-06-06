package hr.tvz.msojat.compass;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.FrameLayout;

import hr.tvz.msojat.compass.view.OverlayView;
import me.aflak.ezcam.EZCam;
import me.aflak.ezcam.EZCamCallback;

public class MainActivity extends AppCompatActivity {
    static final String DEBUG_TAG = "MainActivity";
    static final int MY_PERMISSIONS_REQUEST_CAMERA = 1234;
    EZCam cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cam = new EZCam(this);
        String id = cam.getCamerasList().get(CameraCharacteristics.LENS_FACING_BACK); // should check if LENS_FACING_BACK exist before calling get()
        cam.selectCamera(id);
        cam.setCameraCallback(new EZCamCallback() {
            @Override
            public void onCameraReady() {
                // triggered after cam.open(...)

                // then start the preview
                cam.startPreview();
            }

            @Override
            public void onPicture(Image image) {
            }

            @Override
            public void onError(String message) {
                // all errors will be passed through this methods
            }

            @Override
            public void onCameraDisconnected() {
                // camera disconnected
            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            cam.open(CameraDevice.TEMPLATE_PREVIEW, (TextureView) findViewById(R.id.textureView));
        }

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.ar_view);
        OverlayView overlayView = new OverlayView(getApplicationContext());
        frameLayout.addView(overlayView);
    }

    @Override
    protected void onDestroy() {
        cam.close();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    cam.open(CameraDevice.TEMPLATE_PREVIEW, (TextureView) findViewById(R.id.textureView));
                }
                break;
        }
    }
}
