package com.example.xcm.demo.grahpic.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Context mContext;

    private Triangle mTriangle;
    private Square mSquare;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    // 最终矩阵 = mProjectionMatrix x mViewMatrix x mModel x View
    private final float[] mMVPMatrix = new float[16];
    // 投影变换矩阵
    private final float[] mProjectionMatrix = new float[16];

    private final float[] mViewMatrix = new float[16];

    private float[] mRotationMatrix = new float[16];


    public MyGLRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // initialize a triangle
        mTriangle = new Triangle(mContext);
        // initialize a square
        mSquare = new Square(mContext);
    }

    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


        // 设置旋转矩阵
        //long time = SystemClock.uptimeMillis() % 4000L;
        //float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);
        // Combine the rotation matrix with the projection and camera view
        // 将旋转矩阵应用到mMVPMatrix
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        mTriangle.draw(scratch);
        mSquare.draw(scratch);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // 设定 Screen space 的大小
        GLES20.glViewport(0, 0, width , height);
        float ratio = (float) width / height;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        // 正交投影
        // Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        // 透视投影，第1个参数 保存结果的矩阵，第2个是开始保存的下标偏移量，第3个是从z轴看y轴的角度，视角大小
        Matrix.perspectiveM(mProjectionMatrix, 0, 45, (float) width / height, 0.1f, 100f);
        // 平移矩阵，这里将z轴平移-2.5f
        Matrix.translateM(mProjectionMatrix, 0, 0f, 0f, -2.5f);
    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public volatile float mAngle;

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    void destroy() {
        mSquare.destroy();
    }

}