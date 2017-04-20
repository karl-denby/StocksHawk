package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
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
    @BindView(R.id.tv_detail_stock_name)
    TextView tvStockName;

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
        if (symbol == null) { symbol = "DFLT"; };
        new StockDetailsAsyncTask().execute(symbol);

        Timber.d("Detail activity for stock: %s created", symbol);
        tvStockName.setText(symbol);
    }

    private class StockDetailsAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {

            Stock stock = null;
            List<HistoricalQuote> stockHistQuotes = null;
            try {
                stock = YahooFinance.get(strings[0]);
                stockHistQuotes = stock.getHistory();
            } catch (IOException e) {
                Timber.d(e.toString());
            }

            String[] not_s = { "4.5", "6.7", "3.2", "6.8", "4.7", "4.1", "5.5" };
            return not_s;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);

            tvStockName.setVisibility(View.VISIBLE);
            pbLoadingIndicator.setVisibility(View.GONE);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {});

            double counter = 0;
            for (String price : s) {
                series.appendData(new DataPoint(counter, Double.valueOf(price)), true, s.length - 2);
                counter++;
            }
            grpHistorical.addSeries(series);
        }
    } // StockDetailsAsyncTask
}
