package com.example.xcm.demo.grahpic.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class SmallGLUT {

    public static final float PI = 3.14159265358979323846f;
    
    
    static ByteBuffer getByteBufferFromByteArray( byte array[]) {
    	ByteBuffer buffer = ByteBuffer.allocateDirect(array.length);
    	buffer.put(array);
    	buffer.position(0);
    	return buffer;
    }

    /**
     * Java的缓冲区数据存储结构为大端字节序(BigEdian)，而OpenGl的数据为小端字节序（LittleEdian）,
     * 因为数据存储结构的差异，所以，在Android中使用OpenGl的时候必须要进行下转换
     * @param array
     * @return
     */
    static FloatBuffer getFloatBufferFromFloatArray(float[] array) {
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个float占4个字节
        ByteBuffer tempBuffer = ByteBuffer.allocateDirect(array.length * 4);
        // 数组排列用nativeOrder
        tempBuffer.order(ByteOrder.nativeOrder());
        // 从ByteBuffer创建一个浮点缓冲区
        FloatBuffer buffer = tempBuffer.asFloatBuffer();
        // 将坐标添加到FloatBuffer
        buffer.put(array);
        // 设置缓冲区来读取第一个坐标
        buffer.position(0);
        return buffer;
    }

    static IntBuffer getIntBufferFromIntArray(int[] array) {
        ByteBuffer tempBuffer = ByteBuffer.allocateDirect(array.length * 4);
        tempBuffer.order(ByteOrder.nativeOrder());
        IntBuffer buffer = tempBuffer.asIntBuffer();
        buffer.put(array);
        buffer.position(0);
        return buffer;
    }
}
