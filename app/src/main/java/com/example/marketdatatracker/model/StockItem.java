package com.example.marketdatatracker.model;

/**
 * Custom stock object POJO
 */
public class StockItem {

    private String name;
    private String currency;
    private String symbol;
    private String stockExchange;
    private double price;
    private long avgVolume;
    private long volume;
    private double dayHigh;
    private double dayLow;
    private double yearHigh;
    private double yearLow;
    private double open;
    private double previousClose;
    private double marketCapitalisation;
    private double earningsPerShare;
    private double change;
    private double changeInPercent;
    private double annualYield;
    private double annualYieldPercentage;


    public StockItem() {}

    public StockItem(String name,
                     String currency,
                     String symbol,
                     String stockExchange,
                     double price,
                     long avgVolume,
                     long volume,
                     double dayHigh,
                     double dayLow,
                     double yearHigh,
                     double yearLow,
                     double open,
                     double previousClose,
                     double marketCapitalisation,
                     double earningsPerShare,
                     double change,
                     double changeInPercent,
                     double annualYield,
                     double annualYieldPercentage) {

        this.name = name;
        this.currency = currency;
        this.symbol = symbol;
        this.stockExchange = stockExchange;
        this.price = price;
        this.avgVolume = avgVolume;
        this.volume = volume;
        this.dayHigh = dayHigh;
        this.dayLow = dayLow;
        this.yearHigh = yearHigh;
        this.yearLow = yearLow;
        this.open = open;
        this.previousClose = previousClose;
        this.marketCapitalisation = marketCapitalisation;
        this.earningsPerShare = earningsPerShare;
        this.change = change;
        this.changeInPercent = changeInPercent;
        this.annualYield = annualYield;
        this.annualYieldPercentage = annualYieldPercentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getAvgVolume() {
        return avgVolume;
    }

    public void setAvgVolume(long avgVolume) {
        this.avgVolume = avgVolume;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getDayHigh() {
        return dayHigh;
    }

    public void setDayHigh(double dayHigh) {
        this.dayHigh = dayHigh;
    }

    public double getDayLow() {
        return dayLow;
    }

    public void setDayLow(double dayLow) {
        this.dayLow = dayLow;
    }

    public double getYearHigh() {
        return yearHigh;
    }

    public void setYearHigh(double yearHigh) {
        this.yearHigh = yearHigh;
    }

    public double getYearLow() {
        return yearLow;
    }

    public void setYearLow(double yearLow) {
        this.yearLow = yearLow;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public double getMarketCapitalisation() {
        return marketCapitalisation;
    }

    public void setMarketCapitalisation(double marketCapitalisation) {
        this.marketCapitalisation = marketCapitalisation;
    }

    public double getEarningsPerShare() {
        return earningsPerShare;
    }

    public void setEarningsPerShare(double earningsPerShare) {
        this.earningsPerShare = earningsPerShare;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangeInPercent() {
        return changeInPercent;
    }

    public void setChangeInPercent(double changeInPercent) {
        this.changeInPercent = changeInPercent;
    }

    public double getAnnualYield() {
        return annualYield;
    }

    public void setAnnualYield(double annualYield) {
        this.annualYield = annualYield;
    }

    public double getAnnualYieldPercentage() {
        return annualYieldPercentage;
    }

    public void setAnnualYieldPercentage(double annualYieldPercentage) {
        this.annualYieldPercentage = annualYieldPercentage;
    }


}
