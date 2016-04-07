package com.example.marketdatatracker.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.model.StockItem;
import com.example.marketdatatracker.model.data.StockDataCache;

public class StockDetailFragment extends Fragment {


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
        View view =  inflater.inflate(R.layout.stock_item_detail, container, false);

        // fetch the stockItem object from the data cache
        String symbol = getArguments().getString(StockDataCache.STOCK_OBJECT);
        StockItem stockItem = StockDataCache.getStockDataCache().getStock(symbol);

        // populate view elements
        displayStockStats(view, stockItem);

        return view;
    }


    private void displayStockStats(View view, StockItem stockItem) {

        TextView stockName = (TextView) view.findViewById(R.id.stock_name);
        TextView stockPrice = (TextView) view.findViewById(R.id.stock_price);
        TextView stockSymbol = (TextView) view.findViewById(R.id.stock_symbol);
        TextView stockExchange = (TextView) view.findViewById(R.id.stock_exchange);
        TextView stockChange = (TextView) view.findViewById(R.id.stock_change);
        TextView stockChangeInPercent = (TextView) view.findViewById(R.id.stock_change_in_percent);
        TextView stockOpen = (TextView) view.findViewById(R.id.stock_open);
        TextView stockPreviousClose = (TextView) view.findViewById(R.id.stock_previous_close);
        TextView stockDayHi = (TextView) view.findViewById(R.id.stock_day_high);
        TextView stockDayLo = (TextView) view.findViewById(R.id.stock_day_low);
        TextView stockYearHi = (TextView) view.findViewById(R.id.stock_year_high);
        TextView stockYearLo = (TextView) view.findViewById(R.id.stock_year_low);
        TextView stockCapitalisation = (TextView) view.findViewById(R.id.stock_capitalisation);
        //TextView StockVolume = (TextView) view.findViewById(R.id.stock_volume);
        //TextView StockAverageVolume = (TextView) view.findViewById(R.id.stock_average_volume);
        //TextView StockAnnualYield = (TextView) view.findViewById(R.id.stock_annual_yield);
        //TextView StockAnnualPercentageYield = (TextView) view.findViewById(R.id.stock_annual_percentage_yield);
        TextView stockEarningsPerShare = (TextView) view.findViewById(R.id.stock_earnings_per_share);


        // determine currency
        String currencySymbol;
        String currency = stockItem.getCurrency();

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
        if (stockItem.getPrice() > stockItem.getPreviousClose()) {
            changeSymbol = "+";
            stockChange.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_light));
            //mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_green_18dp, 0, 0, 0);
            stockChangeInPercent.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_green_light));
        }
        else if (stockItem.getPrice() < stockItem.getPreviousClose()) {
            changeSymbol = "";
            stockChange.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));
            //mStockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_red_18dp, 0, 0, 0);
            stockChangeInPercent.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));
        }
        else {
            changeSymbol = "";
            stockChange.setPadding(18, 0, 0, 0);
        }


        stockName.setText(stockItem.getName());
        stockPrice.setText(String.format("%s%.2f", currencySymbol, stockItem.getPrice()));
        stockSymbol.setText(String.format("[%s]", stockItem.getSymbol()));
        stockExchange.setText(stockItem.getStockExchange());
        stockChange.setText(String.format("%s%.2f", changeSymbol, stockItem.getChange()));
        stockChangeInPercent.setText(String.format("%s%.2f", changeSymbol, stockItem.getChangeInPercent()));
        stockOpen.setText(String.format("%s%.2f", currencySymbol, stockItem.getOpen()));
        stockPreviousClose.setText(String.format("%s%.2f", currencySymbol, stockItem.getPreviousClose()));
        stockDayHi.setText(String.format("%s%.2f", currencySymbol, stockItem.getDayHigh()));
        stockDayLo.setText(String.format("%s%.2f", currencySymbol, stockItem.getDayLow()));
        stockYearHi.setText(String.format("%s%.2f", currencySymbol, stockItem.getYearHigh()));
        stockYearLo.setText(String.format("%s%.2f", currencySymbol, stockItem.getYearLow()));
        stockCapitalisation.setText(String.format("%s%s", currencySymbol, String.format("%,.0fM", stockItem.getMarketCapitalisation()/1000000.0)));
        //StockVolume.setText(String.valueOf(stockItem.getVolume()));
        //StockAverageVolume.setText(String.valueOf(stockItem.getAvgVolume()));
        //StockAnnualYield.setText(String.valueOf(stockItem.getAnnualYield()));
        //StockAnnualPercentageYield.setText(String.valueOf(stockItem.getAnnualYieldPercentage()));
        stockEarningsPerShare.setText(String.valueOf(stockItem.getEarningsPerShare()));

    }



}
