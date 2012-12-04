package com.tempura.storagewidget;

import java.util.ArrayList;

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

	private static ArrayList<DataItem> dataItems; // A list of the available storage 
	
	private Context mContext;
	
	public StorageListAdapter(Context paramContext) {
		this.mContext = paramContext; 
		
		// UT: Test sample data list
		generateTestList();		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataItems.get(position);
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

	void generateTestList() {		
		if (dataItems == null) {
			dataItems = new ArrayList<DataItem>();
		}
		
		if (dataItems.isEmpty()) {
			dataItems.add(new DataItem("Test Data", 100L, 20L));
			dataItems.add(new DataItem("Test SDCard", 200L, 100L));
		}
	}	
	
	// This is a object holder for layout defined in simple_data.xml
	static class SimpleViewHolder
	{
		TextView percentage;
		ProgressBar progressBar;
		TextView txtName;		
	}

	@Override
	public RemoteViews getLoadingView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		// TODO Auto-generated method stub
		RemoteViews rv = new RemoteViews(this.mContext.getPackageName(), R.layout.simple_data);
	    DataItem localDataItem;
	    double d;		
		localDataItem = (DataItem)dataItems.get(position);          
        if (localDataItem.getSize().longValue() != 0L)
        	d = 100L * localDataItem.getFree().longValue() / localDataItem.getSize().longValue();
        else
        	d = 100.0D;
        
        String text = d + "% free " 
        				+ "(" + localDataItem.getFree().longValue() 
        				+ "/" + localDataItem.getSize().longValue() + ")";
	    
        rv.setTextViewText(R.id.simple_text_name, localDataItem.getName());
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
