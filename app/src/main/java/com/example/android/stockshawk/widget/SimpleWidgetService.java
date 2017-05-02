package com.example.android.stockshawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.stockshawk.R;
import com.example.android.stockshawk.data.Contract;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.stockshawk.R.id.price;


public class SimpleWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(getApplicationContext(), intent);
    }
}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Intent mIntent;
    private Cursor mCursor;
    private List mFakeData = new ArrayList();
    private List mRealSymbol = new ArrayList();
    private List mRealPrice = new ArrayList();
    private List mRealChange = new ArrayList();

    ListViewRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
    }

    @Override
    public void onCreate() {
        // Setup connections to data/cursors
        // nothing heavy as we have 20 seconds before an ANR gets shown

        mFakeData.add("TST1");
        mFakeData.add("TST2");
        mFakeData.add("TST3");
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void onDataSetChanged() {
        // Do heavy stuff here

        // RemoteView does not have permission to read the URI, so clear out the callin identiy 1st
        final long token = Binder.clearCallingIdentity();
        try {

            Log.v("DEBUG: ", "onDataSetChanged");
            Uri selection = Contract.Quote.URI;
            String[] projection = {"*"};

            mCursor = mContext.getContentResolver().query(
                    selection,
                    projection,
                    null,
                    null,
                    null
            );

            mRealSymbol = new ArrayList();
            mRealPrice = new ArrayList();
            mRealChange = new ArrayList();

            int symbol = mCursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
            int price = mCursor.getColumnIndex(Contract.Quote.COLUMN_PRICE);
            int change = mCursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE);

            Log.v("DEBUG: ", "Do we have a cursor?");
            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    mRealSymbol.add(mCursor.getString(symbol));
                    mRealPrice.add(mCursor.getString(price));
                    mRealChange.add(mCursor.getString(change));
                    Log.v("DEBUG: ", "Symbol is " + mCursor.getString(symbol));
                }
            }


        } finally {
            Binder.restoreCallingIdentity(token);
        }


    }

    @Override
    public int getCount() {
        return mRealSymbol.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDestroy() {}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);
        remoteViews.setTextViewText(R.id.symbol, mRealSymbol.get(position).toString());
        remoteViews.setTextViewText(R.id.price, mRealPrice.get(position).toString());
        remoteViews.setTextViewText(R.id.change, mRealChange.get(position).toString());
        return remoteViews;
    }

}