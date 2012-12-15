package com.tempura.storagewidget;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
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
        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)
                || action.equals(Intent.ACTION_MEDIA_REMOVED)) {
            
            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            ArrayList<Integer> appWidgetIds = new ArrayList<Integer>();               

            Toast.makeText(context, intent.getDataString().replace("file://", "") + " mounted...\nHandling to be added.", Toast.LENGTH_SHORT).show();

            // Send a ADD_STORAGE_NODE action to widget
            /*            
            final int N = appWidgetIds.size();
            for (int i=0; i<N; i++) {
                BetterStorageWidget.updateAppWidget(context, gm, appWidgetIds.get(i));            
            }
            */
        } else if (action.equals(Intent.ACTION_MEDIA_REMOVED) 
                    || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
            Toast.makeText(context, intent.getDataString().replace("file://", "") + " removed...\nHandling to be added.", Toast.LENGTH_SHORT).show();
        }
        
    }
}
