package com.example.android.stockshawk.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.stockshawk.R;
import com.example.android.stockshawk.StocksHawkApp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class PrefUtils {

    private PrefUtils() {
    }

    public static Set<String> getStocks(Context context) {
        Context applicationContext = StocksHawkApp.getContextOfApplication();
        String prefsName = context.getString(R.string.app_name);
        String stocksKey = context.getString(R.string.pref_stocks_key);
        String initializedKey = context.getString(R.string.pref_stocks_initialized_key);
        String[] defaultStocksList = context.getResources().getStringArray(R.array.default_stocks);

        HashSet<String> defaultStocks = new HashSet<>(Arrays.asList(defaultStocksList));
        SharedPreferences prefs = applicationContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        boolean initialized = prefs.getBoolean(initializedKey, false);

        if (!initialized) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.putBoolean(initializedKey, true);
            editor.putStringSet(stocksKey, defaultStocks);
            editor.apply();
            return defaultStocks;
        }
        return prefs.getStringSet(stocksKey, new HashSet<String>());
    }

    private static void editStockPref(Context context, String symbol, Boolean add) {
        Context applicationContext = StocksHawkApp.getContextOfApplication();
        String initializedKey = context.getString(R.string.pref_stocks_initialized_key);
        String prefsName = context.getString(R.string.app_name);
        String key = context.getString(R.string.pref_stocks_key);
        String stocksKey = context.getString(R.string.pref_stocks_key);

        Set<String> stocks = getStocks(context);


        if (add) {
            stocks.add(symbol);
        } else {
            stocks.remove(symbol);
        }

        SharedPreferences prefs = applicationContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putBoolean(initializedKey, true);
        editor.putStringSet(key, stocks);
        editor.apply();
    }

    public static void addStock(Context context, String symbol) {
        editStockPref(context, symbol, true);
    }

    public static void removeStock(Context context, String symbol) {
        editStockPref(context, symbol, false);
    }

    public static String getDisplayMode(Context context) {
        String prefsName = context.getString(R.string.app_name);
        Context applicationContext = StocksHawkApp.getContextOfApplication();
        String key = context.getString(R.string.pref_display_mode_key);
        String defaultValue = context.getString(R.string.pref_display_mode_default);
        SharedPreferences prefs = applicationContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        return prefs.getString(key, defaultValue);
    }

    public static void toggleDisplayMode(Context context) {
        String prefsName = context.getString(R.string.app_name);
        Context applicationContext = StocksHawkApp.getContextOfApplication();
        String key = context.getString(R.string.pref_display_mode_key);
        String absoluteKey = context.getString(R.string.pref_display_mode_absolute_key);
        String percentageKey = context.getString(R.string.pref_display_mode_percentage_key);

        SharedPreferences prefs = applicationContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE);

        String displayMode = getDisplayMode(context);

        SharedPreferences.Editor editor = prefs.edit();

        if (displayMode.equals(absoluteKey)) {
            editor.putString(key, percentageKey);
        } else {
            editor.putString(key, absoluteKey);
        }

        editor.apply();
    }

}
