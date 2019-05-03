package com.yichuang.ml_demo.tool;

public class Tools {
    /**
     * 将byte数组转换为int类型，注意这里进适用于大端模式，mnist的数据是按照大端存储的
     *
     * @param b
     * @return
     */
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static int byteToInt(byte b) {
        return (b & 0xFF);
    }

    public static int findMaxIndexInArray(float[] array) {
        int max = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > array[max]) {
                max = i;
            }
        }
        return max;
    }
}
