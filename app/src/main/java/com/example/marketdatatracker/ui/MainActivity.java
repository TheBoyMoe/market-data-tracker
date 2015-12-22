package com.example.marketdatatracker.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.service.GetQuoteThread;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetQuoteThread().start();
    }
}
