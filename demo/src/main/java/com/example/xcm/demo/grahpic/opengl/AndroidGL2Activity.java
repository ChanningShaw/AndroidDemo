package com.example.xcm.demo.grahpic.opengl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.example.xcm.demo.R;

public class AndroidGL2Activity extends Activity {
    private static final String DEBUG_TAG = "AndroidGL2Activity";
    CustomGL2SurfaceView mAndroidSurface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAndroidSurface = new CustomGL2SurfaceView(this);
        setContentView(mAndroidSurface);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAndroidSurface != null) {
            mAndroidSurface.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAndroidSurface != null) {
            mAndroidSurface.onResume();
        }
    }

    private class CustomGL2SurfaceView extends GLSurfaceView {
        final CustomRenderer renderer;

        public CustomGL2SurfaceView(Context context) {
            super(context);
            // request an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);
            renderer = new CustomRenderer();
            setRenderer(renderer);
        }
    }

    private class CustomRenderer implements GLSurfaceView.Renderer {
        private boolean initialized = false;
        private final float[] vertices = {
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        };
        private int shaderProgram = 0;
        private FloatBuffer verticesBuffer;
        private int mMVPMatrixHandle;

        // Model View Projection Matrix的缩写
        private final float[] mMVPMatrix = new float[16];
        private final float[] mProjectionMatrix = new float[16];
        private final float[] mViewMatrix = new float[16];

        public CustomRenderer() {
            verticesBuffer = SmallGLUT.getFloatBufferFromFloatArray(vertices);
        }

        @Override
        public void onDrawFrame(GL10 unused) {
            if (!initialized) {
                return;
            }
            // 设置相机的位置 (View matrix)
            // eyeX, eyeY,eyeZ 表示相机坐标的位置，centerX,centerY,centerZ表示 原点的位置，upX,upY,upZ表示相机顶部的朝向
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -5, 0f, 0f, 0f, 0.0f, 1.0f, 0.0f);
            // Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
            mMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix");
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glUseProgram(shaderProgram);
            GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 12,
                verticesBuffer);
            int colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
            float[] color = {0, 255, 0, 1.0f};
            GLES20.glUniform4fv(colorHandle, 1, color, 0);
            GLES20.glEnableVertexAttribArray(0);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        }

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height) {
            Log.v(DEBUG_TAG, "onSurfaceChanged");
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1);
            // 设置透视投影变换
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        }

        @Override
        public void onSurfaceCreated(GL10 unused, EGLConfig unused2) {
            try {
                initShaderProgram(R.raw.simple_vertex, R.raw.simple_fragment);
                initialized = true;
            } catch (Exception e) {
                Log.e(DEBUG_TAG, "Failed to init GL");
            }
        }

        private void initShaderProgram(int vertexId, int fragmentId)
            throws Exception {
            int vertexShader = loadAndCompileShader(GLES20.GL_VERTEX_SHADER, vertexId);
            int fragmentShader = loadAndCompileShader(GLES20.GL_FRAGMENT_SHADER, fragmentId);
            shaderProgram = GLES20.glCreateProgram();
            if (shaderProgram == 0) {
                throw new Exception("Failed to create shader program");
            }
            // attach the shaders to the program
            GLES20.glAttachShader(shaderProgram, vertexShader);
            GLES20.glAttachShader(shaderProgram, fragmentShader);
            // bind attribute in our vertex shader
            GLES20.glBindAttribLocation(shaderProgram, 0, "vPosition");
            // link the shaders
            GLES20.glLinkProgram(shaderProgram);
            // check the linker status
            int[] linkerStatus = new int[1];
            GLES20.glGetProgramiv(shaderProgram, GLES20.GL_LINK_STATUS,
                linkerStatus, 0);
            if (GLES20.GL_TRUE != linkerStatus[0]) {
                Log.e(DEBUG_TAG, "Linker Failure: "
                    + GLES20.glGetProgramInfoLog(shaderProgram));
                GLES20.glDeleteProgram(shaderProgram);
                throw new Exception("Program linker failed");
            }
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1);
        }

        /**
         *
         *
         * @param shaderType 创造顶点着色器类型(GLES20.GL_VERTEX_SHADER),
         *                   片段着色器类型 (GLES20.GL_FRAGMENT_SHADER)
         * @param shaderId
         * @return
         * @throws Exception
         */
        private int loadAndCompileShader(int shaderType, int shaderId)
            throws Exception {
            InputStream inputStream =
                AndroidGL2Activity.this.getResources().openRawResource(shaderId);
            String shaderCode = inputStreamToString(inputStream);
            int shader = GLES20.glCreateShader(shaderType);
            if (shader == 0) {
                throw new Exception("Can't create shader");
            }
            // hand the code over to GL
            GLES20.glShaderSource(shader, shaderCode);
            // compile it
            GLES20.glCompileShader(shader);
            // get compile status
            int[] status = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
            if (status[0] == 0) {
                // failed
                Log.e(DEBUG_TAG, "Compiler Failure: "
                    + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                throw new Exception("Shader compilation failed");
            }
            return shader;
        }

        public String inputStreamToString(InputStream is) throws IOException {
            StringBuilder sBuffer = new StringBuilder();
            DataInputStream dataIO = new DataInputStream(is);
            String strLine = null;
            while ((strLine = dataIO.readLine()) != null) {
                sBuffer.append(strLine).append("\n");
            }
            dataIO.close();
            is.close();
            return sBuffer.toString();
        }
    }
}
