package com.example.marketdatatracker;

import android.app.Application;

import timber.log.Timber;

public class MarketDataTrackerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }


}
