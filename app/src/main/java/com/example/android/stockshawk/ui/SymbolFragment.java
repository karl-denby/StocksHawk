package com.example.android.stockshawk.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.stockshawk.R;
import com.example.android.stockshawk.data.Contract;
import com.example.android.stockshawk.data.PrefUtils;
import com.example.android.stockshawk.sync.QuoteSyncJob;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class SymbolFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener,
        StockAdapter.StockAdapterOnClickHandler,
        AddStockDialog.StockAddedRequestListener {

    private static final String TAG = "MasterDetail";
    private static final int STOCK_LOADER = 0;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view)
    RecyclerView stockRecyclerView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.error)
    TextView error;

    OnClickListener mCallback;
    FrameLayout frameLayout;

    private StockAdapter adapter;
    Toast mToast;
    Context mContext;

    @Override
    public void onClick(String symbol, String price, String change, String absolute) {
        Timber.d("Symbol clicked: %s", symbol);

        boolean tablet = getResources().getBoolean(R.bool.isTablet);
        if (tablet) {

            Bundle arguments = new Bundle();
            arguments.putString("symbol", symbol);
            arguments.putString("price", price);
            arguments.putString("change", change);
            arguments.putString("absolute", absolute);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction().replace(R.id.details_frag, fragment).commit();

        } else {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("symbol", symbol);
            intent.putExtra("price", price);
            intent.putExtra("change", change);
            intent.putExtra("absolute", absolute);
            startActivity(intent);
        }
    }

    public void button(@SuppressWarnings("UnusedParameters") View view) {
        AddStockDialog addStockDialog = new AddStockDialog();
        addStockDialog.setTargetFragment(SymbolFragment.this, 300);
        addStockDialog.show(getFragmentManager(), "StockDialogFragment");
    }

    //void addStock(String symbol) {}

    @Override
    public void onAddStockRequest(String symbol) {
        Log.v("DEBUG", "onAddStockRequest Code Called");
        if (symbol != null && !symbol.isEmpty()) {
            if (networkUp()) {
                swipeRefreshLayout.setRefreshing(true);
                new StockCheckAsyncTask().execute(symbol);
            } else {
                String message = getString(R.string.toast_stock_added_no_connectivity, symbol);
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface OnClickListener {
        public void OnClick(View view);
    }

    private boolean networkUp() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onRefresh() {

        QuoteSyncJob.syncImmediately(mContext);

        if (!networkUp() && adapter.getItemCount() == 0) {
            swipeRefreshLayout.setRefreshing(false);
            error.setText(getString(R.string.error_no_network));
            error.setVisibility(View.VISIBLE);
        } else if (!networkUp()) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(mContext, R.string.toast_no_connectivity, Toast.LENGTH_LONG).show();
        } else if (PrefUtils.getStocks(mContext).size() == 0) {
            swipeRefreshLayout.setRefreshing(false);
            error.setText(getString(R.string.error_no_stocks));
            error.setVisibility(View.VISIBLE);
        } else {
            error.setVisibility(View.GONE);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();

        return inflater.inflate(R.layout.symbol_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frameLayout = (FrameLayout) getView().findViewById(R.id.symbol_frame);
        if (frameLayout!=null) {
            frameLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    mCallback.OnClick(view);
                }
            });
        } else {
            Log.v("DEBUG", "Didn't find framelayout reference");
        }

        ButterKnife.bind(getActivity());

        adapter = new StockAdapter(mContext, this);

        stockRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        stockRecyclerView.setAdapter(adapter);
        stockRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        error = (TextView) getView().findViewById(R.id.error);
        onRefresh();

        QuoteSyncJob.initialize(getActivity());
        getLoaderManager().initLoader(STOCK_LOADER, null,this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String symbol = adapter.getSymbolAtPosition(viewHolder.getAdapterPosition());
                PrefUtils.removeStock(mContext, symbol);
                getActivity().getContentResolver().delete(Contract.Quote.makeUriForStock(symbol), null, null);
            }
        }).attachToRecyclerView(stockRecyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button(v);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        swipeRefreshLayout.setRefreshing(false);

        if (data.getCount() != 0) {
            error.setVisibility(View.GONE);
        }
        adapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setCursor(null);
    }

    private class StockCheckAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... symbol) {

            Stock stock = null;

            try {
                stock = YahooFinance.get(symbol[0], false);
            } catch (IOException e) {
                Log.v("CHECK VALID", "Network Error: Exception during Stock.get(symbol)");
            }

            if (stock.getQuote().getAsk() != null) {
                PrefUtils.addStock(mContext, symbol[0]);
                QuoteSyncJob.syncImmediately(mContext);
                return true;
            } else {
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean valid) {
            super.onPostExecute(valid);
            if (!valid) {
                swipeRefreshLayout.setRefreshing(false);

                if (mToast!=null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(mContext, "Unrecognized Stock", Toast.LENGTH_LONG);
                mToast.show();
            }
        }
    }

}
