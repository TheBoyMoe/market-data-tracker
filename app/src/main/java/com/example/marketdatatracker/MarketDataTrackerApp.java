package com.example.marketdatatracker;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.timber.StethoTree;

import timber.log.Timber;

public class MarketDataTrackerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize Stetho
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this)) // enable cli
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)) // enable chrome dev tools
                .build());

        // enable Timber debugging in debug build
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree(){
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    // adding the line number to the tag
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
            // show logs in the Chrome browser console log via Stetho (works with Timber 3.0.1)
            Timber.plant(new StethoTree());
        }

    }


}
