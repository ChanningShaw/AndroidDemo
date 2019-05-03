package com.example.xcm.demo.brightness;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.xcm.demo.R;

public class AutoBrightnessDemoActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar mSeekBar;
    private DiagramView mDiagramView;
    private EditText mEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_brightness);
        mSeekBar = findViewById(R.id.seek_bar);
        mDiagramView = findViewById(R.id.curve_view);
        mSeekBar.setOnSeekBarChangeListener(this);
        mEditText = findViewById(R.id.lux);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String luxString = mEditText.getText().toString();
        float lux = 0.0f;
        try {
            lux = Float.parseFloat(luxString);
        } catch (Exception e) {
            Toast.makeText(this, "请输入合法的光照值，", Toast.LENGTH_SHORT);
            return;
        }
        mDiagramView.addUserDataPoint(lux, progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
