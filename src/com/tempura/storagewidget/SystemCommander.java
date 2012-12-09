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
        // Get data storage
        aList.add(getStorageNode(Environment.getDataDirectory()));
        
        // Get cache storage
        aList.add(getStorageNode(Environment.getDownloadCacheDirectory()));

        // Get sdcard storage
        aList.add(getStorageNode(Environment.getExternalStorageDirectory()));
        
        // Get system storage
        aList.add(getStorageNode(Environment.getRootDirectory()));
        
        return aList;
    }
    
    private static StorageNode getStorageNode(File envFile) {
        StatFs fs = new StatFs(envFile.getPath());
        StorageNode sn = new StorageNode(StorageNode.nodeMap.get(envFile.getPath()), 
                fs.getBlockCount(), fs.getAvailableBlocks());
        sn.setPath(envFile.getPath());
        sn.setBlockSize(fs.getBlockSize());
        return sn;
    }
}

/*
Process localProcess;
BufferedReader localBufferedReader;
String[] arrayOfString;
DataItem localDataItem;
try
{
  localProcess = Runtime.getRuntime().exec("df");
  localBufferedReader = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
  Boolean localBoolean = Boolean.valueOf(false);
  String str = localBufferedReader.readLine();
  if (str != null)
  {
    if (str.startsWith("Filesystem"))
      localBoolean = Boolean.valueOf(true);
    while (true)
    {
      str = localBufferedReader.readLine();
      break;
      arrayOfString = str.split(" +");
      if (!localBoolean.booleanValue())
        break label184;
      if (arrayOfString.length != 6)
        break label166;
      localDataItem = new DataItem(Integer.valueOf(3), arrayOfString);
      label123: if (!paramBoolean)
        break label202;
      localArrayList.add(localDataItem);
    }
  }
}
catch (Exception localException)
{
  showException(paramContext, "df: " + localException.getMessage());
}
while (true)
{
  return localArrayList;
  label166: localDataItem = new DataItem(Integer.valueOf(1), arrayOfString);
  break label123;
  label184: localDataItem = new DataItem(Integer.valueOf(2), arrayOfString);
  break label123;
  label202: if (!DataItem.isPartitionImportant(localDataItem.getPath()))
    break;
  localArrayList.add(localDataItem);
  break;
  localBufferedReader.close();
  localProcess.waitFor();
}*/