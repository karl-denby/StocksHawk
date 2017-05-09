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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.util.ArrayList;
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
    LineChart grpHistorical;

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

    private class StockDetailsAsyncTask extends AsyncTask<String, Void, List<Entry>> {

        @Override
        protected List<Entry> doInBackground(String... strings) {

            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();

            from.add(Calendar.MONTH, -1); // from 1 month

            Stock stock;
            List<HistoricalQuote> stockHistQuotes = null;
            List<Entry> lineChartEntries = new ArrayList<>();
            int counter = 0;

            try {
                stock = YahooFinance.get(strings[0], true);
                stockHistQuotes = stock.getHistory(from, to, Interval.DAILY);
                for (HistoricalQuote quote : stockHistQuotes) {
                    Log.v("Quote: ", "close > " + quote.getClose().toString());
                    lineChartEntries.add(new Entry(counter, quote.getClose().floatValue()));
                    counter++;
                }
            } catch (IOException e) {
                Timber.d(e.toString());
            }
            return lineChartEntries;
        }

        @Override
        protected void onPostExecute(List<Entry> data) {

            tvStockName.setVisibility(View.VISIBLE);
            pbLoadingIndicator.setVisibility(View.GONE);

            LineDataSet lineDataSet = new LineDataSet(data, "price");
            lineDataSet.setColors(new int[] {R.color.material_blue_500}, getApplicationContext());
            LineData lineData = new LineData(lineDataSet);
            grpHistorical.setData(lineData);
            grpHistorical.invalidate();  // refresh
        }
    } // StockDetailsAsyncTask

}
