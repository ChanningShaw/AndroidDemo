package com.example.xcm.demo;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xcm.demo.ams.MyReceiver;
import com.example.xcm.demo.base.CategoryContainer;
import com.example.xcm.demo.base.CategoryListViewAdapter;
import com.example.xcm.demo.base.ContentActivity;
import com.example.xcm.demo.base.RootCategoryContainer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "xcm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowManager.LayoutParams a = getWindow().getAttributes();
        setupView();
        BroadcastReceiver receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.miui.slide_cover");
        registerReceiver(receiver, filter);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    private void setupView() {
        ListView lv = findViewById(R.id.main_menu);
        final RootCategoryContainer mRoot = new RootCategoryContainer("root");
        BaseAdapter adapter = new CategoryListViewAdapter(mRoot.getChildren(), this);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startDomain(mRoot.getChildren().get(position));
            }
        });

    }

    private void startDomain(CategoryContainer container) {
        Intent intent = new Intent(MainActivity.this, ContentActivity.class);
        intent.putExtra("container", container);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 700) {
            Toast.makeText(this, "滑盖打开", Toast.LENGTH_SHORT).show();
        } else if (keyCode == 701) {
            Toast.makeText(this, "滑盖关闭", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
}
