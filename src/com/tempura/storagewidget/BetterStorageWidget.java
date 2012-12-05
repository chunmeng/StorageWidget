package com.tempura.storagewidget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BetterStorageWidget extends AppWidgetProvider {
    public static String ACTION_WIDGET_CLICKED = "ActionWidgetClicked";

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void updateAppWidget(Context paramContext, AppWidgetManager paramAppWidgetManager, int widgetId) {
        // Set the list as view
        RemoteViews widgetViews = new RemoteViews(paramContext.getPackageName(), R.layout.main_list);

        try {
            Intent intent = new Intent(paramContext, StorageListRemoteService.class);
            intent.putExtra("appWidgetId", widgetId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                widgetViews.setRemoteAdapter(R.id.storage_list, intent);
            }
            else
                widgetViews.setRemoteAdapter(widgetId, R.id.storage_list, intent);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        paramAppWidgetManager.updateAppWidget(widgetId, widgetViews);
        return;
    }

    public void onEnabled(Context paramContext) {
        super.onEnabled(paramContext);
    }

    public void onReceive(Context paramContext, Intent paramIntent) {
        super.onReceive(paramContext, paramIntent);
        AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(paramContext);
        int[] arrayOfId = localAppWidgetManager.getAppWidgetIds(new ComponentName(paramContext.getPackageName(), BetterStorageWidget.class.getName()));
        // TODO: Handle click only at the moment
        if (paramIntent.getAction().equals(ACTION_WIDGET_CLICKED))
        {
            onUpdate(paramContext, localAppWidgetManager, arrayOfId);
            Toast.makeText(paramContext, "Updating storage info...", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdate(Context paramContext, AppWidgetManager paramAppWidgetManager, int[] paramArrayOfInt) {
        int[] arrayOfId = paramAppWidgetManager.getAppWidgetIds(new ComponentName(paramContext, BetterStorageWidget.class));
        for (int j = 0; j < arrayOfId.length; j++)
        {
            updateAppWidget(paramContext, paramAppWidgetManager, arrayOfId[j]);
        }
    }
}