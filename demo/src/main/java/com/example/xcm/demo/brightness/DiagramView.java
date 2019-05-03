package com.example.xcm.demo.brightness;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.util.Preconditions;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.example.xcm.demo.utils.MathUtils;

import java.util.Arrays;

public class DiagramView extends View  {

    private static final String TAG = "xcm";
    private static final boolean DEBUG = true;

    private static final float LUX_GRAD_SMOOTHING = 0.25f;
    private static final float MAX_GRAD = 1.0f;

    private Paint mPaint;
    private int mScreenHeight = getResources().getDisplayMetrics().heightPixels;
    private int mScreenWidth = getResources().getDisplayMetrics().widthPixels;

    private  float[] lux_levels = new float[]{
            0, 1, 2, 3, 4, 5, 6, 8, 13, 17, 21, 26, 30, 34, 140, 310, 400,
            500, 600, 1000, 1200, 1500, 3000, 3500, 4000,
    };

    private float[] brightness_levels = new float[]{
            2, 2, 2, 3, 3, 5, 5, 12, 12, 20, 20, 39, 39, 43, 43, 55,
            55, 63, 63, 84, 93, 105, 200, 240, 255
    };
    private Spline mSpline;

    private final int START_X = 50;
    private final int AXIS_Y = mScreenHeight - 400;
    private float mAutoBrightnessAdjustment;
    private float mUserLux;
    private float mUserBrightness;
    private float mMaxGamma = 3.0f;

    public DiagramView(Context context) {
        this(context, null);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mSpline = Spline.createSpline(lux_levels, brightness_levels);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Preconditions.checkArgument(brightness_levels.length == lux_levels.length);
        drawAxis(canvas);
        drawCurve(canvas);
    }

    private void drawCurve(Canvas canvas) {
        mPaint.setStrokeWidth(10.0f);
        mPaint.setColor(Color.BLUE);
        float radioX = (mScreenWidth - START_X) * 1.0f / lux_levels[lux_levels.length - 1];
        float radioY = AXIS_Y * 1.0f / brightness_levels[brightness_levels.length - 1];
        final int n = brightness_levels.length;
        int x;
        int y;
        // draw control point
        for (int i = 0; i < n; i++) {
            x = (int) (lux_levels[i] * radioX) + START_X;
            y = AXIS_Y - (int) (brightness_levels[i] * radioY);
            canvas.drawPoint(x, y, mPaint);
        }

        mPaint.setStrokeWidth(2.0f);
        mPaint.setColor(Color.BLACK);
        for (int i = 0; i < lux_levels[lux_levels.length - 1]; i++) {
            x = (int) (i * radioX) + START_X;
            y = AXIS_Y - (int) (mSpline.interpolate(i) * radioY);
            canvas.drawPoint(x, y, mPaint);
        }
    }

    private void drawAxis(Canvas canvas) {
        mPaint.setStrokeWidth(5.0f);
        canvas.drawLine(START_X, AXIS_Y, mScreenWidth - START_X,
                mScreenHeight - 400, mPaint);
        canvas.drawLine(START_X, START_X, START_X,
                mScreenHeight - 400, mPaint);
        float maxY = brightness_levels[brightness_levels.length - 1];
        float maxX = lux_levels[lux_levels.length - 1];
        for (int i = 1; i <= 10; i++) {
            float ratio = 1.0f * i / 10;
            canvas.drawText(String.valueOf(ratio * maxX), ratio * (mScreenWidth - START_X),
                    AXIS_Y + 20, mPaint);
            canvas.drawText(String.valueOf(ratio * maxY), START_X - 40,
                    AXIS_Y - ratio * mScreenHeight, mPaint);
        }
    }

    public void addUserDataPoint(float lux, float brightness) {
        float unadjustedBrightness = mSpline.interpolate(lux);
        if (DEBUG){
            Log.d(TAG, "addUserDataPoint: (" + lux + "," + brightness + ")");
        }
        float adjustment = inferAutoBrightnessAdjustment(mMaxGamma,
                brightness /* desiredBrightness */,
                unadjustedBrightness /* currentBrightness */);
        if (DEBUG) {
            Log.d(TAG, "addUserDataPoint: " + mAutoBrightnessAdjustment + " => " +
                    adjustment);
        }
        mAutoBrightnessAdjustment = adjustment;
        mUserLux = lux;
        mUserBrightness = brightness;
        computeSpline();
        invalidate();
    }

    private void computeSpline() {
        Pair<float[], float[]> curve = getAdjustedCurve(lux_levels, brightness_levels, mUserLux,
                mUserBrightness, mAutoBrightnessAdjustment, mMaxGamma);
        lux_levels = curve.first;
        brightness_levels = curve.second;
        mSpline = Spline.createSpline(lux_levels, brightness_levels);
    }

    private Pair<float[], float[]> getAdjustedCurve(float[] lux, float[] brightness,
                                                    float userLux, float userBrightness,
                                                    float adjustment, float maxGamma) {
        float[] newLux = lux;
        float[] newBrightness = Arrays.copyOf(brightness, brightness.length);
        adjustment = MathUtils.constrain(adjustment, -1, 1);
        float gamma = MathUtils.pow(maxGamma, -adjustment);
        if (DEBUG) {
            Log.d(TAG, "getAdjustedCurve: " + maxGamma + "^" + -adjustment + "=" +
                    MathUtils.pow(maxGamma, -adjustment) + " == " + gamma);
        }
        if (gamma != 1) {
            for (int i = 0; i < newBrightness.length; i++) {
                newBrightness[i] = MathUtils.pow(newBrightness[i], gamma);
            }
        }
        if (userLux != -1) {
            Pair<float[], float[]> curve = insertControlPoint(newLux, newBrightness, userLux,
                    userBrightness);
            newLux = curve.first;
            newBrightness = curve.second;
        }
        return Pair.create(newLux, newBrightness);
    }

    private static Pair<float[], float[]> insertControlPoint(
            float[] luxLevels, float[] brightnessLevels, float lux, float brightness) {
        final int idx = findInsertionPoint(luxLevels, lux);
        final float[] newLuxLevels;
        final float[] newBrightnessLevels;
        if (idx == luxLevels.length) {
            newLuxLevels = Arrays.copyOf(luxLevels, luxLevels.length + 1);
            newBrightnessLevels  = Arrays.copyOf(brightnessLevels, brightnessLevels.length + 1);
            newLuxLevels[idx] = lux;
            newBrightnessLevels[idx] = brightness;
        } else if (luxLevels[idx] == lux) {
            newLuxLevels = Arrays.copyOf(luxLevels, luxLevels.length);
            newBrightnessLevels = Arrays.copyOf(brightnessLevels, brightnessLevels.length);
            newBrightnessLevels[idx] = brightness;
        } else {
            newLuxLevels = Arrays.copyOf(luxLevels, luxLevels.length + 1);
            System.arraycopy(newLuxLevels, idx, newLuxLevels, idx+1, luxLevels.length - idx);
            newLuxLevels[idx] = lux;
            newBrightnessLevels  = Arrays.copyOf(brightnessLevels, brightnessLevels.length + 1);
            System.arraycopy(newBrightnessLevels, idx, newBrightnessLevels, idx+1,
                    brightnessLevels.length - idx);
            newBrightnessLevels[idx] = brightness;
        }
        smoothCurve(newLuxLevels, newBrightnessLevels, idx);
        return Pair.create(newLuxLevels, newBrightnessLevels);
    }

    private static void smoothCurve(float[] lux, float[] brightness, int idx) {
        float prevLux = lux[idx];
        float prevBrightness = brightness[idx];
        // Smooth curve for data points above the newly introduced point
        for (int i = idx+1; i < lux.length; i++) {
            float currLux = lux[i];
            float currBrightness = brightness[i];
            float maxBrightness = prevBrightness * permissibleRatio(currLux, prevLux);
            float newBrightness = MathUtils.constrain(
                    currBrightness, prevBrightness, maxBrightness);
            if (newBrightness == currBrightness) {
                break;
            }
            prevLux = currLux;
            prevBrightness = newBrightness;
            brightness[i] = newBrightness;
        }
        // Smooth curve for data points below the newly introduced point
        prevLux = lux[idx];
        prevBrightness = brightness[idx];
        for (int i = idx-1; i >= 0; i--) {
            float currLux = lux[i];
            float currBrightness = brightness[i];
            float minBrightness = prevBrightness * permissibleRatio(currLux, prevLux);
            float newBrightness = MathUtils.constrain(
                    currBrightness, minBrightness, prevBrightness);
            if (newBrightness == currBrightness) {
                break;
            }
            prevLux = currLux;
            prevBrightness = newBrightness;
            brightness[i] = newBrightness;
        }
    }

    private static float permissibleRatio(float currLux, float prevLux) {
        return MathUtils.exp(MAX_GRAD
                * (MathUtils.log(currLux + LUX_GRAD_SMOOTHING)
                - MathUtils.log(prevLux + LUX_GRAD_SMOOTHING)));
    }

    private static int findInsertionPoint(float[] arr, float val) {
        for (int i = 0; i < arr.length; i++) {
            if (val <= arr[i]) {
                return i;
            }
        }
        return arr.length;
    }

    private static float inferAutoBrightnessAdjustment(float maxGamma,
                                                       float desiredBrightness, float currentBrightness) {
        float adjustment = 0;
        float gamma = Float.NaN;
        // Extreme edge cases: use a simpler heuristic, as proper gamma correction around the edges
        // affects the curve rather drastically.
        if (currentBrightness <= 0.1f || currentBrightness >= 0.9f) {
            adjustment = (desiredBrightness - currentBrightness) * 2;
            // Edge case: darkest adjustment possible.
        } else if (desiredBrightness == 0) {
            adjustment = -1;
            // Edge case: brightest adjustment possible.
        } else if (desiredBrightness == 1) {
            adjustment = +1;
        } else {
            // current^gamma = desired => gamma = log[current](desired)
            gamma = MathUtils.log(desiredBrightness) / MathUtils.log(currentBrightness);
            // max^-adjustment = gamma => adjustment = -log[max](gamma)
            adjustment = -MathUtils.log(gamma) / MathUtils.log(maxGamma);
        }
        adjustment = MathUtils.constrain(adjustment, -1, +1);
        if (DEBUG) {
            Log.d(TAG, "inferAutoBrightnessAdjustment: " + maxGamma + "^" + -adjustment + "=" +
                    MathUtils.pow(maxGamma, -adjustment) + " == " + gamma);
            Log.d(TAG, "inferAutoBrightnessAdjustment: " + currentBrightness + "^" + gamma + "=" +
                    MathUtils.pow(currentBrightness, gamma) + " == " + desiredBrightness);
        }
        return adjustment;
    }
}
