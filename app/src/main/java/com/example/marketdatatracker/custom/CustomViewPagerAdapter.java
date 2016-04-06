package com.example.marketdatatracker.custom;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.marketdatatracker.ui.fragments.StockDetailFragment;
import com.example.marketdatatracker.ui.fragments.StockLineGraphFragment;


public class CustomViewPagerAdapter extends FragmentPagerAdapter {


    public static final int STOCK_DETAIL_FRAGMENT = 0;
    public static final int STOCK_GRAPH_FRAGMENT = 1;
    private static final int NUM_PAGES = 2;
    private String mSymbol;


    public CustomViewPagerAdapter(FragmentManager fm, String symbol) {
        super(fm);
        mSymbol = symbol;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case STOCK_DETAIL_FRAGMENT:
                return StockDetailFragment.newInstance(mSymbol);
            case STOCK_GRAPH_FRAGMENT:
                //return StockChartFragment.newInstance(mSymbol);
                return StockLineGraphFragment.newInstance(mSymbol);
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }


}
