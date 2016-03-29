package com.example.marketdatatracker.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.util.Constants;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

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

            // create and register a listener callback when changes are made to the app's preferences
            SharedPreferences.OnSharedPreferenceChangeListener listener =
                    new SharedPreferences.OnSharedPreferenceChangeListener() {
                        @Override
                        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                            Timber.i("Portfolio preferences have changed");
                            EventBus.getDefault().post(new AppMessageEvent(Constants.STOCK_PORTFOLIO_HAS_CHANGED));
                        }
                    };

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            prefs.registerOnSharedPreferenceChangeListener(listener);
        }
    }


}
