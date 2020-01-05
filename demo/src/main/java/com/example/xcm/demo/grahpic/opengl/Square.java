package com.example.xcm.demo.grahpic.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.xcm.demo.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square extends Shape {
    private FloatBuffer mVertexBuffer;
    // 绘制顺序
    private ShortBuffer mDrawListBuffer;

    private final FloatBuffer mTexVertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float[] squareCoords = {
            1, 1, 0,   // top right
            -1, 1, 0,  // top left
            -1, -1, 0, // bottom left
            1, -1, 0,  // bottom right
    };

    private short[] drawOrder = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    // 纹理的坐标系
    private static final float[] TEX_VERTEX = {   // in clockwise order:
            1, 0,  // bottom right
            0, 0,  // bottom left
            0, 1,  // top left
            1, 1,  // top right
    };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.23671875f, 0.26953125f, 0.72265625f, 0.1f};

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";
    /**
     * uniform 由外部程序传递给 shader，就像是C语言里面的常量，shader 只能用，不能改；
     * attribute 是只能在 vertex shader 中使用的变量；
     * varying 变量是 vertex 和 fragment shader 之间做数据传递用的。
     */
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "     gl_FragColor = texture2D(s_texture, v_texCoord);" +
                    "}";

    private final int mProgram;
    private int mPositionHandle;
    private int mTexCoordHandle;
    private int mTextureHandle;
    private int mTexSamplerHandle;

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    public Square(Context context) {
        super(context);
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(squareCoords);
        mVertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        mDrawListBuffer = dlb.asShortBuffer();
        mDrawListBuffer.put(drawOrder);
        mDrawListBuffer.position(0);

        mTexVertexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEX_VERTEX);
        mTexVertexBuffer.position(0);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // get handle to fragment shader's vColor member
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texCoord");
        mTexSamplerHandle = GLES20.glGetUniformLocation(mProgram, "s_texture");

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, mVertexBuffer);

        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0,
                mTexVertexBuffer);

        int[] texNames = new int[1];
        GLES20.glGenTextures(1, texNames, 0);
        mTextureHandle = texNames[0];

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.cat01);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    public void draw(float[] mvpMatrix) {

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniform1i(mTexSamplerHandle, 0);

        // 用 glDrawElements 来绘制，mDrawListBuffer 指定了顶点绘制顺序
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    @Override
    public void destroy() {
        GLES20.glDeleteTextures(1, new int[] { mTextureHandle }, 0);
    }
}
