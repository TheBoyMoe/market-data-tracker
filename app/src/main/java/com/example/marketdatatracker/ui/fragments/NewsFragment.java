package com.example.marketdatatracker.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marketdatatracker.R;

public class NewsFragment extends Fragment {

    public NewsFragment() {}

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView view = (TextView) inflater.inflate(R.layout.generic_fragment, container, false);
        view.setText("News Fragment");

        return view;
    }
}
