package com.udacity.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.stockhawk.R;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

public class DetailActivity extends AppCompatActivity {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.detail_stock_name)
    TextView tvStockName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String symbol = intent.getStringExtra("symbol");
        new StockDetailsAsyncTask().execute(symbol);

        Timber.d("Detail activity for stock: %s created", symbol);
    }
        /*
        Stock stock = null;
        List<HistoricalQuote> stockHistQuotes = null;
        try {
            stock = YahooFinance.get(symbol);
            stockHistQuotes = stock.getHistory();
        } catch (IOException e) {
            Timber.d(e.toString());
        }

        tvStockName.setText(symbol);
        Timber.d(stockHistQuotes.toString());
        */

    private class StockDetailsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            Stock stock = null;
            List<HistoricalQuote> stockHistQuotes = null;
            try {
                stock = YahooFinance.get(strings[0]);
                stockHistQuotes = stock.getHistory();
            } catch (IOException e) {
                Timber.d(e.toString());
            }

            return stockHistQuotes.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvStockName.setText(s);
        }
    }

}
