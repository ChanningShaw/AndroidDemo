package com.example.xcm.demo.base;

import com.example.xcm.demo.ams.ProviderDemoActivity;
import com.example.xcm.demo.ams.TestBackgroundActivity;
import com.example.xcm.demo.ams.TestLockTaskActivity;
import com.example.xcm.demo.animation.TestViewAnimationActivity;
import com.example.xcm.demo.brightness.AutoBrightnessDemoActivity;
import com.example.xcm.demo.crash.TestCrashActivity;
import com.example.xcm.demo.grahpic.TestCameraActivity;
import com.example.xcm.demo.grahpic.TestMediaProjectionActivity;
import com.example.xcm.demo.grahpic.TestShotViewActivity;
import com.example.xcm.demo.grahpic.TestSurfaceViewActivity;
import com.example.xcm.demo.grahpic.TestVideoViewActivity;
import com.example.xcm.demo.grahpic.opengl.AndroidGL2Activity;
import com.example.xcm.demo.grahpic.opengl.TestGLActivity;
import com.example.xcm.demo.handler.TestHandlerActivity;
import com.example.xcm.demo.image.TestColorFilterActivity;
import com.example.xcm.demo.input.TestInputRestrictedActivity;
import com.example.xcm.demo.ipc.TestAIDLActivity;
import com.example.xcm.demo.ipc.TestMemoryFileActivity;
import com.example.xcm.demo.jni.TestJNIActivity;
import com.example.xcm.demo.network.OkHttpTestActivity;
import com.example.xcm.demo.notification.TestNotificationActivity;
import com.example.xcm.demo.notification.TestVibratorActivity;
import com.example.xcm.demo.sensor.TestGravityActivity;
import com.example.xcm.demo.sensor.TestLightSensorActivity;
import com.example.xcm.demo.sensor.TestOrientationActivity;
import com.example.xcm.demo.task.TestJobSchedulerActivity;
import com.example.xcm.demo.view.TestAnimationDrawableActivity;
import com.example.xcm.demo.view.TestCanvasActivity;
import com.example.xcm.demo.view.TestDarkThemeActivity;
import com.example.xcm.demo.view.TestForceDarkActivity;
import com.example.xcm.demo.view.TestForceDarkActivity2;
import com.example.xcm.demo.view.TestForceDarkActivity3;
import com.example.xcm.demo.view.TestForceDarkActivity4;
import com.example.xcm.demo.view.TestPadListViewActivity;
import com.example.xcm.demo.view.TestShowViewOnChildThreadActivity;
import com.example.xcm.demo.wms.TestPipActivity;
import com.example.xcm.demo.wms.TestWmsActivity;

/**
 * 在这里配置层级列表的包含关系，需要构建一棵树
 */
public class RootCategoryContainer extends CategoryContainer<FirstLevelCategory> {

    public RootCategoryContainer(String name) {
        super(name);
        FirstLevelCategory first = new FirstLevelCategory("Notification");
        SecondLevelCategory second = new SecondLevelCategory("notification");
        second.mChildren.add(new Category(TestNotificationActivity.class));
        first.mChildren.add(second);
        second = new SecondLevelCategory("vibrator");
        second.mChildren.add(new Category(TestVibratorActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("IPC");
        second = new SecondLevelCategory("ashmem");
        second.mChildren.add(new Category(TestMemoryFileActivity.class));
        first.mChildren.add(second);
        second = new SecondLevelCategory("aidl");
        second.mChildren.add(new Category(TestAIDLActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("MultiThread");
        second = new SecondLevelCategory("handler");
        second.mChildren.add(new Category(TestHandlerActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("Job");
        second = new SecondLevelCategory("job scheduler");
        second.mChildren.add(new Category(TestJobSchedulerActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("Sensor");
        second = new SecondLevelCategory("gravity");
        second.mChildren.add(new Category(TestGravityActivity.class));
        second.mChildren.add(new Category(TestOrientationActivity.class));
        first.mChildren.add(second);
        second = new SecondLevelCategory("light");
        second.mChildren.add(new Category(TestLightSensorActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("Cmd");
        second = new SecondLevelCategory("cmd");
        second.mChildren.add(new Category(TestAIDLActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("AMS");
        second = new SecondLevelCategory("ams");
        second.mChildren.add(new Category(TestAIDLActivity.class));
        second.mChildren.add(new Category(ProviderDemoActivity.class));
        second.mChildren.add(new Category(TestBackgroundActivity.class));
        second.mChildren.add(new Category(TestLockTaskActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("View");
        second = new SecondLevelCategory("view");
        second.mChildren.add(new Category(TestAnimationDrawableActivity.class));
        second.mChildren.add(new Category(TestPadListViewActivity.class));
        second.mChildren.add(new Category(TestForceDarkActivity.class));
        second.mChildren.add(new Category(TestForceDarkActivity2.class));
        second.mChildren.add(new Category(TestForceDarkActivity3.class));
        second.mChildren.add(new Category(TestForceDarkActivity4.class));
        second.mChildren.add(new Category(TestDarkThemeActivity.class));
        second.mChildren.add(new Category(TestCanvasActivity.class));
        second.mChildren.add(new Category(TestShowViewOnChildThreadActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("Graphic");
        second = new SecondLevelCategory("camera");
        second.mChildren.add(new Category(TestCameraActivity.class));
        first.mChildren.add(second);
        second = new SecondLevelCategory("media");
        second.mChildren.add(new Category(TestVideoViewActivity.class));
        second.mChildren.add(new Category(TestSurfaceViewActivity.class));
        second.mChildren.add(new Category(TestMediaProjectionActivity.class));
        first.mChildren.add(second);
        second = new SecondLevelCategory("opengl");
        second.mChildren.add(new Category(TestGLActivity.class));
        second.mChildren.add(new Category(AndroidGL2Activity.class));
        first.mChildren.add(second);
        second = new SecondLevelCategory("image");
        second.mChildren.add(new Category(TestColorFilterActivity.class));
        second.mChildren.add(new Category(TestShotViewActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("JNI");
        second = new SecondLevelCategory("jni");
        second.mChildren.add(new Category(TestJNIActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("Network");
        second = new SecondLevelCategory("okhttp");
        second.mChildren.add(new Category(OkHttpTestActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("Window");
        second = new SecondLevelCategory("window");
        second.mChildren.add(new Category(TestWmsActivity.class));
        second.mChildren.add(new Category(TestPipActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("Brightness");
        second = new SecondLevelCategory("auto bright");
        second.mChildren.add(new Category(AutoBrightnessDemoActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);


        first = new FirstLevelCategory("Stability");
        second = new SecondLevelCategory("crash");
        second.mChildren.add(new Category(TestCrashActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("Animation");
        second = new SecondLevelCategory("view animation");
        second.mChildren.add(new Category(TestViewAnimationActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        first = new FirstLevelCategory("Input");
        second = new SecondLevelCategory("input");
        second.mChildren.add(new Category(TestInputRestrictedActivity.class));
        first.mChildren.add(second);
        mChildren.add(first);

        sort();
    }
}
