package com.example.android.stockshawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.stockshawk.R;


public class SimpleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Log.v("DEBUG: ", "onUpdate");
        Log.v("DEBUG: ", "Number of widgets " + N);
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch Service that will provide views for this listView
            Intent intent = new Intent(context, SimpleWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            Log.v("DEBUG: ", "Set Data?");

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_simple);
            remoteViews.setRemoteAdapter(R.id.widget_list_view, intent);
            Log.v("DEBUG: ", "SetRemoteAdapter");

            remoteViews.setEmptyView(R.id.widget_list_view, R.id.no_data);
            Log.v("DEBUG: ", "SetEmptyView");

            // Get the layout for the App Widget and attach an on-click listener
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
            Log.v("DEBUG: ", "SetOnClickPendingIntent");

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            Log.v("DEBUG: ", "Update");
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //context.startService(new Intent(context, SimpleWidgetService.class));
        Log.v("DEBUG: ", "onRecieve");
        super.onReceive(context, intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        //context.startService(new Intent(context, SimpleWidgetService.class));
        Log.v("DEBUG: ", "onAppWidgetOptionsChanged");
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

}
