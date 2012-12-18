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
        private AppWidgetManager mMgr;
        private ComponentName mComp;
        
        public MyTimerTask(AppWidgetManager mgr, ComponentName comp) {
        	super();
        	this.mMgr = mgr;        
        	this.mComp = comp;
        }
    	
    	@Override
    	public void run()    
        {            
    		if (this.mMgr != null) {
                int[] arrayOfId = mMgr.getAppWidgetIds(this.mComp);
                mMgr.notifyAppWidgetViewDataChanged(arrayOfId, R.id.storage_list);    			
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
            
            // Trigger widget update - which should just rescan mounts
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            int[] arrayOfId = mgr.getAppWidgetIds(new ComponentName(context.getPackageName(), BetterStorageWidgetProvider.class.getName()));
            mgr.notifyAppWidgetViewDataChanged(arrayOfId, R.id.storage_list);
            
        } else if (action.equals(Intent.ACTION_MEDIA_REMOVED)) {
            Toast.makeText(context, intent.getDataString().replace("file://", "") + " removed...", Toast.LENGTH_SHORT).show();
            
        	// Trigger widget update - which should just rescan mounts
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            int[] arrayOfId = mgr.getAppWidgetIds(new ComponentName(context.getPackageName(), BetterStorageWidgetProvider.class.getName()));            
            mgr.notifyAppWidgetViewDataChanged(arrayOfId, R.id.storage_list);
        } else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
        	Toast.makeText(context, intent.getDataString().replace("file://", "") + " unmounted...", Toast.LENGTH_SHORT).show();
        
        	// TODO: Need to delayed the data changed notify for 1-2s. 
	        // It seems the unmount will take sometime to reflect on the df table, and the external sd still appear. 
	    	// Trigger widget update - which should just rescan mounts
	        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
	        // int[] arrayOfId = mgr.getAppWidgetIds(new ComponentName(context.getPackageName(), BetterStorageWidgetProvider.class.getName()));            
	        // mgr.notifyAppWidgetViewDataChanged(arrayOfId, R.id.storage_list);
	        
	        MyTimerTask task = new MyTimerTask(mgr, new ComponentName(context.getPackageName(), BetterStorageWidgetProvider.class.getName())); //create new task
	        Timer timer = new Timer(); 
	        timer.schedule(task,2*1000L); // start a new timer task in 5seconds (timertask, seconds(long))
        }        
        
    }
}
