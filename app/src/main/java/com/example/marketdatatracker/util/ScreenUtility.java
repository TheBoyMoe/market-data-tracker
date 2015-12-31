package com.example.marketdatatracker.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

/**
* Screen utility class from the Lynda.com course Building a Note Taking App for Android
* http://www.lynda.com/Android-tutorials/Building-Note-Taking-App-Android/377485-2.html
* by David Gassner
*/
public class ScreenUtility {

    private Activity activity;
    private float dpWidth;
    private float dpHeight;

    public ScreenUtility(Activity activity) {
        this.activity = activity;

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = activity.getResources().getDisplayMetrics().density;
        dpHeight = outMetrics.heightPixels / density;
        dpWidth = outMetrics.widthPixels / density;
    }

    public float getWidth() {
        return dpWidth;
    }

    public float getHeight() {
        return dpHeight;
    }

}

