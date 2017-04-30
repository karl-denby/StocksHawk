package com.example.android.stockshawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.stockshawk.R;
import com.example.android.stockshawk.ui.PhoneActivity;


public class SimpleWidgetIntentService extends IntentService {

    public SimpleWidgetIntentService() {
        super("SimpleWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, SimpleWidgetProvider.class));
        int randomNumber = 123;

        for (int appWidgetId: appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_simple);
            views.setEmptyView(R.id.widget_list_view, R.id.no_data);

            Intent launchIntent = new Intent(this, PhoneActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            Intent listViewLoader = new Intent(this, ListViewLoader.class);
            listViewLoader.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            listViewLoader.putExtra("random", randomNumber);
            randomNumber++;

            Log.v("DEBUG", "pre- set remote adapter");
            views.setRemoteAdapter(appWidgetId, listViewLoader );
            Log.v("DEBUG", "done set remote adapter");
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}
