package com.example.marketdatatracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Custom stock object POJO
 */
public class Stock implements Parcelable {


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
                 BigDecimal earningsPerShare) {

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

    @Override
    public String toString() {
        return String.format("%s %s %.2f %.2f %.2f",
                getName(), getSymbol(), getPrice(), getDayLow(), getDayHigh());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mCurrency);
        dest.writeString(this.mSymbol);
        dest.writeString(this.mStockExchange);
        dest.writeSerializable(this.mPrice);
        dest.writeLong(this.mAvgVolume);
        dest.writeLong(this.mVolume);
        dest.writeSerializable(this.mDayHigh);
        dest.writeSerializable(this.mDayLow);
        dest.writeSerializable(this.mYearHigh);
        dest.writeSerializable(this.mYearLow);
        dest.writeSerializable(this.mOpen);
        dest.writeSerializable(this.mPreviousClose);
        dest.writeSerializable(this.mLastTradeDateTime);
        dest.writeSerializable(this.mMarketCapitalisation);
        dest.writeSerializable(this.mEarningsPerShare);
    }

    protected Stock(Parcel in) {
        this.mName = in.readString();
        this.mCurrency = in.readString();
        this.mSymbol = in.readString();
        this.mStockExchange = in.readString();
        this.mPrice = (BigDecimal) in.readSerializable();
        this.mAvgVolume = in.readLong();
        this.mVolume = in.readLong();
        this.mDayHigh = (BigDecimal) in.readSerializable();
        this.mDayLow = (BigDecimal) in.readSerializable();
        this.mYearHigh = (BigDecimal) in.readSerializable();
        this.mYearLow = (BigDecimal) in.readSerializable();
        this.mOpen = (BigDecimal) in.readSerializable();
        this.mPreviousClose = (BigDecimal) in.readSerializable();
        this.mLastTradeDateTime = (Calendar) in.readSerializable();
        this.mMarketCapitalisation = (BigDecimal) in.readSerializable();
        this.mEarningsPerShare = (BigDecimal) in.readSerializable();
    }

    public static final Parcelable.Creator<Stock> CREATOR = new Parcelable.Creator<Stock>() {
        public Stock createFromParcel(Parcel source) {
            return new Stock(source);
        }

        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };
}
