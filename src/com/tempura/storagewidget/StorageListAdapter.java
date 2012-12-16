package com.tempura.storagewidget;

import java.text.DecimalFormat;
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
    private static ArrayList<String> extNodePathList; 
    
    private Context mContext;
    private int mAppWidgetId;
    
    private int nCount; // Debug refresh count
    
    public StorageListAdapter(Context context, Intent intent) {
        this.mContext = context;     
        this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        
        if (extNodePathList == null) {
            extNodePathList = new ArrayList<String>();
        }        
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

        synchronized (nodeList) {    
            // If the list is not emptied, clear it
            if (!nodeList.isEmpty()) {
                nodeList.clear();
            }
            
            // Get the local filesystem info
            ArrayList<StorageNode> dfList = SystemCommander.getEnvFS(this.mContext);
            if (!dfList.isEmpty()) {
                //Collections.sort(dfList);
                nodeList.addAll(dfList);
            }
            
            // Get the mounted storage info
            // ArrayList localArrayList2 = CommandsRunner.runCatMounts(this, localArrayList1);
            if (!extNodePathList.isEmpty()) {
                for (int i = 0; i < extNodePathList.size(); ++i) {
                    nodeList.add(SystemCommander.getStorageNode(extNodePathList.get(i)));
                }
            }
            
            // Get the memory info
            ArrayList memList = SystemCommander.runCatMeminfo(this.mContext);
            if (!memList.isEmpty())
            {
                nodeList.addAll(memList);
            }           
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
    
    public static void addExternalNode(String path) {
        if (extNodePathList == null)
            return;
        
        if (!extNodePathList.contains(path)) {
            extNodePathList.add(path);
        }
    }

    public static void removeExternalNode(String path) {
        if (extNodePathList == null)
            return;

        if (extNodePathList.contains(path)) {
            extNodePathList.remove(path);
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
        final RemoteViews rv = new RemoteViews(this.mContext.getPackageName(), R.layout.simple_data);
        rv.setProgressBar(R.id.simple_progress, (int)100, 0, true); // show in progress
        rv.setTextViewText(R.id.simple_percentage, this.mContext.getResources().getString(R.string.loading_message));
    	return rv;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt " + position + " - " + this.mAppWidgetId);
        
        RemoteViews rv = new RemoteViews(this.mContext.getPackageName(), R.layout.simple_data_image);
        try {
            StorageNode node;
            double d;       
            node = (StorageNode)nodeList.get(position);
            if (node.getSize().longValue() != 0L)
                d = 100.0D * node.getFree().doubleValue() / node.getSize().doubleValue();
            else
                d = 100.0D;
            
            DecimalFormat df = new DecimalFormat("#.#"); 
            String text = /*"[" + this.nCount + "]*/ "Free " + df.format(d) + "% " 
                            + "(" + node.getFreeDisplay() 
                            + "/" + node.getSizeDisplay() + ")";
                        
            rv.setTextViewText(R.id.simple_text_name, /*"[" + this.mAppWidgetId + "]: " +  */node.getName());
            rv.setTextViewText(R.id.simple_text_path, node.getPath());
            rv.setProgressBar(R.id.simple_progress, (int)100, (int)(100.0D - d), false);
            rv.setTextViewText(R.id.simple_percentage, text); 
            Integer srcId = -1;
            if (node.getName().contains("External")) {
                srcId = StorageNode.iconMap.get("External");
            } else {
                srcId = StorageNode.iconMap.get(node.getName());
            }                
            
            if (srcId != null && srcId != -1) {                
                rv.setImageViewResource(R.id.storage_icon, srcId.intValue());
            }
            
        } catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex.getMessage());
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
        // TODO Auto-generated method stub
        Log.d(TAG, "onDataSetChanged - " + this.mAppWidgetId);         
        generateStorageList();        
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        
    }   
}