package com.example.xcm.demo.jni;

public class JniMethod {
    static {
        /**
         * 类加载时便加载库文件,文件名与实现所需的库函数的 C 代码文件名相同
         */
//        System.loadLibrary("JniMethod");
    }

    // native 方法
    public static native String getNativeString(String s);
}
