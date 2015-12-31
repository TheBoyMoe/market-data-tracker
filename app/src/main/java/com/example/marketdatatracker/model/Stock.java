package com.example.marketdatatracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Custom stock object POJO
 */
public class Stock {


    private String mName;
    private String mCurrency;
    private String mSymbol;
    private String mStockExchange;
    private BigDecimal mPrice;
    private long mAvgVolume;
    private long mVolume;
    private BigDecimal mDayHigh;
    private BigDecimal mDayLow;
    private BigDecimal mYearHigh;
    private BigDecimal mYearLow;
    private BigDecimal mOpen;
    private BigDecimal mPreviousClose;
    private Calendar mLastTradeDateTime;
    private BigDecimal mMarketCapitalisation;
    private BigDecimal mEarningsPerShare;
    private BigDecimal mChange;
    private BigDecimal mChangeInPercent;


    public Stock(String name,
                 String currency,
                 String symbol,
                 String stockExchange,
                 BigDecimal price,
                 long avgVolume,
                 long volume,
                 BigDecimal dayHigh,
                 BigDecimal dayLow,
                 BigDecimal yearHigh,
                 BigDecimal yearLow,
                 BigDecimal open,
                 BigDecimal previousClose,
                 Calendar lastTradeDateTime,
                 BigDecimal marketCapitalisation,
                 BigDecimal earningsPerShare,
                 BigDecimal change,
                 BigDecimal changeInPercent) {

        mName = name;
        mCurrency = currency;
        mSymbol = symbol;
        mStockExchange = stockExchange;
        mPrice = price;
        mAvgVolume = avgVolume;
        mVolume = volume;
        mDayHigh = dayHigh;
        mDayLow = dayLow;
        mYearHigh = yearHigh;
        mYearLow = yearLow;
        mOpen = open;
        mPreviousClose = previousClose;
        mLastTradeDateTime = lastTradeDateTime;
        mMarketCapitalisation = marketCapitalisation;
        mEarningsPerShare = earningsPerShare;
        mChange = change;
        mChangeInPercent = changeInPercent;
    }


    public String getName() {
        return mName;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public String getStockExchange() {
        return mStockExchange;
    }

    public BigDecimal getPrice() {
        return mPrice;
    }

    public long getAvgVolume() {
        return mAvgVolume;
    }

    public long getVolume() {
        return mVolume;
    }

    public BigDecimal getDayHigh() {
        return mDayHigh;
    }

    public BigDecimal getDayLow() {
        return mDayLow;
    }

    public BigDecimal getYearHigh() {
        return mYearHigh;
    }

    public BigDecimal getYearLow() {
        return mYearLow;
    }

    public BigDecimal getOpen() {
        return mOpen;
    }

    public BigDecimal getPreviousClose() {
        return mPreviousClose;
    }

    public Calendar getLastTradeDateTime() {
        return mLastTradeDateTime;
    }

    public BigDecimal getMarketCapitalisation() {
        return mMarketCapitalisation;
    }

    public BigDecimal getEarningsPerShare() {
        return mEarningsPerShare;
    }

    public BigDecimal getChange() {
        return mChange;
    }

    public BigDecimal getChangeInPercent() {
        return mChangeInPercent;
    }

    @Override
    public String toString() {
        return String.format("Name: %s symbol: %s currency: %s price: %.2f open: %.2f previous close: %.2f change: %.2f percent: %.2f",
                getName(), getSymbol(), getCurrency(), getPrice(),
                getOpen(), getPreviousClose(), getChange(), getChangeInPercent());
    }


}
