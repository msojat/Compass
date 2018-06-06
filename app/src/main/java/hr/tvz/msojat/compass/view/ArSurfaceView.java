package hr.tvz.msojat.compass.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class ArSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Handler.Callback {
    public static final String DEBUG_TAG = "ArSurfaceView";

    Camera mCamera;

    static final int MY_PERMISSIONS_REQUEST_CAMERA = 1234;
    private static final int MSG_CAMERA_OPENED = 1;
    private static final int MSG_SURFACE_READY = 2;
    private final Handler mHandler = new Handler(this);
    SurfaceHolder mHolder;
    Activity mActivity;
    CameraManager mCameraManager;
    String[] mCameraIDsList;
    CameraDevice.StateCallback mCameraStateCB;
    CameraDevice mCameraDevice;

    public ArSurfaceView(Context context, Activity activity){
        super(context);

        this.mActivity = activity;
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
        this.mCameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

        try {
            mCameraIDsList = this.mCameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        mCameraStateCB = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                mCameraDevice = camera;
                mHandler.sendEmptyMessage(MSG_CAMERA_OPENED);
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {

            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {

            }
        };
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)){

            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            try {
                mCameraManager.openCamera(mCameraIDsList[1], mCameraStateCB, new Handler());
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        // old
//        mCamera = Camera.open();
//
//        Camera.CameraInfo info = new Camera.CameraInfo();
//        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
//
//        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
//        int degrees = 0;
//        switch(rotation){
//            case Surface.ROTATION_0: degrees = 0; break;
//            case Surface.ROTATION_90: degrees = 90; break;
//            case Surface.ROTATION_180: degrees = 180; break;
//            case Surface.ROTATION_270: degrees = 270; break;
//        }
//
//        mCamera.setDisplayOrientation((info.orientation - degrees + 360) % 360);
//
//        try {
//            mCamera.setPreviewDisplay(mHolder);
//        } catch (IOException e) {
//            Log.e(DEBUG_TAG, String.valueOf(e));
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size s: previewSizes) {
            if ((s.height <= height) && (s.width <= width)) {
                parameters.setPreviewSize(s.width, s.height);
                break;
            }
        }

        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            if (false){

            }
        } catch (Exception e){

        }

        mCamera.stopPreview();
        mCamera.release();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
