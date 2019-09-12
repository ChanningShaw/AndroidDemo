package com.example.xcm.demo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.xcm.demo.base.Config;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BitmapUtils {
    public static Bitmap scaleBitmap(Bitmap bitmap, int screenWidth, int screenHeight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHeight / h;
        // scale = scale < scale2 ? scale : scale2;
        matrix.postScale(scale, scale);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    public static Bitmap ReadBitmapById(Context context, int drawableId,
                                        int screenWidth, int screenHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return scaleBitmap(bitmap, screenWidth, screenHeight);
    }


    public static Bitmap decodeBitmapFitScreen(Context context, int drawableId) {
        Bitmap image = null;
        try {
            // 获取原图宽度
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeResource(context.getResources(), drawableId, options);

            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

            int size = 1;
            int scaleW = (options.outWidth / screenWidth);
            int scaleH = (options.outHeight / screenHeight);
            size = scaleW > scaleH ? scaleW : scaleH;

            // 计算缩放比例
            if (size <= 0) {
                size = 1;
            }
            Log.d(Config.TAG, "old-w:" + options.outWidth + ", size:" + size);
            // 缩放
            options.inJustDecodeBounds = false;
            options.inSampleSize = size;
            image = BitmapFactory.decodeResource(context.getResources(), drawableId, options);
            image = scaleBitmap(image, screenWidth, screenHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Bitmap imageARGB8888ToBitmap(DisplayMetrics metrics, Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();

        int width = image.getWidth();
        int height = image.getHeight();

        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;

        int offset = 0;
        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(metrics, width, height, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = 0;
                pixel |= (buffer.get(offset) & 0xff) << 16;     // R
                pixel |= (buffer.get(offset + 1) & 0xff) << 8;  // G
                pixel |= (buffer.get(offset + 2) & 0xff);       // B
                pixel |= (buffer.get(offset + 3) & 0xff) << 24; // A
                bitmap.setPixel(j, i, pixel);
                offset += pixelStride;
            }
            offset += rowPadding;
        }
        return bitmap;
    }

    /**
     * 这个方法可以转换，但是得到的图片右边多了一列，比如上面方法得到1080x2160，这个方法得到1088x2160
     * 所以要对得到的Bitmap裁剪一下
     *
     * @param image
     * @param config
     * @return
     */
    public static Bitmap imageToBitmap(Image image, Bitmap.Config config) {

        int width = image.getWidth();
        int height = image.getHeight();
        Bitmap bitmap;

        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Log.d(Config.TAG,
                "pixelStride:" + pixelStride + ". rowStride:" + rowStride + ". rowPadding" + rowPadding);

        bitmap = Bitmap.createBitmap(
                width + rowPadding / pixelStride/*equals: rowStride/pixelStride */
                , height, config);
        bitmap.copyPixelsFromBuffer(buffer);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height);
    }

    /**
     * PNG
     *
     * @return
     */
    public static byte[] bitmapToByte(Bitmap bmp, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, quality, baos);
        return baos.toByteArray();
    }

    public static Bitmap byteToBitmap(byte[] data) {
        if (data.length != 0) {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } else {
            return null;
        }
    }
}
