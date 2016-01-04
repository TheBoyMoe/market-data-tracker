package com.example.marketdatatracker.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.model.Stock;
import com.example.marketdatatracker.model.StockDataCache;
import com.example.marketdatatracker.ui.StockDetailActivity;

import timber.log.Timber;

public class StockDetailFragment extends Fragment{

    private Stock mStock;
    private View mView;

    public StockDetailFragment() {}

    public static StockDetailFragment newInstance(String symbol) {
        StockDetailFragment fragment = new StockDetailFragment();
        Bundle args = new Bundle();
        args.putString(StockDataCache.STOCK_OBJECT, symbol);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.stock_item_detail, container, false);

        // fetch the stock object from the data cache
        String symbol = getArguments().getString(StockDataCache.STOCK_OBJECT);
        mStock = StockDataCache.getStockDataCache().getStock(symbol);
        Timber.i("Detail fragment: %s", mStock.toString());

        // populate view elements
        displayStockStats();

        return mView;
    }



    private void displayStockStats() {

        TextView mStockName = (TextView) mView.findViewById(R.id.stock_name);
        TextView mStockPrice = (TextView) mView.findViewById(R.id.stock_price);
        TextView mStockSymbol = (TextView) mView.findViewById(R.id.stock_symbol);
        TextView mStockExchange = (TextView) mView.findViewById(R.id.stock_exchange);
        TextView mStockChange = (TextView) mView.findViewById(R.id.stock_change);
        TextView mStockChangeInPercent = (TextView) mView.findViewById(R.id.stock_change_in_percent);
        TextView mStockOpen = (TextView) mView.findViewById(R.id.stock_open);
        TextView mStockPreviousClose = (TextView) mView.findViewById(R.id.stock_previous_close);
        TextView mStockDayHi = (TextView) mView.findViewById(R.id.stock_day_high);
        TextView mStockDayLo = (TextView) mView.findViewById(R.id.stock_day_low);
        TextView mStockYearHi = (TextView) mView.findViewById(R.id.stock_year_high);
        TextView mStockYearLo = (TextView) mView.findViewById(R.id.stock_year_low);
        TextView mStockCapitalisation = (TextView) mView.findViewById(R.id.stock_capitalisation);
        TextView mStockVolume = (TextView) mView.findViewById(R.id.stock_volume);
        TextView mStockAverageVolume = (TextView) mView.findViewById(R.id.stock_average_volume);
        TextView mStockAnnualYield = (TextView) mView.findViewById(R.id.stock_annual_yield);
        TextView mStockAnnualPercentageYield = (TextView) mView.findViewById(R.id.stock_annual_percentage_yield);
        TextView mStockEarningsPerShare = (TextView) mView.findViewById(R.id.stock_earnings_per_share);


        // determine currency
        String currencySymbol;
        String currency = mStock.getCurrency();

        switch (currency) {
            case "EUR":
                currencySymbol = "€";
                break;
            case "USD":
                currencySymbol = "$";
                break;
            default:
                currencySymbol = "£";
        }

        String changeSymbol;
        if (mStock.getPrice().doubleValue() > mStock.getPreviousClose().doubleValue()) {
            changeSymbol = "+";
            mStockChange.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_light));
            //mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_green_18dp, 0, 0, 0);
            mStockChangeInPercent.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_light));
        }
        else if (mStock.getPrice().doubleValue() < mStock.getPreviousClose().doubleValue()) {
            changeSymbol = "";
            mStockChange.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));
            //mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_red_18dp, 0, 0, 0);
            mStockChangeInPercent.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));
        }
        else {
            changeSymbol = "";
            mStockChange.setPadding(18, 0, 0, 0);
        }

        // TODO properly format large numbers
        mStockName.setText(mStock.getName());
        mStockPrice.setText(String.format("%s%.2f", currencySymbol, mStock.getPrice()));
        mStockSymbol.setText(String.format("[%s]", mStock.getSymbol()));
        mStockExchange.setText(mStock.getStockExchange());
        mStockChange.setText(String.format("%s%.2f", changeSymbol, mStock.getChange()));
        mStockChangeInPercent.setText(String.format("%s%.2f", changeSymbol, mStock.getChangeInPercent()));
        mStockOpen.setText(String.format("%s%.2f", currencySymbol, mStock.getOpen()));
        mStockPreviousClose.setText(String.format("%s%.2f", currencySymbol, mStock.getPreviousClose()));
        mStockDayHi.setText(String.format("%s%.2f", currencySymbol, mStock.getDayHigh()));
        mStockDayLo.setText(String.format("%s%.2f", currencySymbol, mStock.getDayLow()));
        mStockYearHi.setText(String.format("%s%.2f", currencySymbol, mStock.getYearHigh()));
        mStockYearLo.setText(String.format("%s%.2f", currencySymbol, mStock.getYearLow()));
        mStockCapitalisation.setText(String.format("%s%.2f", currencySymbol, mStock.getMarketCapitalisation()));
        mStockVolume.setText(String.valueOf(mStock.getVolume()));
        mStockAverageVolume.setText(String.valueOf(mStock.getAvgVolume()));
        mStockAnnualYield.setText(String.valueOf(mStock.getAnnualYield()));
        mStockAnnualPercentageYield.setText(String.valueOf(mStock.getAnnualYieldPercentage()));
        mStockEarningsPerShare.setText(String.valueOf(mStock.getEarningsPerShare()));

    }



}
