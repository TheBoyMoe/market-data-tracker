package com.example.marketdatatracker.ui.fragments;


import android.support.v4.app.Fragment;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

}
