package com.tempura.storagewidget;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.RemoteViewsService;

public class StorageListAdapter extends BaseAdapter 
    implements RemoteViewsService.RemoteViewsFactory {

    private static ArrayList<StorageNode> nodeList; // A list of the available storage 
    
    private Context mContext;
    
    public StorageListAdapter(Context paramContext) {
        this.mContext = paramContext; 
        
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
    
    void generateStorageList() {
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

    void generateTestList() {       
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
        // TODO Auto-generated method stub
        RemoteViews rv = new RemoteViews(this.mContext.getPackageName(), R.layout.simple_data);
        StorageNode localDataItem;
        double d;       
        localDataItem = (StorageNode)nodeList.get(position);
        if (localDataItem.getSize().longValue() != 0L)
            d = 100L * localDataItem.getFree().longValue() / localDataItem.getSize().longValue();
        else
            d = 100.0D;
        
        String text = d + "Free " + d + "% " 
                        + "(" + localDataItem.getFree().longValue() 
                        + "/" + localDataItem.getSize().longValue() + ")";
        
        rv.setTextViewText(R.id.simple_text_name, localDataItem.getName() + "(" + localDataItem.getPath() + ")");
        rv.setProgressBar(R.id.simple_progress, (int)100, (int)(100.0D - d), false);
        rv.setTextViewText(R.id.simple_percentage, text);
        
        return rv;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDataSetChanged() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        
    }   
}