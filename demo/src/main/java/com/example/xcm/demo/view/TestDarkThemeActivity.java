package com.example.xcm.demo.view;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;
import com.example.xcm.demo.utils.NightModeUtils;

import java.util.ArrayList;
import java.util.List;

public class TestDarkThemeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        RadioGroup.OnCheckedChangeListener {

    private static final String TAG = Config.TAG;

    View decorView;
    ListView mListView;
    MyListAdapter mAdapter;
    RadioGroup mRadioGroup;

    private List<String> mContents = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int nightMode = NightModeUtils.getNightMode(getApplicationContext());
        AppCompatDelegate.setDefaultNightMode(nightMode);
        setContentView(R.layout.test_dark_theme);
        decorView = getWindow().getDecorView();
        final Button bt = findViewById(R.id.bt);
        mListView = findViewById(R.id.lv);
        mListView.setOnItemClickListener(this);
        mContents.add("1111111111111");
        mContents.add("1111111111111");
        mContents.add("1111111111111");
        mContents.add("1111111111111");
        mAdapter = new MyListAdapter(this, R.layout.item_layout, mContents);
        mListView.setAdapter(mAdapter);

        mRadioGroup = findViewById(R.id.rg);
        mRadioGroup.setOnCheckedChangeListener(this);
        switch (nightMode) {
            case AppCompatDelegate.MODE_NIGHT_YES:
                mRadioGroup.check(R.id.rb_dark);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                mRadioGroup.check(R.id.rb_light);
                break;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                mRadioGroup.check(R.id.rb_follow_system);
                break;
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                mRadioGroup.check(R.id.rb_auto);
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        Log.d(TAG, "currentNightMode: " + currentNightMode);
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                Log.d(TAG, "Night mode is not active");
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                Log.d(TAG, "Night mode is active");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        switch (checkedId) {
            case R.id.rb_dark:
                mode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case R.id.rb_light:
                mode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case R.id.rb_auto:
                mode = AppCompatDelegate.MODE_NIGHT_AUTO;
                break;
            case R.id.rb_follow_system:
                mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
        }
        if (mode == NightModeUtils.getNightMode(getApplicationContext())) {
            return;
        }
        NightModeUtils.setNightMode(getApplicationContext(), mode);
        recreate();
        Log.d(TAG, "change mode to " + mode);
    }
}
