package com.example.marketdatatracker.ui.fragments;


import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.marketdatatracker.R;
import com.example.marketdatatracker.custom.CustomMarkerView;
import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.data.HistoricalDataCache;
import com.example.marketdatatracker.model.historical.StockValues;
import com.example.marketdatatracker.network.GetHistoricalDataThread;
import com.example.marketdatatracker.util.Constants;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class StockLineGraphFragment extends BaseFragment implements
        View.OnClickListener{

    private String mSymbol;
    private List<StockValues> mList = new ArrayList<>();
    private LineChart mLineGraph;


    public StockLineGraphFragment() {}

    public static StockLineGraphFragment newInstance(String symbol) {

        StockLineGraphFragment fragment = new StockLineGraphFragment();
        Bundle args = new Bundle();
        args.putString(Constants.STOCK_SYMBOL_ITEM, symbol);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stock_line_graph, container, false);
        setupTimeFrameButtons(view);
        mLineGraph = (LineChart) view.findViewById(R.id.line_graph);

        mSymbol = getArguments().getString(Constants.STOCK_SYMBOL_ITEM);
        if(savedInstanceState == null) {
            executeQuery(mSymbol, Constants.HISTORICAL_STOCK_QUERY_FOR_ONE_MONTH);
        }

        if(savedInstanceState != null) {
            mList.clear();
            mList.addAll(HistoricalDataCache.getHistoricalDataCache().getHistoricalValues());
            displayChart(mLineGraph);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time_frame_one_month:
                executeQuery(mSymbol, Constants.HISTORICAL_STOCK_QUERY_FOR_ONE_MONTH);
                break;
            case R.id.time_frame_three_months:
                executeQuery(mSymbol, Constants.HISTORICAL_STOCK_QUERY_FOR_THREE_MONTH);
                break;
            case R.id.time_frame_six_months:
                executeQuery(mSymbol, Constants.HISTORICAL_STOCK_QUERY_FOR_SIX_MONTH);
                break;
            case R.id.time_frame_twelve_months:
                executeQuery(mSymbol, Constants.HISTORICAL_STOCK_QUERY_FOR_TWELVE_MONTH);
                break;
        }
    }

    private void displayChart(LineChart lineGraph) {

        lineGraph.setDescription(mList.get(0).getSymbol() + " close values");
        lineGraph.setNoDataTextDescription("Data set empty");
        lineGraph.setTouchEnabled(true);
        lineGraph.setDragEnabled(true);
        lineGraph.setScaleEnabled(true);
        lineGraph.setPinchZoom(true);
        lineGraph.getLegend().setEnabled(false); // hide legend at base of x-axis

        // set marker view
        CustomMarkerView mv = new CustomMarkerView(getActivity(), R.layout.close_value);
        lineGraph.setMarkerView(mv);

        // set upper & lower limit lines
        //LimitLine lowerLimit = new LimitLine(getLowerLimit() - 5f, "Lower limit");
        //setLimitLine(lowerLimit, LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        //LimitLine upperLimit = new LimitLine(getUpperLimit() + 5f, "Upper limit");
        //setLimitLine(upperLimit, LimitLine.LimitLabelPosition.RIGHT_TOP);

        // set scale on YAxis (x-axis shown by default)
        YAxis yAxis = lineGraph.getAxisLeft();
        //setYAxis(yAxis, lowerLimit, upperLimit);
        setYAxis(yAxis);
        lineGraph.getAxisRight().setEnabled(false); // hide axis

        // animate graph
        lineGraph.animateY(1500, Easing.EasingOption.EaseInOutQuart);

        // add data
        addData(lineGraph);

    }

    private void addData(LineChart lineGraph) {
        // reverse StockItem values list (oldest first)
        List<StockValues> reversed = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            reversed.add(mList.get(mList.size() - 1 - i));
        }

        // number of data points to display (one for each date)
        String[] xVals = new String[mList.size()];
        for (int i = 0; i < xVals.length; i++) {
            xVals[i] = reversed.get(i).getDate();
        }

        // dataset of close values
        List<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < reversed.size(); i++) {
            yVals.add(new Entry(Float.parseFloat(reversed.get(i).getClose()), i));
        }

        // config the graph
        LineDataSet dataSet = new LineDataSet(yVals, "Data set");
        dataSet.enableDashedLine(10f, 5f, 0f);
        dataSet.enableDashedHighlightLine(10f, 5f, 0f);
        dataSet.setLineWidth(1f);
        dataSet.setColor(R.color.colorAccent);
        dataSet.setDrawCircles(false);
        //dataSet.setCircleColor(R.color.colorAccent);
        //dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawValues(false); // hide data set values
        //dataSet.setValueTextSize(9f); // set text size of data set values
        dataSet.setDrawFilled(true);

        if(Build.VERSION.SDK_INT >= 18) {
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_accent_color);
            dataSet.setFillDrawable(drawable);
        } else {
            dataSet.setFillColor(R.color.colorAccent);
        }

        // create & add the graph
        List<ILineDataSet> lineDateSet = new ArrayList<>();
        lineDateSet.add(dataSet);
        LineData data = new LineData(xVals, lineDateSet);
        lineGraph.setData(data);
    }

    private void setYAxis(YAxis yAxis, LimitLine lower, LimitLine upper) {
        yAxis.removeAllLimitLines(); // reset
        yAxis.addLimitLine(lower);
        yAxis.addLimitLine(upper);
        yAxis.setAxisMaxValue(getUpperLimit() + setAxisMargin());
        yAxis.setAxisMinValue(getLowerLimit() - setAxisMargin());
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawZeroLine(true);
        yAxis.setDrawLimitLinesBehindData(true);
    }

    private void setYAxis(YAxis yAxis) {
        yAxis.setAxisMaxValue(getUpperLimit() + setAxisMargin());
        yAxis.setAxisMinValue(getLowerLimit() - setAxisMargin());
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawZeroLine(true);
    }

    private float getUpperLimit() {
        float maxCloseDuringTimeframe = 0f;
        if(mList.size() > 0) {
            // assuming the first value is the largest value
            maxCloseDuringTimeframe = Float.parseFloat(mList.get(0).getClose());
            float close;
            for (int i = 1; i < mList.size(); i++) {
                close = Float.parseFloat(mList.get(i).getClose());
                if(close > maxCloseDuringTimeframe) {
                    maxCloseDuringTimeframe = close;
                }
            }
        }
        return maxCloseDuringTimeframe;
    }

    private float getLowerLimit() {
        float minCloseDuringTimeframe = 0f;
        if(mList.size() > 0) {
            minCloseDuringTimeframe = Float.parseFloat(mList.get(0).getClose());
            float close;
            for (int i = 1; i < mList.size(); i++) {
                close = Float.parseFloat(mList.get(i).getClose());
                if(close < minCloseDuringTimeframe) {
                    minCloseDuringTimeframe = close;
                }
            }
        }
        return minCloseDuringTimeframe;
    }

    private float setAxisMargin() {
        float margin;
        float diff = getUpperLimit() - getLowerLimit();
        if(diff > 100f) margin = 20f;
        else if(diff <= 100f && diff > 50f) margin = 10f;
        else if(diff <= 50f && diff > 20f) margin = 5f;
        else if(diff <= 20f && diff < 10f) margin = 2f;
        else margin = 1f;

        return margin;
    }

    private void setLimitLine(LimitLine ll, LimitLine.LimitLabelPosition position) {
        ll.setLineWidth(4f);
        ll .enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(position);
        ll.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        ll.setTextSize(10f);
    }

    private void setupTimeFrameButtons(View view) {
        Button oneMonth = (Button) view.findViewById(R.id.time_frame_one_month);
        Button threeMonths = (Button) view.findViewById(R.id.time_frame_three_months);
        Button sixMonths = (Button) view.findViewById(R.id.time_frame_six_months);
        Button twelveMonths = (Button) view.findViewById(R.id.time_frame_twelve_months);

        oneMonth.setOnClickListener(this);
        threeMonths.setOnClickListener(this);
        sixMonths.setOnClickListener(this);
        twelveMonths.setOnClickListener(this);
    }

    private void executeQuery(String symbol, String timeFrame) {
        new GetHistoricalDataThread(symbol, timeFrame).start();
    }

    public void onEventMainThread(AppMessageEvent event) {
        if(event.getMessage().equals(Constants.HISTORICAL_DATA_CACHE_UPDATED)) {
            mList.clear();
            mList.addAll(HistoricalDataCache.getHistoricalDataCache().getHistoricalValues());
            if(mList.size() > 0)
                displayChart(mLineGraph);
        }
    }


}
