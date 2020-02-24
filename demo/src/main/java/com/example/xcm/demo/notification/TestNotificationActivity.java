package com.example.xcm.demo.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.xcm.demo.R;
import com.example.xcm.demo.utils.NotificationUtils;
import com.example.xcm.demo.utils.Utils;

/**
 * Created by xcm on 18-3-27.
 */

public class TestNotificationActivity extends AppCompatActivity {

    private Vibrator vibrator;
    private Button bt1;
    private Button bt2;
    private CheckBox cbLedColor;
    private CheckBox cbChannelColor;
    private CheckBox cbUseDefault;
    private EditText edLedColor;
    private EditText edChannelColor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_notification);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        bt1 = findViewById(R.id.button1);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        });

        bt2 = findViewById(R.id.button2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessageDelayed(2, 50);
            }
        });


        cbLedColor = findViewById(R.id.cb_led_color);
        cbChannelColor = findViewById(R.id.cb_channel_color);
        cbUseDefault = findViewById(R.id.cb_use_default);

//        cbLedColor.setOnCheckedChangeListener(this);
//        cbChannelColor.setOnCheckedChangeListener(this);
//        cbUseDefault.setOnCheckedChangeListener(this);

        edLedColor = findViewById(R.id.ed_led_color);
        edChannelColor = findViewById(R.id.ed_channel_color);

    }

    private  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    NotificationUtils.showNotification1(TestNotificationActivity.this);
                    break;
                case 2:
                    vibrate();
                    break;
            }

        }
    };

    private void vibrate() {
        VibrationEffect ve = VibrationEffect.createWaveform(
                new long[]{200, 100, 200, 100}, new int[]{3, 1, 3, 1}, 2);
        vibrator.vibrate(ve);
    }

    private void showNotification() {
        int ledColor = 0;
        if (cbLedColor.isChecked()) {
            ledColor = Integer.valueOf(edLedColor.getText().toString().trim(), 16);
            Log.d("xcm", ledColor + "," + Integer.toHexString(ledColor));
        }

        int channelColor = 0;
        if (cbChannelColor.isChecked()) {
            channelColor = Integer.valueOf(edChannelColor.getText().toString().trim(), 16);
        }

        int defaultValue = 0;
        if (cbUseDefault.isChecked()) {
            defaultValue = Notification.DEFAULT_LIGHTS;
        }

        NotificationUtils.showNotification1(this,ledColor,channelColor,defaultValue);
    }
}
