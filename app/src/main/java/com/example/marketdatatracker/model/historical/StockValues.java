package com.example.marketdatatracker.model.historical;


public class StockValues {

    private String Symbol;
    private String Date;
    private String Open;
    private String High;
    private String Low;
    private String Close;
    private String Volume;
    private String Adj_Close;

    public StockValues() {}

    public StockValues(String symbol,
                       String date,
                       String open,
                       String high,
                       String low,
                       String close,
                       String volume,
                       String adj_Close) {

        Symbol = symbol;
        Date = date;
        Open = open;
        High = high;
        Low = low;
        Close = close;
        Volume = volume;
        Adj_Close = adj_Close;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOpen() {
        return Open;
    }

    public void setOpen(String open) {
        Open = open;
    }

    public String getHigh() {
        return High;
    }

    public void setHigh(String high) {
        High = high;
    }

    public String getLow() {
        return Low;
    }

    public void setLow(String low) {
        Low = low;
    }

    public String getClose() {
        return Close;
    }

    public void setClose(String close) {
        Close = close;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getAdj_Close() {
        return Adj_Close;
    }

    public void setAdj_Close(String adj_Close) {
        Adj_Close = adj_Close;
    }

    @Override
    public String toString() {
        return String.format("Symbol: %s, opening: %s, closing: %s",
                getSymbol(), getOpen(), getClose());
    }


}
