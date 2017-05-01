package com.example.android.stockshawk.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.stockshawk.R;


public class SimpleWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(getApplicationContext(), intent);
    }
}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Intent mIntent;

    ListViewRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
    }

    @Override
    public void onCreate() {
        Log.v("DEBUG: ", "Hello from RemoteViewsFactory");
        // Setup connections to data/cursors
        // nothing heavy as we have 20 seconds before an ANR gets shown
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public void onDataSetChanged() {
        // Do heavy stuff here
    }

    @Override
    public int getCount() {
        return 0;
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
    public void onDestroy() {

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);
        remoteViews.setTextViewText(R.id.symbol,"TEST");
        remoteViews.setTextViewText(R.id.price,"0");
        remoteViews.setTextViewText(R.id.change,"0");
        return remoteViews;
    }

}