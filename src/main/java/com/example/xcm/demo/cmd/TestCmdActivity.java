package com.example.xcm.demo.cmd;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.xcm.demo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by xcm on 18-4-5.
 */

public class TestCmdActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            BufferedReader reader;
            try {
                //("ps -P|grep bg")执行失败，PC端adb shell ps -P|grep bg执行成功
                //Process process = Runtime.getRuntime().exec("ps -P|grep tv");
                //-P 显示程序调度状态，通常是bg或fg，获取失败返回un和er
                // Process process = Runtime.getRuntime().exec("ps -P");
                //打印进程信息，不过滤任何条件
                Process process = Runtime.getRuntime().exec("dumpsys display");
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("xcm", line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.d("xcm", "dump error");
            e.printStackTrace();
        }
    }
}
