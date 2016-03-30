package com.example.marketdatatracker.ui;

import android.support.v7.app.AppCompatActivity;

import de.greenrobot.event.EventBus;

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


}
