package com.example.android.stockshawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
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

    Context mContext;
    Intent mIntent;
    Cursor mCursor;
    List mFakeData = new ArrayList();

    ListViewRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
    }

    @Override
    public void onCreate() {
        // Setup connections to data/cursors
        // nothing heavy as we have 20 seconds before an ANR gets shown

        mFakeData.add("TEST");
        mFakeData.add("GOOG");
        mFakeData.add("TSLA");
        mFakeData.add("YHOO");

        Uri selection = Contract.Quote.URI;
        String[] projection = {Contract.Quote.COLUMN_SYMBOL};
        String selectionClause = null;
        String[] selectionArgs = {""};

        /*
        mCursor = mContext.getContentResolver().query(
                selection,
                projection,
                selectionClause,
                selectionArgs,
                "_ID ASC"
        );

        int symbol = mCursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
        Log.v("DEBUG: ", "Do we have a cursor?");
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                mFakeData.add(mCursor.getString(symbol));
                Log.v("DEBUG: ", "Symbol is " + mCursor.getString(symbol));
            }
        }
        */
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void onDataSetChanged() {
        // Do heavy stuff here
    }

    @Override
    public int getCount() {
        return mFakeData.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return false;
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
        remoteViews.setTextViewText(R.id.symbol, mFakeData.get(position).toString());
        remoteViews.setTextViewText(R.id.price, "0.0");
        remoteViews.setTextViewText(R.id.change, "0.0");
        return remoteViews;
    }

}