package com.tempura.storagewidget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BetterStorageWidget extends AppWidgetProvider {
    public static String ACTION_WIDGET_CLICKED = "com.tempura.storagewidget.CLICK";
    public static String ACTION_WIDGET_REFRESH = "com.tempura.storagewidget.REFRESH";

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void updateAppWidget(Context context, AppWidgetManager paramAppWidgetManager, int widgetId) {
        // Set the list as view
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.main_list);

        try {
            Intent intent = new Intent(context, StorageListRemoteService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                rv.setRemoteAdapter(R.id.storage_list, intent);
            }
            else
                rv.setRemoteAdapter(widgetId, R.id.storage_list, intent);

            // DEBUG
            // rv.setTextViewText(R.id.textViewId, String.valueOf(widgetId));
            
            // This section makes it possible for items to have individualized behavior.
            // It does this by setting up a pending intent template. Individuals items of a collection
            // cannot set up their own pending intents. Instead, the collection as a whole sets
            // up a pending intent template, and the individual items set a fillInIntent
            // to create unique behavior on an item-by-item basis.
            Intent clickIntent = new Intent(context, BetterStorageWidget.class);
            // Set the action for the intent.
            // When the user touches a particular view, it will have the effect of
            // broadcasting ACTION_WIDGET_CLICKED.
            clickIntent.setAction(BetterStorageWidget.ACTION_WIDGET_CLICKED);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent onClickIntent = PendingIntent.getBroadcast(context, widgetId, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.storage_list, onClickIntent);    
            
            // Bind the click intent for the refresh button on the widget
            final Intent refreshIntent = new Intent(context, BetterStorageWidget.class);
            refreshIntent.setAction(BetterStorageWidget.ACTION_WIDGET_REFRESH);
            refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, widgetId,
                    refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.refresh, refreshPendingIntent);            
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        paramAppWidgetManager.updateAppWidget(widgetId, rv);        
        return;
    }

    public void onEnabled(Context paramContext) {
        super.onEnabled(paramContext);
    }

    public void onReceive(Context context, Intent intent) {        
        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        int[] arrayOfId = mgr.getAppWidgetIds(new ComponentName(context.getPackageName(), BetterStorageWidget.class.getName()));
        final String action = intent.getAction();
        // TODO: Handle click only at the moment
        if (action.equals(BetterStorageWidget.ACTION_WIDGET_CLICKED))
        {
            // onUpdate(paramContext, localAppWidgetManager, arrayOfId);
            Toast.makeText(context, "Storage widget clicked...", Toast.LENGTH_SHORT).show();
        }
        else if (action.equals(BetterStorageWidget.ACTION_WIDGET_REFRESH))
        {            
            // Check refresh from which widget is clicked
            final int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            /** This doesn't work. The StorageListAdapter doesn't go to constructor to update the list
                updateAppWidget(context, mgr, id);
            **/           
            mgr.notifyAppWidgetViewDataChanged(arrayOfId, R.id.storage_list);
            
            Toast.makeText(context, "Refreshing... " + id + "/" + arrayOfId.length, Toast.LENGTH_SHORT).show();
            
            // TODO: Find out how to make a data change notification to StoragerListAdapter instead 
        }
        else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Toast.makeText(context, "Updating storage info...", Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int j = 0; j < appWidgetIds.length; ++j)
        {
            updateAppWidget(context, appWidgetManager, appWidgetIds[j]);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}