package com.example.marketdatatracker.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.service.GetQuoteThread;

public class QuoteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getFragmentManager().findFragmentById(R.id.quote_list_container) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.quote_list_container, QuoteListFragment.newInstance())
                    .commit();
        }

    }


    // TODO register/unregister Eventbus

    // TODO fetch message events, display as aSnackbar for the user

    // TODO change the activities layout to Coordinator to enable Snackbars



}
