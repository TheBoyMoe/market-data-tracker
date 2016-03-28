package com.example.marketdatatracker.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.marketdatatracker.R;

public class StockSelectorFragment extends Fragment {

    public StockSelectorFragment() {}

    public static StockSelectorFragment newInstance() {
        return new StockSelectorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stock_selector_content, container, false);
        ListView suggestionList = (ListView) view.findViewById(R.id.suggestion_list);
        EditText suggestion = (EditText) view.findViewById(R.id.symbol_suggestion);

        return view;
    }
}
