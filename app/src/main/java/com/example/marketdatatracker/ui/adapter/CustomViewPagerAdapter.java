package com.example.marketdatatracker.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.marketdatatracker.ui.fragments.StockDetailFragment;
import com.example.marketdatatracker.ui.fragments.StockGraphFragment;


public class CustomViewPagerAdapter extends FragmentPagerAdapter{


    public static final int STOCK_DETAIL_FRAGMENT = 0;
    public static final int STOCK_GRAPH_FRAGMENT = 1;
    private static final int NUM_PAGES = 2;


    public CustomViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case STOCK_DETAIL_FRAGMENT:
                return StockDetailFragment.newInstance();
            case STOCK_GRAPH_FRAGMENT:
                return StockGraphFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }


}
