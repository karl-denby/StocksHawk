package com.example.android.stockshawk.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.stockshawk.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;


public class DetailActivity extends AppCompatActivity {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_detail_stock_name)
    TextView tvStockName;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_detail_price)
    TextView tvStockPrice;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_detail_change)
    TextView tvStockChange;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_detail_percent)
    TextView tvStockPercent;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pbLoadingIndicator;

    @SuppressWarnings("WeakerAcess")
    @BindView(R.id.grp_historical_data)
    GraphView grpHistorical;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String symbol = intent.getStringExtra("symbol");
        String price = intent.getStringExtra("price");
        String change = intent.getStringExtra("change");
        String absolute = intent.getStringExtra("absolute");

        if (symbol == null) { symbol = "DFLT"; }
        new StockDetailsAsyncTask().execute(symbol);

        Timber.d("Detail activity for stock: %s created", symbol);
        tvStockName.setText(symbol);
        tvStockPrice.setText(price);
        tvStockChange.setText(change);
        tvStockPercent.setText(absolute);
    }

    private class StockDetailsAsyncTask extends AsyncTask<String, Void, List<HistoricalQuote>> {

        @Override
        protected List<HistoricalQuote> doInBackground(String... strings) {

            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();

            from.add(Calendar.MONTH, -1); // from 1 month

            Stock stock;
            List<HistoricalQuote> stockHistQuotes = null;

            try {
                stock = YahooFinance.get(strings[0], true);
                stockHistQuotes = stock.getHistory(from, to, Interval.DAILY);
                for (HistoricalQuote quote : stockHistQuotes) {
                    Log.v("Quote: ", "close > " + quote.getClose().toString());
                }
            } catch (IOException e) {
                Timber.d(e.toString());
            }
            return stockHistQuotes;
        }

        @Override
        protected void onPostExecute(List<HistoricalQuote> s) {

            tvStockName.setVisibility(View.VISIBLE);
            pbLoadingIndicator.setVisibility(View.GONE);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {});

            System.out.println(s.size());

            double counter = 0;

            for (HistoricalQuote price : s) {
                //series.appendData(new DataPoint(price.getDate(), price.getClose(), true, 8);
                counter++;
            }

            grpHistorical.addSeries(series);
        }
    } // StockDetailsAsyncTask

}
