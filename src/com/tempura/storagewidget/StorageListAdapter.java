package com.tempura.storagewidget;

import java.util.ArrayList;
import java.util.Collections;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
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

public class StorageListAdapter extends BaseAdapter 
    implements RemoteViewsService.RemoteViewsFactory {

    private final String TAG = "StorageWidget:Adapter";
    private static ArrayList<StorageNode> nodeList; // A list of the available storage     
    
    private Context mContext;
    private int mAppWidgetId;
    
    private int nCount; // Debug refresh count
    
    public StorageListAdapter(Context context, Intent intent) {
        this.mContext = context;     
        this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        // UT: Test sample data list
        // generateTestList();     
        
        generateStorageList();
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return nodeList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return nodeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub    
        return convertView;
    }
    
    private void generateStorageList() {
        nCount++;
        // If the list doesn't exist, create a new one
        if (nodeList == null) {
            nodeList = new ArrayList<StorageNode>();
        }
        
        // If the list is not emptied, clear it
        if (!nodeList.isEmpty()) {
            nodeList.clear();
        }
        
        // Get the local filesystem info
        ArrayList<StorageNode> dfList = SystemCommander.getEnvFS(this.mContext);
        if (!dfList.isEmpty()) {
            Collections.sort(dfList);
            nodeList.addAll(dfList);
        }

        // Get the mounted storage info
        // ArrayList localArrayList2 = CommandsRunner.runCatMounts(this, localArrayList1);
        
        // Get the memory info
        ArrayList memList = SystemCommander.runCatMeminfo(this.mContext);
        if (!memList.isEmpty())
        {
            nodeList.addAll(memList);
        }
        return;        
    }

    //! UT method
    private void generateTestList() {       
        if (nodeList == null) {
            nodeList = new ArrayList<StorageNode>();
        }
        
        if (nodeList.isEmpty()) {
            nodeList.add(new StorageNode("Test Data", 100L, 20L));
            nodeList.add(new StorageNode("Test SDCard", 200L, 100L));
        }
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
    	return new RemoteViews(this.mContext.getPackageName(), R.layout.loading);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt " + position);
        RemoteViews rv = new RemoteViews(this.mContext.getPackageName(), R.layout.simple_data);
        StorageNode node;
        double d;       
        node = (StorageNode)nodeList.get(position);
        if (node.getSize().longValue() != 0L)
            d = 100L * node.getFree().longValue() / node.getSize().longValue();
        else
            d = 100.0D;
        
        String text = /*"[" + this.nCount + "]*/ "Free " + d + "% " 
                        + "(" + node.getFreeDisplay() 
                        + "/" + node.getSizeDisplay() + ")";
        
        rv.setTextViewText(R.id.simple_text_name, /*"[" + this.mAppWidgetId + "]: " + */ node.getName() + " (" + node.getPath() + ")");
        rv.setProgressBar(R.id.simple_progress, (int)100, (int)(100.0D - d), false);
        rv.setTextViewText(R.id.simple_percentage, text);
        
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
        // TODO Auto-generated method stub
        Log.d(TAG, "onDataSetChanged");       
        generateStorageList();        
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        
    }   
}