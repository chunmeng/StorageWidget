package com.tempura.storagewidget;

import java.util.Timer;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class StorageEventReceiver extends BroadcastReceiver {
    private final String TAG = "StorageWidget:Receiver";  
    
    class MyTimerTask extends TimerTask {
        private Context mContext;
        
        public MyTimerTask(Context context) {
        	super();
        	this.mContext = context;        
        }
    	
    	@Override
    	public void run()    
        {            
    		if (this.mContext != null) {
                Intent refreshIntent = new Intent(mContext, BetterStorageWidgetProvider.class);
                refreshIntent.setAction(BetterStorageWidgetProvider.ACTION_WIDGET_REFRESH);
                mContext.sendBroadcast(refreshIntent);  			
    		}   			
        }
    }    
    
    @Override
    public void onReceive(Context context, Intent intent) {
        
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
            
            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            Toast.makeText(context, intent.getDataString().replace("file://", "") + " mounted...", Toast.LENGTH_SHORT).show();
            
            // NOTE 2012-12-19 This method doesn't work anymore because the refresh is removed from query 
            /*
            // Trigger widget update - which should just rescan mounts
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            int[] arrayOfId = mgr.getAppWidgetIds(new ComponentName(context.getPackageName(), BetterStorageWidgetProvider.class.getName()));
            mgr.notifyAppWidgetViewDataChanged(arrayOfId, R.id.storage_list);
            */
            
            Intent refreshIntent = new Intent(context, BetterStorageWidgetProvider.class);
            refreshIntent.setAction(BetterStorageWidgetProvider.ACTION_WIDGET_REFRESH);
            context.sendBroadcast(refreshIntent);
            
        } else if (action.equals(Intent.ACTION_MEDIA_REMOVED)) {
            Toast.makeText(context, intent.getDataString().replace("file://", "") + " removed...", Toast.LENGTH_SHORT).show();
            
            Intent refreshIntent = new Intent(context, BetterStorageWidgetProvider.class);
            refreshIntent.setAction(BetterStorageWidgetProvider.ACTION_WIDGET_REFRESH);
            context.sendBroadcast(refreshIntent);
            
        } else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
        	Toast.makeText(context, intent.getDataString().replace("file://", "") + " unmounted...", Toast.LENGTH_SHORT).show();
                	
	        // It seems the unmount will take sometime to reflect on the df table, and the external sd still appear. 
	    	// Trigger widget update - which should just rescan mounts	        
	        MyTimerTask task = new MyTimerTask(context); //create new task
	        Timer timer = new Timer(); 
	        timer.schedule(task, 2*1000L); // start a new timer task in 5seconds (timertask, seconds(long))
	        
        }        
        
    }
}
