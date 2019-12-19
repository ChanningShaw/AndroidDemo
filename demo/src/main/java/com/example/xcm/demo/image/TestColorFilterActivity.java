package com.example.xcm.demo.image;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.xcm.demo.R;

public class TestColorFilterActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private final String TAG = TestColorFilterActivity.class.getSimpleName();

    ImageView imageView;

    Button originalButton;
    Button greenButton;
    Button redButton;
    Button blueButton;

    Paint paint;
    float[] colorMatrix = new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0
    };

    ColorMatrix matrix = new ColorMatrix();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_color_filter);
        imageView = findViewById(R.id.iv);
        findViewById(R.id.bt_original).setOnClickListener(this);
        findViewById(R.id.bt_green).setOnClickListener(this);
        findViewById(R.id.bt_red).setOnClickListener(this);
        findViewById(R.id.bt_blue).setOnClickListener(this);

        ((SeekBar)findViewById(R.id.seek_saturation)).setOnSeekBarChangeListener(this);

        ((SeekBar)findViewById(R.id.seek_bar_r1)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_r2)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_r3)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_r4)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_r5)).setOnSeekBarChangeListener(this);

        ((SeekBar)findViewById(R.id.seek_bar_g1)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_g2)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_g3)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_g4)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_g5)).setOnSeekBarChangeListener(this);

        ((SeekBar)findViewById(R.id.seek_bar_b1)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_b2)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_b3)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_b4)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_b5)).setOnSeekBarChangeListener(this);

        ((SeekBar)findViewById(R.id.seek_bar_a1)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_a2)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_a3)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_a4)).setOnSeekBarChangeListener(this);
        ((SeekBar)findViewById(R.id.seek_bar_a5)).setOnSeekBarChangeListener(this);


        matrix.setSaturation(-1);
        imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_original:
                imageView.setColorFilter(Color.TRANSPARENT);
                break;
            case R.id.bt_green:
                imageView.setColorFilter(Color.GREEN);
                break;
            case R.id.bt_red:
                imageView.setColorFilter(Color.RED);
                break;
            case R.id.bt_blue:
                imageView.setColorFilter(Color.BLUE);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, "onProgressChanged: progress = " + progress);
        float progressF = (progress - 50) * 1.0f / 50;
        Log.d(TAG, "onStopTrackingTouch: progress = " + progressF);
        switch (seekBar.getId()) {
            case R.id.seek_saturation:
                matrix.setSaturation(progressF);
                imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
                imageView.invalidate();
                return;
            case R.id.seek_bar_r1:
                ((EditText)findViewById(R.id.ed_r1)).setText(String.valueOf(progressF));
                colorMatrix[0] = progressF;
                break;
            case R.id.seek_bar_r2:
                ((EditText)findViewById(R.id.ed_r2)).setText(String.valueOf(progressF));
                colorMatrix[1] = progressF;
                break;
            case R.id.seek_bar_r3:
                ((EditText)findViewById(R.id.ed_r3)).setText(String.valueOf(progressF));
                colorMatrix[2] = progressF;
                break;
            case R.id.seek_bar_r4:
                ((EditText)findViewById(R.id.ed_r4)).setText(String.valueOf(progressF));
                colorMatrix[3] = progressF;
                break;
            case R.id.seek_bar_r5:
                ((EditText)findViewById(R.id.ed_r5)).setText(String.valueOf(progressF));
                colorMatrix[4] = progressF;
                break;
            case R.id.seek_bar_g1:
                ((EditText)findViewById(R.id.ed_g1)).setText(String.valueOf(progressF));
                colorMatrix[5] = progressF;
                break;
            case R.id.seek_bar_g2:
                ((EditText)findViewById(R.id.ed_g2)).setText(String.valueOf(progressF));
                colorMatrix[6] = progressF;
                break;
            case R.id.seek_bar_g3:
                ((EditText)findViewById(R.id.ed_g3)).setText(String.valueOf(progressF));
                colorMatrix[7] = progressF;
                break;
            case R.id.seek_bar_g4:
                ((EditText)findViewById(R.id.ed_g4)).setText(String.valueOf(progressF));
                colorMatrix[8] = progressF;
                break;
            case R.id.seek_bar_g5:
                ((EditText)findViewById(R.id.ed_g5)).setText(String.valueOf(progressF));
                colorMatrix[9] = progressF;
                break;
            case R.id.seek_bar_b1:
                ((EditText)findViewById(R.id.ed_b1)).setText(String.valueOf(progressF));
                colorMatrix[10] = progressF;
                break;
            case R.id.seek_bar_b2:
                ((EditText)findViewById(R.id.ed_b2)).setText(String.valueOf(progressF));
                colorMatrix[11] = progressF;
                break;
            case R.id.seek_bar_b3:
                ((EditText)findViewById(R.id.ed_b3)).setText(String.valueOf(progressF));
                colorMatrix[12] = progressF;
                break;
            case R.id.seek_bar_b4:
                ((EditText)findViewById(R.id.ed_b4)).setText(String.valueOf(progressF));
                colorMatrix[13] = progressF;
                break;
            case R.id.seek_bar_b5:
                ((EditText)findViewById(R.id.ed_b5)).setText(String.valueOf(progressF));
                colorMatrix[14] = progressF;
                break;
            case R.id.seek_bar_a1:
                ((EditText)findViewById(R.id.ed_a1)).setText(String.valueOf(progressF));
                colorMatrix[15] = progressF;
                break;
            case R.id.seek_bar_a2:
                ((EditText)findViewById(R.id.ed_a2)).setText(String.valueOf(progressF));
                colorMatrix[16] = progressF;
                break;
            case R.id.seek_bar_a3:
                ((EditText)findViewById(R.id.ed_a3)).setText(String.valueOf(progressF));
                colorMatrix[17] = progressF;
                break;
            case R.id.seek_bar_a4:
                ((EditText)findViewById(R.id.ed_a4)).setText(String.valueOf(progressF));
                colorMatrix[18] = progressF;
                break;
            case R.id.seek_bar_a5:
                ((EditText)findViewById(R.id.ed_a5)).setText(String.valueOf(progressF));
                colorMatrix[19] = progressF;
                break;
        }
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorFilter);
        imageView.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStartTrackingTouch: ");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
