package com.example.xcm.demo.grahpic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.xcm.demo.base.Config;
import com.example.xcm.demo.utils.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenCapturer {
    private static MediaProjection sMediaProjection;
    boolean isScreenCaptureStarted;
    OnImageCaptureScreenListener mListener;
    private int mDensity;
    private Display mDisplay;
    private int mWidth;
    private int mHeight;
    private ImageReader mImageReader;
    private VirtualDisplay mVirtualDisplay;
    private Handler mHandler;
    private String STORE_DIR;
    private Context mContext;

    public ScreenCapturer(Context context, MediaProjection mediaProjection, String savePath) {
        sMediaProjection = mediaProjection;
        mContext = context;

        isScreenCaptureStarted = false;

        // 启动一个looper线程
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler();
                Looper.loop();
            }
        }.start();


        if (TextUtils.isEmpty(savePath)) {
            File externalFilesDir = mContext.getExternalFilesDir(null);
            Log.d(Config.TAG, "externalFilesDir:" + externalFilesDir.getAbsolutePath());
            if (externalFilesDir != null) {
                STORE_DIR = externalFilesDir.getAbsolutePath() + "/myScreenshots";
            } else {
                Toast.makeText(mContext, "No save path assigned!", Toast.LENGTH_SHORT);
            }
        } else {
            STORE_DIR = savePath;
        }
    }

    public ScreenCapturer startProjection() {
        if (sMediaProjection != null) {
            File storeDir = new File(STORE_DIR);
            if (!storeDir.exists()) {
                boolean success = storeDir.mkdirs();
                if (!success) {
                    Log.d(Config.TAG, "mkdir " + storeDir + "  failed");
                    return this;
                } else {
                    Log.d(Config.TAG, "mkdir " + storeDir + "  success");
                }
            } else {
                Log.d(Config.TAG, " " + storeDir + "  exist");
            }

        } else {
            Log.d(Config.TAG, "get mediaprojection failed");
        }

        try {
            Thread.sleep(500); // 防止截屏截到 显示截屏权限的窗口
            isScreenCaptureStarted = true;
        } catch (InterruptedException e) {

        }

        WindowManager window = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = window.getDefaultDisplay();
        final DisplayMetrics metrics = new DisplayMetrics();
        // use getMetrics is 2030, use getRealMetrics is 2160, the diff is NavigationBar's height
        mDisplay.getRealMetrics(metrics);
        mDensity = metrics.densityDpi;
        Log.d(Config.TAG, "metrics.widthPixels is " + metrics.widthPixels);
        Log.d(Config.TAG, "metrics.heightPixels is " + metrics.heightPixels);
        mWidth = metrics.widthPixels;//size.x;
        mHeight = metrics.heightPixels;//size.y;

        //start capture reader
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
        // 创建一个虚拟屏，用于截图
        mVirtualDisplay = sMediaProjection.createVirtualDisplay(
                "ScreenShot",
                mWidth,
                mHeight,
                mDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                mImageReader.getSurface(),
                null,
                mHandler);

        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {

                if (isScreenCaptureStarted) {

                    Image image = null;
                    FileOutputStream fos = null;
                    Bitmap bitmap = null;

                    try {
                        image = reader.acquireLatestImage();
                        if (image != null) {
//                            bitmap = ImageUtils.image_ARGB8888_2_bitmap(metrics, image);
                            bitmap = BitmapUtils.imageToBitmap(image, Bitmap.Config.ARGB_8888);
                            if (null != mListener) {
                                mListener.onImageCaptured(BitmapUtils.bitmapToByte(bitmap, 80));
                            }

                            Date currentDate = new Date();
                            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
                            String fileName = STORE_DIR + "/myScreen_" + date.format(
                                    currentDate) + ".png";
                            fos = new FileOutputStream(fileName);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            Log.d(Config.TAG, "End now!!!!!!  Screenshot saved in " + fileName);
                            Toast.makeText(mContext, "Screenshot saved in " + fileName,
                                    Toast.LENGTH_LONG);
                            stopProjection();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        if (null != fos) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (null != bitmap) {
                            bitmap.recycle();
                        }
                        if (null != image) {
                            image.close(); // close it when used and
                        }
                    }
                }
            }
        }, mHandler);
        sMediaProjection.registerCallback(new MediaProjectionStopCallback(), mHandler);
        return this;
    }

    public ScreenCapturer stopProjection() {
        isScreenCaptureStarted = false;
        Log.d(Config.TAG, "Screen captured");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (sMediaProjection != null) {
                    sMediaProjection.stop();
                }
            }
        });
        return this;
    }

    public ScreenCapturer setOnImageCaptureListener(OnImageCaptureScreenListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public interface OnImageCaptureScreenListener {
        void onImageCaptured(byte[] image);
    }

    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mVirtualDisplay != null) {
                        mVirtualDisplay.release();
                    }
                    if (mImageReader != null) {
                        mImageReader.setOnImageAvailableListener(null, null);
                    }
                    sMediaProjection.unregisterCallback(MediaProjectionStopCallback.this);
                }
            });
        }
    }
}