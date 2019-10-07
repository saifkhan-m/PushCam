package com.word.sample.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.word.sample.R;
import com.word.sample.fragment.CaptureFragment;

public class CaptureActivity extends AppCompatActivity{

    private SensorManager sensorManager;
    private Sensor sensor;
    Double G;
    float x,y,z,stdG;

    CaptureFragment captureFragment;

    private static final String TAG = "CaptureActivity";

    public void setCaptureFragment(CaptureFragment captureFragment) {
        this.captureFragment = captureFragment;
    }
    public CaptureFragment getCaptureFragment() {
        return captureFragment;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getFragmentManager().beginTransaction().add(R.id.content, new CaptureFragment()).commit();
    }
}
