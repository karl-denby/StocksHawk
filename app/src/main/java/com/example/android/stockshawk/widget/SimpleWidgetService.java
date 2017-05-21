package com.example.android.stockshawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.stockshawk.R;
import com.example.android.stockshawk.data.Contract;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.stockshawk.R.id.change;
import static com.example.android.stockshawk.R.id.price;
import static com.example.android.stockshawk.R.id.symbol;


public class SimpleWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(getApplicationContext(), intent);
    }
}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Intent mIntent;
    private int mAppWidgetId;

    private List<String> mRealSymbol = new ArrayList<>();
    private List<String> mRealPrice = new ArrayList<>();
    private List<String> mRealChange = new ArrayList<>();

    ListViewRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
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
        Cursor cursor;

        // RemoteView does not have permission to read the URI, clear out the calling identity 1st
        final long token = Binder.clearCallingIdentity();
        try {

            Uri selection = Contract.Quote.URI;
            String[] projection = {"*"};
            String sortOrder = Contract.Quote.COLUMN_SYMBOL;

            cursor = mContext.getContentResolver().query(
                    selection,
                    projection,
                    null,
                    null,
                    sortOrder
            );

            mRealSymbol = new ArrayList<>();
            mRealPrice = new ArrayList<>();
            mRealChange = new ArrayList<>();

            cursor.moveToFirst();
            int symbol = cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
            int price = cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE);
            int change = cursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE);

            mRealSymbol.add(cursor.getString(symbol));
            mRealPrice.add(cursor.getString(price));
            mRealChange.add(cursor.getString(change));

            while (cursor.moveToNext()) {
                symbol = cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
                price = cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE);
                change = cursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE);

                mRealSymbol.add(cursor.getString(symbol));
                mRealPrice.add(cursor.getString(price));
                mRealChange.add(cursor.getString(change));
            }

        } finally {
            Binder.restoreCallingIdentity(token);
        }
        cursor.close();
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
        remoteViews.setTextViewText(symbol, mRealSymbol.get(position));
        remoteViews.setTextViewText(price, mRealPrice.get(position));
        remoteViews.setTextViewText(change, mRealChange.get(position));

        // that is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        extras.putInt(SimpleWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        remoteViews.setOnClickFillInIntent(R.id.widget_list_view, fillInIntent);

        return remoteViews;
    }

}