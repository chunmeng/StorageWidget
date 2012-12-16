package com.tempura.storagewidget;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

//! A utility class to execute the system commands and parse the output into a list
public class SystemCommander {

    public static ArrayList<StorageNode> runCatMeminfo(Context paramContext) {
        ArrayList<StorageNode> aList = new ArrayList<StorageNode>();
        
        return aList;
    }
    
    public static ArrayList<StorageNode> runDf(Context paramContext) {
        ArrayList<StorageNode> aList = new ArrayList<StorageNode>();
        return aList;
    }
    
    //! Get the node list from Environment 
    public static ArrayList<StorageNode> getEnvFS(Context paramContext) {
        ArrayList<StorageNode> aList = new ArrayList<StorageNode>();
        // Get cache storage
        aList.add(getStorageNode(Environment.getDownloadCacheDirectory()));

        // Get system storage
        aList.add(getStorageNode(Environment.getRootDirectory()));

        // Get data storage
        aList.add(getStorageNode(Environment.getDataDirectory()));        

        // Get sdcard storage
        aList.add(getStorageNode(Environment.getExternalStorageDirectory()));
                
        //aList.add(getStorageNode("/Removable/MicroSD"));
        
        return aList;
    }
    
    private static StorageNode getStorageNode(File envFile) {
        return getStorageNode(envFile.getPath());
    }
    
    public static StorageNode getStorageNode(String path) {
        StatFs fs = new StatFs(path);
        String name = StorageNode.nodeMap.get(path);
        if (name == null) { // If the path is not known before, this will be null
            name = "Generic";
        }
        StorageNode sn = new StorageNode(name, 
                fs.getBlockCount(), fs.getAvailableBlocks());
        sn.setPath(path);
        sn.setBlockSize(fs.getBlockSize());      
        return sn;
    }
}