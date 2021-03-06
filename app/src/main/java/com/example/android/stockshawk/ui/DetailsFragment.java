package com.example.android.stockshawk.ui;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.stockshawk.R;
import com.example.android.stockshawk.mock.MockUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

public class DetailsFragment extends Fragment {

    String symbol;
    String price;
    String change;
    String absolute;

    TextView tvStockName;
    TextView tvStockPrice;
    TextView tvStockChange;
    TextView tvStockPercent;
    ProgressBar pbLoadingIndicator;
    LineChart grpHistorical;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle arguments = getArguments();
        if (arguments!=null) {
            symbol = arguments.getString("symbol");
            price = arguments.getString("price");
            change = arguments.getString("change");
            absolute = arguments.getString("absolute");
        }
        return inflater.inflate(R.layout.detail_panel, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.pb_loading_indicator).setVisibility(View.GONE);
        LineChart grp = (LineChart) view.findViewById(R.id.grp_historical_data);
        grp.setNoDataText("");
    }

    private void updateUI() {
        tvStockName = (TextView) getView().findViewById(R.id.tv_detail_stock_name);
        tvStockPrice = (TextView) getView().findViewById(R.id.tv_detail_price);
        tvStockChange = (TextView) getView().findViewById(R.id.tv_detail_change);
        tvStockPercent = (TextView) getView().findViewById(R.id.tv_detail_percent);
        pbLoadingIndicator = (ProgressBar) getView().findViewById(R.id.pb_loading_indicator);
        grpHistorical = (LineChart) getView().findViewById(R.id.grp_historical_data);
        grpHistorical.setNoDataText("");

        tvStockName.setText(symbol);
        tvStockPrice.setText(price);
        tvStockChange.setText(change);
        tvStockPercent.setText(absolute);
        pbLoadingIndicator.setVisibility(View.GONE);

        if (symbol!=null) {

            new StockDetailsAsyncTask().execute(symbol);
        }

    }

    private class StockDetailsAsyncTask extends AsyncTask<String, Void, List<Entry>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

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
                //stock = YahooFinance.get(strings[0], true);
                //stockHistQuotes = stock.getHistory(from, to, Interval.DAILY);

                // Note for reviewer:
                // Due to the problems with the Yahoo API we have commented the lines above
                // and included this one to fetch the history from MockUtils
                // This should be enough as to develop and review while the API is down
                stockHistQuotes = MockUtils.getHistory();

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

            if (data!=null) {
                LineDataSet lineDataSet = new LineDataSet(data, "price");
                lineDataSet.setColors(new int[]{R.color.material_blue_500}, getContext().getApplicationContext());
                LineData lineData = new LineData(lineDataSet);
                grpHistorical.setData(lineData);
                grpHistorical.invalidate();  // refresh
            }
        }
    } // StockDetailsAsyncTask
    
}
