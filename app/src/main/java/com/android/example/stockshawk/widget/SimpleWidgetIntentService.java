package com.android.example.stockshawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.example.stockshawk.R;
import com.android.example.stockshawk.ui.PhoneActivity;

import static com.android.example.stockshawk.R.id.widget;


public class SimpleWidgetIntentService extends IntentService {

    public SimpleWidgetIntentService() {
        super("SimpleWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Context context = getApplicationContext();
        Log.v("KARL", "Handling Intent");

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, SimpleWidgetProvider.class));


        for (int appWidgetId: appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_simple);
            views.setTextViewText(R.id.widget_text, "passed");
            Log.v("KARL", "KARL" + views.toString());

            Intent launchIntent = new Intent(this, PhoneActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}
