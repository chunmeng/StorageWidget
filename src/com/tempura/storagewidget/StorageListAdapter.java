package com.tempura.storagewidget;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.RemoteViewsService;
import android.widget.Toast;

public class StorageListAdapter implements RemoteViewsService.RemoteViewsFactory {

    private final String TAG = "StorageWidget:Adapter";
    
    private Context mContext;
    private int mAppWidgetId;
    private Cursor mCursor; //< A cursor is a resultset of a database query
    
    public StorageListAdapter(Context context, Intent intent) {
        this.mContext = context;     
        this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    
    @Override
    public int getCount() {
        if (mCursor == null)
        	return 0;
        return mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }    
        
    // This is a object holder for layout defined in simple_data.xml
    static class SimpleViewHolder
    {
        ProgressBar progressBar;
        TextView txtName;       
        TextView percentage;
    }

    @Override
    public RemoteViews getLoadingView() {
        // Return a simple text view while loading
        final RemoteViews rv = new RemoteViews(this.mContext.getPackageName(), R.layout.simple_data);
        rv.setProgressBar(R.id.simple_progress, (int)100, 0, true); // show in progress
        rv.setTextViewText(R.id.simple_percentage, this.mContext.getResources().getString(R.string.loading_message));
    	return rv;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Log.d(TAG, "getViewAt " + position + " - " + this.mAppWidgetId);
        
        RemoteViews rv = new RemoteViews(this.mContext.getPackageName(), R.layout.simple_data_image);
        try {
        	// Get the raw data from cursor table
        	String name = "Unknown";
        	String path = "/unknown";
        	long size = 0L;
        	long free = 0L;
        	
            if (mCursor.moveToPosition(position)) {
                final int nameColIndex = mCursor.getColumnIndex(StorageDataProvider.Columns.NAME);
                final int pathColIndex = mCursor.getColumnIndex(StorageDataProvider.Columns.PATH);
                final int sizeColIndex = mCursor.getColumnIndex(StorageDataProvider.Columns.SIZE);
                final int freeColIndex = mCursor.getColumnIndex(StorageDataProvider.Columns.FREE);
                
                name = mCursor.getString(nameColIndex);
                path = mCursor.getString(pathColIndex);
                size = mCursor.getLong(sizeColIndex);
                free = mCursor.getLong(freeColIndex);
            }
                        
            double d = 0.0D;                   
            if (size != 0L)
                d = 100.0D * (double)free / (double)size;
            
            DecimalFormat df = new DecimalFormat("#.#"); 
            String text = "Free " + df.format(d) + "% " 
                            + "(" + StorageNode.formatSize(free) 
                            + "/" + StorageNode.formatSize(size) + ")";
                        
            rv.setTextViewText(R.id.simple_text_name, name);
            rv.setTextViewText(R.id.simple_text_path, path);
            rv.setProgressBar(R.id.simple_progress, (int)100, (int)(100.0D - d), false);
            rv.setTextViewText(R.id.simple_percentage, text); 
            Integer srcId = -1;
            if (name.contains("External")) {
                srcId = StorageNode.iconMap.get("External");
            } else {
                srcId = StorageNode.iconMap.get(name);
            }                
            
            if (srcId != null && srcId != -1) {                
                rv.setImageViewResource(R.id.storage_icon, srcId.intValue());
            }
            
        } catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        return rv;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        // Since we reload the cursor in onDataSetChanged() which gets called immediately after
        // onCreate(), we do nothing here.        
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged - " + this.mAppWidgetId);         
        // Refresh the cursor
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(StorageDataProvider.CONTENT_URI, null, null,
                null, null);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }        
    }

	@Override
	public int getViewTypeCount() { 
		return 1;
	}

	@Override
	public boolean hasStableIds() { 
		return true;
	}   
}