package com.example.android.stockshawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.stockshawk.R;
import com.example.android.stockshawk.data.Contract;

import java.util.ArrayList;
import java.util.List;


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
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void onDataSetChanged() {
        // Do heavy stuff here

        // RemoteView does not have permission to read the URI, clear out the calling identity 1st
        final long token = Binder.clearCallingIdentity();
        try {

            Uri selection = Contract.Quote.URI;
            String[] projection = {"*"};
            String sortOrder = Contract.Quote.COLUMN_SYMBOL;

            mCursor = mContext.getContentResolver().query(
                    selection,
                    projection,
                    null,
                    null,
                    sortOrder
            );

            mRealSymbol = new ArrayList();
            mRealPrice = new ArrayList();
            mRealChange = new ArrayList();

            int symbol = mCursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
            int price = mCursor.getColumnIndex(Contract.Quote.COLUMN_PRICE);
            int change = mCursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE);

            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    mRealSymbol.add(mCursor.getString(symbol));
                    mRealPrice.add(mCursor.getString(price));
                    mRealChange.add(mCursor.getString(change));
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