package com.example.marketdatatracker.ui;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.ui.fragments.StockSelectorFragment;

public class StockSelectorActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_selector);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        StockSelectorFragment selectorFragment = (StockSelectorFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
        if(selectorFragment == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, StockSelectorFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
