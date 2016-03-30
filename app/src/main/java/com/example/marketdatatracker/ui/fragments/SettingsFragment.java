package com.example.marketdatatracker.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marketdatatracker.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
       return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView view = (TextView) inflater.inflate(R.layout.generic_fragment, container, false);
        view.setText("Settings Fragment");

        return view;
    }
}
