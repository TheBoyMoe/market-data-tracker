package com.example.marketdatatracker.ui;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.service.GetQuoteThread;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class QuoteListActivity extends AppCompatActivity {

    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        if(getFragmentManager().findFragmentById(R.id.quote_list_container) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.quote_list_container, QuoteListFragment.newInstance())
                    .commit();
        }

    }



    // TODO EventBus not registered ???
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


    @SuppressWarnings("unused")
    public void onEventMainThread(AppMessageEvent event) {
        Timber.i("%s", event.getMessage());
        Snackbar.make(mCoordinatorLayout, event.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

}
