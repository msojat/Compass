package hr.tvz.msojat.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;

public class OverlayView extends View implements SensorEventListener {

    public static final String DEBUG_TAG = "OverlayView";
    String compassData = "Compass Data";
    float [] mLastAccelerometer = new float[9];
    float [] mLastCompass = new float[9];
    Paint contentPaint;

    public OverlayView(Context context) {
        super(context);

        contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        contentPaint.setTypeface(Typeface.create("serif", Typeface.NORMAL));
        contentPaint.setTextAlign(Paint.Align.CENTER);
        contentPaint.setTextSize(100);
        contentPaint.setColor(Color.GREEN);

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        boolean isAccelAvailable = sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        boolean isCompassAvailable = sensorManager.registerListener(this, compassSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(compassData, canvas.getWidth() / 2, canvas.getHeight() / 4, contentPaint);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER : mLastAccelerometer = event.values; break;
            case Sensor.TYPE_MAGNETIC_FIELD :
                mLastCompass = event.values;
                calculateOrientation();
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void calculateOrientation(){
        if ((mLastCompass.length == 0) || (mLastAccelerometer.length == 0)) {return;}

        float rotation[] = new float[9];
        float identity[] = new float[9];
        boolean gotRotationMatrix = SensorManager.getRotationMatrix(rotation,
                identity, mLastAccelerometer, mLastCompass);

        if(gotRotationMatrix) {
            float cameraRotation[] = new float[9];
            SensorManager.remapCoordinateSystem(rotation, SensorManager.AXIS_X,
                    SensorManager.AXIS_Z, cameraRotation);

            float[] orientation = new float[3];
            SensorManager.getOrientation(cameraRotation, orientation);
            double degree = (double) orientation[0];
            if (degree < 0) {
                degree = degree + (2 * Math.PI);
            }
            degree = degree * 180 / Math.PI;
            if ((degree <= 45) || (degree >= 315)){
                contentPaint.setColor(Color.parseColor("#939393"));
                if ((degree <= 3) || (degree >= 357)){
                    contentPaint.setColor(Color.parseColor("#800000"));
                }
                compassData = "S";
            } else if ((degree >= 45) && (degree <= 135)) {
                contentPaint.setColor(Color.parseColor("#939393"));
                if ((degree >= 87) && (degree <= 93)){
                    contentPaint.setColor(Color.parseColor("#800000"));
                }
                compassData = "I";
            } else if ((degree >= 135) && (degree <= 225)) {
                contentPaint.setColor(Color.parseColor("#939393"));
                if ((degree >= 177) && (degree <= 183)){
                    contentPaint.setColor(Color.parseColor("#800000"));
                }
                compassData = "J";
            } else if ((degree >= 225) && (degree <= 315)) {
                contentPaint.setColor(Color.parseColor("#939393"));
                if ((degree >= 267) && (degree <= 273)){
                    contentPaint.setColor(Color.parseColor("#800000"));
                }
                compassData = "Z";
            } else {
                compassData = "";
            }

            this.invalidate();
        }
    }
}
