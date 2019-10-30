package com.example.xcm.demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.xcm.demo.R;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<String> {
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