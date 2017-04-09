package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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

        Timber.d("Detail activity for stock: %s created", symbol);

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
    }

}
