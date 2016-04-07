package com.example.marketdatatracker.ui.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.marketdatatracker.event.AppMessageEvent;
import com.example.marketdatatracker.model.StockItem;
import com.example.marketdatatracker.model.data.StockDataCache;
import com.example.marketdatatracker.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class DeleteStockItemsDialog extends DialogFragment {

    public DeleteStockItemsDialog() {}

    public static DeleteStockItemsDialog newInstance(List<String> symbols) {
        DeleteStockItemsDialog fragment = new DeleteStockItemsDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(Constants.STOCK_SYMBOL_ARRAY_LIST, (ArrayList<String>) symbols);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final List<String> symbols = getArguments().getStringArrayList(Constants.STOCK_SYMBOL_ARRAY_LIST);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Remove stocks from your portfolio?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new AppMessageEvent(Constants.ACTION_CANCELLED));
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // iterate through the list, delete the items
                        List<StockItem> portfolio = StockDataCache.getStockDataCache().getStocks();
                        if (symbols != null) {
                            PortfolioLoop:
                            for (int i = 0; i < symbols.size(); i++) {
                                for (int j = 0; j < portfolio.size(); j++) {
                                    if (portfolio.get(j).getSymbol().equals(symbols.get(i))) {
                                        portfolio.remove(j);
                                        continue PortfolioLoop;
                                    }
                                }
                            }
                            // update the data cache
                            Timber.i("Portfolio: %s", portfolio);
                            StockDataCache.getStockDataCache().setStocks(new ArrayList<>(portfolio));

                            // update default prefs
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            Set<String> savedPortfolio = prefs.getStringSet(Constants.PREFS_STOCK_PORTFOLIO_SET, null);
                            List<String> temp = new ArrayList<>();
                            if (savedPortfolio != null) {
                                // iterate through the set, remove matching symbols, update prefs
                                temp.addAll(savedPortfolio);
                                PrefsLoop:
                                for (int i = 0; i < symbols.size(); i++) {
                                    for (int j = 0; j < temp.size(); j++) {
                                        if (temp.get(j).equals(symbols.get(i))) {
                                            temp.remove(j);
                                            continue PrefsLoop;
                                        }
                                    }
                                }
                                // save the updated symbol set to shared preferences
                                Set<String> updatedPortfolio = new TreeSet<>(temp);
                                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                                        .clear()
                                        .putStringSet(Constants.PREFS_STOCK_PORTFOLIO_SET, updatedPortfolio)
                                        .apply();

                            }
                            EventBus.getDefault().post(new AppMessageEvent(Constants.CONFIRM_STOCK_ITEM_DELETION));
                        }
                    }
                })
                .create();
    }
}
