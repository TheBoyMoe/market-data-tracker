package com.example.marketdatatracker.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marketdatatracker.R;

public class StockGraphFragment extends Fragment{

    public StockGraphFragment() {}

    public static StockGraphFragment newInstance() {
        return new StockGraphFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView view = (TextView) inflater.inflate(R.layout.generic_fragment, container, false);
        view.setText(R.string.stock_graph_title);
        return view;
    }
}
