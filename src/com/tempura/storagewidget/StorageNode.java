package com.tempura.storagewidget;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
//! A class to holding a storage unit's information
public class StorageNode implements Comparable, Parcelable {

    private Long blksize;
    private String fileSystem;
    private Long free;
    private String mountType;
    private String name;
    private String path;
    private Long size;
    private Long used;
    
    private static final long TB = 0x10000000000L;
    private static final long GB = 0x40000000L;    
    private static final long MB = 0x100000L;
    private static final long KB = 0x400L;    
    
    //! A mapping for known mount point to a friendly name
    public static final Map<String, String> nodeMap = new HashMap<String, String>();
    
    /**
     * NOTE TO SELF: 
     * About external (removable) storage, it's a mess. Mount point is per manufacturer. 
     * The best hope is to run a "df" command and analyze the output. 
     */
    static
    {
        nodeMap.put("/system", "System");
        nodeMap.put("/data", "Data");
        nodeMap.put("/mnt/sdcard", "SD card");
        nodeMap.put("/storage/sdcard0", "SD card");
        nodeMap.put("/storage/sdcard1", "SD card 2");
        nodeMap.put("/storage/extsdcard", "External SD card");
        nodeMap.put("/mnt/extsdcard", "External SD card");
        nodeMap.put("/mnt/sdcard/external_sd", "External SD card");  //!< Samsung galaxy family
        nodeMap.put("/mnt/emmc", "External SD card");
        nodeMap.put("/mnt/external_sd", "External SD card");
        nodeMap.put("/removable/microsd", "External SD card"); 		//!< Asus transformer prime
        nodeMap.put("/cache", "Cache");
        nodeMap.put("/mnt/cache", "Cache");
        nodeMap.put("/data/sdext", "SD-Ext");
        nodeMap.put("/data/sdext2", "SD-Ext2");
        nodeMap.put("/data/sdext3", "SD-Ext3");
        nodeMap.put("/data/sdext4", "SD-Ext4");
        nodeMap.put("/sd-ext", "SD-Ext");
        nodeMap.put("/sd-ext2", "SD-Ext2");
        nodeMap.put("/sd-ext3", "SD-Ext3");
        nodeMap.put("/sd-ext4", "SD-Ext4");
        nodeMap.put("/system/sd", "SD-Ext");
        nodeMap.put("/mnt/usbdisk", "USB drive");
        // nodeMap.put("/storage/emulated/0", "SD card");  // Nexus-7, JB - this is emulated
    }    
    
    public static final Map<String, Integer> iconMap = new HashMap<String, Integer>();
    static {
        iconMap.put("System", R.drawable.blue_device_28);
        iconMap.put("Data", R.drawable.blue_data_04);
        iconMap.put("SD card", R.drawable.blue_folder_03);
        iconMap.put("Cache", R.drawable.blue_cache_11);	
        iconMap.put("External", R.drawable.blue_external_36);
    }

    private void initialize() {
        this.name = "unknown";
        this.path = "/unknown";
        this.size = Long.valueOf(-1L);
        this.used = Long.valueOf(-1L);
        this.free = Long.valueOf(-1L);
        this.blksize = Long.valueOf(-1L);
        this.fileSystem = "unknown";
        this.mountType = "unknown";
    }
    
    private StorageNode(Parcel parcelIn)
    {
        this.name = parcelIn.readString();
        this.path = parcelIn.readString();
        this.free = Long.valueOf(parcelIn.readLong());
        this.used = Long.valueOf(parcelIn.readLong());
        this.size = Long.valueOf(parcelIn.readLong());
        this.blksize = Long.valueOf(parcelIn.readLong());
        this.fileSystem = parcelIn.readString();
        this.mountType = parcelIn.readString();
    }    
    
    public static boolean isPartitionImportant(String mountPoint)
    {
    	return nodeMap.containsKey(mountPoint.toLowerCase());
    }
    
    public static String formatSize(Long longSize)
    {
    	String str;
        if (longSize.longValue() >= TB)
            str = smartFormatter(Long.valueOf(longSize.longValue() / TB), longSize.doubleValue() / (double)TB, "TB");        
        else if (longSize.longValue() >= GB)
            str = smartFormatter(Long.valueOf(longSize.longValue() / GB), longSize.doubleValue() / (double)GB, "GB");
        else if (longSize.longValue() >= MB)
            str = smartFormatter(Long.valueOf(longSize.longValue() / MB), longSize.doubleValue() / (double)MB, "MB");
        else if (longSize.longValue() >= KB)
            str = smartFormatter(Long.valueOf(longSize.longValue() / KB), longSize.doubleValue() / (double)KB, "KB");
        else
            str = "" + longSize + " B ";
        
        return str;      
    }    
    
    private static String smartFormatter(Long lSize, double dSize, String postFix)
    {
        DecimalFormat df = new DecimalFormat("#.#");
        String str;
        if (lSize.longValue() < 100L)
            str = df.format(dSize) + " " + postFix; 
        else
            str = lSize + " " + postFix;
        return str;
    } 
    
    private static Long sizeInBytes(String sizeStr)
    {
    	if ((sizeStr == null) || (sizeStr.length() < 2))
    		throw new IllegalArgumentException("size string is null.");
      
    	// Convert last character to uppercase
    	Character lastChar = Character.toUpperCase(sizeStr.charAt(-1 + sizeStr.length()));
      
    	Long size = 0L;
    	if ((lastChar.equals('T')) || (lastChar.equals('G')) || (lastChar.equals('M')) || (lastChar.equals('K')))
    	{
    		size = Long.parseLong(sizeStr.substring(0, -1 + sizeStr.length()));
    		switch (lastChar.charValue())
    		{
    			case 'T':
    				size = TB * size.longValue();
    				break;
    			case 'G':
    				size = GB * size.longValue();
    				break;
    			case 'M':
    				size = MB * size.longValue();
    				break;
    			case 'K':
    				size = KB * size.longValue();
    				break;
    			default:    				
    				break;
    		}
    	} else {
    		size = Long.parseLong(sizeStr);
    	}
    		
    	return size;
    }    

    public StorageNode(String strName) {
        initialize();
        this.name = strName;
        this.path = ("--" + strName);
    }

    // Prevent use
    private StorageNode(String strName, long lSize, long lFree) {
        initialize();
        this.name = strName;
        this.path = ("--" + strName);
        this.size = lSize;
        this.free = lFree;
        this.used = lSize - lFree;
    }
    
    public StorageNode(String strName, String strPath, long lSize, long lFree, long lBlkSize) {
        initialize();
        this.name = strName;
        this.path = strPath;
        this.size = lSize;
        this.free = lFree;
        this.used = lSize - lFree;
        this.blksize = lBlkSize;
    }    

    public String getName() {
        return this.name;
    }

    public Long getSize() {
        return this.size;
    }

    public Long getFree() {
        return this.free;
    }
    
    public String getPath() { 
        return this.path;
    }
    
    public void setPath(String sPath) {
        this.path = sPath;
    }    

    public void setBlockSize(long lBlkSize) {
        this.blksize = lBlkSize;
    }

    public Long getBlkSize() {
    	return this.blksize;
    }    
    
    public String getSizeDisplay() {
        if (this.blksize == -1L)
            return formatSize(this.size);
        else
            return formatSize(this.size * this.blksize);
    }
    
    public String getFreeDisplay() {
        if (this.blksize == -1L)
            return formatSize(this.free);
        else
            return formatSize(this.free * this.blksize);        
    }
    
    public long getSizeRaw() {
        if (this.blksize == -1L)
            return this.size;
        else
            return this.size * this.blksize;    	
    }
    
    public long getFreeRaw() {
        if (this.blksize == -1L)
            return this.free;
        else
            return this.free * this.blksize;    
    }
        
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcelOut, int arg1) {
        parcelOut.writeString(this.name);
        parcelOut.writeString(this.path);
        parcelOut.writeLong(this.free.longValue());
        parcelOut.writeLong(this.used.longValue());
        parcelOut.writeLong(this.size.longValue());
        parcelOut.writeLong(this.blksize.longValue());
        parcelOut.writeString(this.fileSystem);
        parcelOut.writeString(this.mountType);
    }

    @Override
    public int compareTo(Object paramObject) {
        return this.name.compareTo(((StorageNode)paramObject).getName());
    }
    
    public static final Parcelable.Creator<StorageNode> CREATOR = new Parcelable.Creator<StorageNode>() {
        public StorageNode createFromParcel(Parcel parcelIn) {
            return new StorageNode(parcelIn);
        }

        public StorageNode[] newArray(int count) {
            return new StorageNode[count];
        }
    };    
}