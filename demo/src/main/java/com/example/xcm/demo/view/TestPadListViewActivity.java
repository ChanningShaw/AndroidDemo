package com.example.xcm.demo.view;


import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xcm.demo.R;

import java.util.ArrayList;
import java.util.List;

public class TestPadListViewActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<String> mContents = new ArrayList<>();
    private MyListAdapter mAdapter;
    private TextView mAddSlideItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContents = new ArrayList<>();
        mContents.add(String.valueOf(mContents.size() + 1));
        mListView = getListView();
        mListView.setOnItemClickListener(this);
        mAddSlideItem = createAddSlideItem();
        mAdapter = new MyListAdapter(this, R.layout.item_layout, mContents);
        mListView.setAdapter(mAdapter);
        if (mListView.getFooterViewsCount() <= 0) {
            mListView.addFooterView(mAddSlideItem);
        }
        MyOnFocusChangeListener listener = new MyOnFocusChangeListener();
        mListView.setOnFocusChangeListener(listener);
    }

    private TextView createAddSlideItem() {
        TextView textView = (TextView) ((LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_layout, null);

        textView.setText("Add item");

        return textView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view == mAddSlideItem) {
            addNewContent();
        }
    }

    private void addNewContent() {
        if (mContents == null) {
            mContents = new ArrayList<>();
        }
//        mContents.add(String.valueOf(mContents.size() + 1));
        mContents.add(0, String.valueOf(mContents.size() + 1));
        mAdapter.notifyDataSetChanged();
    }

    private static class MyListAdapter extends ArrayAdapter<String> {
        private List<String> mStrings;
        private final LayoutInflater mInflater;

        public MyListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            mStrings = objects;
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView textView;
            textView = (TextView) mInflater.inflate(R.layout.item_layout, null);
            textView.setText(getItem(position));
            // Work around fix way
            //textView.setPressed(true);
            // textView.setSelected(true);
            return textView;
        }
    }

    private class MyOnFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            //判断是否有焦点，如果有焦点则设置背景色为想要的颜色或者背景图片，当失去焦点的时候再设置为原来的颜色

            if (hasFocus == true) {
                //获得焦点
                v.setBackgroundResource(android.R.color.white);
            } else {
                //失去焦点
                v.setBackgroundResource(android.R.color.black);
            }
        }
    }
}