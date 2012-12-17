package com.tempura.storagewidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class StorageEventReceiver extends BroadcastReceiver {
    private final String TAG = "StorageWidget:Receiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
            
            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            //Toast.makeText(context, intent.getDataString().replace("file://", "") + " mounted...\nHandling to be added.", Toast.LENGTH_SHORT).show();
            
/*            // Should not be static?
            StorageListAdapter.addExternalNode(intent.getDataString().replace("file://", ""));
            // Trigger widget update?
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            int[] arrayOfId = mgr.getAppWidgetIds(new ComponentName(context.getPackageName(), BetterStorageWidgetProvider.class.getName()));
            mgr.notifyAppWidgetViewDataChanged(arrayOfId, R.id.storage_list);*/
            
        } else if (action.equals(Intent.ACTION_MEDIA_REMOVED) 
                    || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
            //Toast.makeText(context, intent.getDataString().replace("file://", "") + " removed...\nHandling to be added.", Toast.LENGTH_SHORT).show();
            // Should not be static?
            /*StorageListAdapter.removeExternalNode(intent.getDataString().replace("file://", ""));
            // Trigger widget update?
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            int[] arrayOfId = mgr.getAppWidgetIds(new ComponentName(context.getPackageName(), BetterStorageWidgetProvider.class.getName()));            
            mgr.notifyAppWidgetViewDataChanged(arrayOfId, R.id.storage_list);*/
        }
        
    }
}
