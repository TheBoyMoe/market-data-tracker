package com.example.marketdatatracker.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import com.example.marketdatatracker.R;

public class StockPrefsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // instantiate the PrefsFragment
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new StockPrefsFragment())
                    .commit();
        }
    }

    public static class StockPrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // load the preferences from Preferences.xml
            addPreferencesFromResource(R.xml.preferences);
        }
    }


}
