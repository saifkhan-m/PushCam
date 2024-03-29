package com.word.sample.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.word.sample.activity.CaptureActivity;
import com.word.sample.camera2.fragment.Camera2Fragment;
import com.word.sample.camera2.util.Camera2Listener;
import com.word.sample.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CaptureFragment extends Camera2Fragment implements Camera2Listener, SensorEventListener {

    private static final String VIDEO_DIRECTORY_NAME = "PushCam";
    private static double GVal = 3;
    public boolean starred = false;
    View mainview;
    ImageView buttonApp;
    ImageView button;
    private SensorManager sensorManager;
    private Sensor sensor;
    Double G;
    float x, y, z, stdG;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRationaleMessage("Hey man, we need to use your camera please!");

        /*
        the resources below were used to set up the sensor manager and other related aspects of the sensor

        https://developer.android.com/reference/android/hardware/SensorManager
        https://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-grav
         */

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_capture, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CaptureActivity) getActivity()).setCaptureFragment(this);
        button = (ImageView) view.findViewById(R.id.camera_control);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCameraControlClick((ImageView) v, 0.0);
            }
        });

//        buttonApp = (ImageView) view.findViewById(R.id.appControl);
//        buttonApp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onAppClick((ImageView)v);
//            }
//        });
    }

    @Override
    public int getTextureResource() {
        return R.id.camera_preview;
    }

    @Override
    public File getVideoFile(Context context) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
                VIDEO_DIRECTORY_NAME);
        // Create storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + VIDEO_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");
        Log.i(TAG, "the file " + mediaFile.getAbsoluteFile() + "-" + mediaFile.getName());
        return mediaFile;
    }

    public void onCameraControlClick(ImageView view, double sensorValue) {
        if (isRecording()) {
            Log.d("TEST", "File saved: " + getCurrentFile().getName());
            view.setImageResource(R.drawable.stopapp);
            Toast.makeText(getActivity().getApplicationContext(), "Recording saved ", Toast.LENGTH_SHORT).show();
            stopRecordingVideo();
            starred = false;
        } else if (!isRecording() || sensorValue > GVal) {
            if(!(sensorValue > GVal)) {
                Toast.makeText(getActivity().getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
                view.setImageResource(R.drawable.startapp);
            }else
                view.setImageResource(R.drawable.startbyg);
            starred = true;
            startRecordingVideo();
        }
    }
//    public void onCameraControlClick1() {
//        ImageView view=(ImageView) mainview;
//        if (isRecording()) {
//            Log.i("TEST", "File saved: " + getCurrentFile().getName());
//            view.setImageResource(R.drawable.ic_record);
//            stopRecordingVideo();
//        } else {
//            view.setImageResource(R.drawable.ic_pause);
//            startRecordingVideo();
//        }
//    }
//    public void onAppClick(ImageView view) {
//        mainview=view;
//        onCameraControlClick1();
//    }



    /*
    The below resources were used to capture data from the sensor event onSensorChanged

    https://developer.android.com/reference/android/hardware/SensorEvent
    https://www.youtube.com/watch?v=pkT7DU1Yo9Q&list=PLc2rvfiptPSQrErIlwHz7DZrNfhFdDXa6&index=2&t=0s
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        G = calcG(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], SensorManager.STANDARD_GRAVITY);

        if (G > GVal && !starred) {
            Toast.makeText(getActivity().getApplicationContext(), "Recording started due to high G ", Toast.LENGTH_SHORT).show();
            onCameraControlClick(button, G);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }



    /*
    The 2 resources below were used for the formula to get the value of G out of the x,y and z inputs

    https://stackoverflow.com/questions/11205930/what-is-a-maximum-g-force-value-possible-to-simulate-with-a-phone-in-a-hand
    https://stackoverflow.com/questions/6291931/how-to-calculate-g-force-using-x-y-z-values-from-the-accelerometer-in-android
     */
    double calcG(float x, float y, float z, float stdG) {
        return (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2))) / stdG;
    }

}