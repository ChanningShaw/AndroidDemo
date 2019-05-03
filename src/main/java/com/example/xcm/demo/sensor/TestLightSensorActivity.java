package com.example.xcm.demo.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xcm.demo.R;

public class TestLightSensorActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvAmbientLux;
    private Button btStart;
    private Button btStop;

    private boolean mLightSensorEnabled = false;

    private SensorManager sensorManager;
    private Sensor mLightSensor;
    private Handler mHandler;
    private HandlerThread mWorker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_light_sensor);
        tvAmbientLux = findViewById(R.id.tv_ambient_lux);
        btStart = findViewById(R.id.bt_start_sense);
        btStop = findViewById(R.id.bt_end_sense);
        btStart.setOnClickListener(this);
        btStop.setOnClickListener(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mWorker = new HandlerThread("sensor-thread");
        mWorker.start();
        mHandler = new Handler(mWorker.getLooper());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start_sense:
                startSense();
                break;
            case R.id.bt_end_sense:
                stopSense();
                break;
        }
    }

    private void startSense() {
        mLightSensorEnabled = true;
        sensorManager.registerListener(mLightSensorListener, mLightSensor,
                100 * 1000, mHandler);
    }

    private void stopSense() {
        mLightSensorEnabled = false;
        sensorManager.unregisterListener(mLightSensorListener);
    }


    private final SensorEventListener mLightSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (mLightSensorEnabled) {
                final long time = SystemClock.uptimeMillis();
                final float lux = event.values[0];
                handleLightSensorEvent(time, lux);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void handleLightSensorEvent(long time, final float lux) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvAmbientLux.setText(lux + "");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSense();
    }
}
