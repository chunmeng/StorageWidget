package com.tempura.storagewidget;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

//! A utility class to execute the system commands and parse the output into a list
public class SystemCommander {
	private final static String TAG = "StorageWidget:Commander";
	
    public static ArrayList<StorageNode> runCatMeminfo(Context paramContext) {
        ArrayList<StorageNode> aList = new ArrayList<StorageNode>();
        
        return aList;
    }
    
    /**
     * Execute the 'df' command and parse for known filesystem
     * 
     * From a couple of devices (htc onex, asus transformer prime, nexus 7), the format of the output looks like:
     * Filesystem	Size	Used	Free	Blksize
     * </mount>     <488M>  <128K>  <488M>  <4096>
     *    
     * @param paramContext
     * @return A list of storage node
     */
    public static ArrayList<StorageNode> runDf(Context paramContext) {
        ArrayList<StorageNode> aList = new ArrayList<StorageNode>();
        
        Process proc;
        BufferedReader bufReader;
        String[] arrayOfString;
        StorageNode localDataItem;
        try
        {
        	proc = Runtime.getRuntime().exec("df");
        	bufReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        	boolean IsHeaderRow = false;
        	String str = bufReader.readLine();
        	while (str != null)
        	{
        		if (!str.startsWith("Filesystem")) // Skip header row
        		{        		  
        			// parse
        			arrayOfString = str.split(" +"); // break it down
        		
        			if (arrayOfString.length == 5) {
        				Log.d(TAG, "Parsing: " + str + " (" + arrayOfString.length + ")");
        				// Get the mount point
        				String path = arrayOfString[0].replace(":", "");
        			  
        				// Check if important?        			  
        				if (StorageNode.isPartitionImportant(path)) {
        					Log.d(TAG, "--- " + path + " is important!");
        					// Yes - Pass the path to StatFs to get stats
        					// Create the storageNode
        			  
        					// Add it to list aList.add(localDataItem);
        				}
        			}        		  
        		}
        		str = bufReader.readLine(); // read next line
        	}
          
        	bufReader.close();
        	proc.waitFor();         
        } catch (Exception ex) {
        	Log.d(TAG, "df: " + ex.getMessage());
        	ex.printStackTrace();
        }
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
        StorageNode dataNode = getStorageNode(Environment.getDataDirectory());        
        aList.add(dataNode);
        
        // Do not add if emulated
        if (!Environment.isExternalStorageEmulated()) {
        	StorageNode sdNode = getStorageNode(Environment.getExternalStorageDirectory());
        	aList.add(sdNode);
        } 

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
        StorageNode sn = new StorageNode(name, path, fs.getBlockCount(), fs.getAvailableBlocks(), fs.getBlockSize());  
        return sn;
    }
    
/*    public boolean parseDfString(Integer paramInteger, String[] paramArrayOfString)
    {     
      if (paramArrayOfString == null);
      while (true)
      {
        return;
        try
        {
          Long localLong = Long.valueOf(1L);

          Integer localInteger2;
          Integer localInteger3;
          label76: String str;
          switch (paramInteger.intValue())
          {
          default:

            localInteger2 = Integer.valueOf(1);
            Integer.valueOf(2);
            localInteger3 = Integer.valueOf(3);
            Integer.valueOf(4);
            this.path = paramArrayOfString[0].replace(":", "");
            str = this.path.toLowerCase();
            if (!partitionNames.containsKey(str))
              break;
          case 2:
          case 3:
          case 4:
          }
          for (this.name = ((String)partitionNames.get(str)); ; this.name = this.path)
          {
            if (paramInteger.intValue() == 4)
              break label345;
            StatFs localStatFs = new StatFs(this.path);
            this.size = Long.valueOf(localStatFs.getBlockCount() * localStatFs.getBlockSize());
            this.free = Long.valueOf(localStatFs.getAvailableBlocks() * localStatFs.getBlockSize());
            this.used = Long.valueOf(this.size.longValue() - this.free.longValue());
            this.blksize = Long.valueOf(localStatFs.getBlockSize());
            break;
            localInteger1 = Integer.valueOf(0);
            localInteger2 = Integer.valueOf(1);
            Integer.valueOf(3);
            localInteger3 = Integer.valueOf(5);
            Integer.valueOf(9);
            break label76;
            localInteger1 = Integer.valueOf(5);
            localInteger2 = Integer.valueOf(1);
            Integer.valueOf(2);
            localInteger3 = Integer.valueOf(3);
            Integer.valueOf(-1);
            localLong = Long.valueOf(1024L);
            break label76;
            localInteger1 = Integer.valueOf(0);
            localInteger2 = Integer.valueOf(1);
            Integer.valueOf(-1);
            localInteger3 = Integer.valueOf(2);
            Integer.valueOf(-1);
            break label76;
          }
          label345: this.size = Long.valueOf(sizeInBytes(paramArrayOfString[localInteger2.intValue()]).longValue() * localLong.longValue());
          if ((paramArrayOfString[localInteger3.intValue()].length() != 0) && (this.size.longValue() != -1L))
            this.used = Long.valueOf(this.size.longValue() - sizeInBytes(paramArrayOfString[localInteger3.intValue()]).longValue() * localLong.longValue());
          if ((paramArrayOfString[localInteger3.intValue()].length() == 0) && (this.size.longValue() != -1L) && (this.used.longValue() != -1L))
            this.free = Long.valueOf(this.size.longValue() - this.used.longValue());
          else
            this.free = Long.valueOf(sizeInBytes(paramArrayOfString[localInteger3.intValue()]).longValue() * localLong.longValue());
        }
        catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
        {
        }
        catch (NumberFormatException localNumberFormatException)
        {
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
        }
      }
    }    */
}