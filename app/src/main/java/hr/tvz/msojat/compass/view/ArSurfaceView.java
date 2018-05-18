package hr.tvz.msojat.compass.view;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ArSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    public static final String DEBUG_TAG = "ArSurfaceView";

    SurfaceHolder holder;
    Activity activity;

    public ArSurfaceView(Context context, Activity activity){
        super(context);

        this.activity = activity;
        this.holder = getHolder();
        this.holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
