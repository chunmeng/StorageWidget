package com.tempura.storagewidget;

import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

/**
 * The Data content provider for the widget.
 */
public class StorageDataProvider extends ContentProvider {
    public static final Uri CONTENT_URI =
            Uri.parse("content://com.tempura.storagewidget.provider");
    
    //! This is how we will return the query result table
    public static class Columns {
        public static final String ID = "_id"; 		//!< an unique id
        public static final String NAME = "name"; 	//!< name of the storage node
        public static final String PATH = "path";	//!< path or mount point of the storage node
        public static final String SIZE = "size";		//!< total size (B) 
        public static final String FREE = "free";		//!< total free size (B)
        public static final String BLKSIZE = "blksize";		//!< block size (B)
    }
    
    /**
     * Generally, this data will be stored in an external and persistent location (ie. File,
     * Database, SharedPreferences) so that the data can persist if the process is ever killed.
     * For simplicity, in this sample the data will only be stored in memory.
     */
    private static final ArrayList<StorageNode> sData = new ArrayList<StorageNode>();    
    
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		refreshData();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
        assert(uri.getPathSegments().isEmpty());

        // Force a refresh on query
        refreshData();
        
        // Only support query without any parameters, so we can just return a cursor to
        // all the storage data.
        // Table header
        final MatrixCursor c = new MatrixCursor(
                new String[]{ Columns.ID, Columns.NAME, Columns.PATH, Columns.SIZE, Columns.FREE, Columns.BLKSIZE });
        // Table rows
        for (int i = 0; i < sData.size(); ++i) {
            final StorageNode data = sData.get(i);
            c.addRow(new Object[]{ Integer.valueOf(i), data.getName(), data.getPath(), data.getSizeRaw(), data.getFreeRaw(), data.getBlkSize() });
        }
        return c;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * 
	 */	
    private void refreshData() {

        synchronized (sData) {    
            // If the list is not emptied, clear it
            if (!sData.isEmpty()) {
            	sData.clear();
            }
            
            // Get the local filesystem info
            /*
            ArrayList<StorageNode> dfList = SystemCommander.getEnvFS(getContext());
            if (!dfList.isEmpty()) {
            	sData.addAll(dfList);
            }*/
            
            // Get the memory info
            ArrayList<StorageNode> dfList = SystemCommander.runDf(getContext());
            if (!dfList.isEmpty()) {
            	// Add none duplicate
            	sData.addAll(dfList);
            }
            
            // Get the memory info
            ArrayList<StorageNode> memList = SystemCommander.runCatMeminfo(getContext());
            if (!memList.isEmpty())
            {
            	sData.addAll(memList);
            }           
        }
        return;        
    }

    //! UT method
    private void generateTestData() {               
        if (sData.isEmpty()) {
        	sData.add(new StorageNode("Data", "/data", 100L, 20L, 1L));
        	sData.add(new StorageNode("SD Card", "/sdcard", 200L, 100L, 1L));
        }
    }   	

}
