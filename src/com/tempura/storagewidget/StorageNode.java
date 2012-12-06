package com.tempura.storagewidget;

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
    
    //! A mapping for known mount point to a friendly name
    public static final Map<String, String> partitionNames = new HashMap<String, String>();
    
    static
    {
        partitionNames.put("/system", "System");
        partitionNames.put("/data", "Data");
        partitionNames.put("/mnt/sdcard", "SD card");
        partitionNames.put("/storage/sdcard0", "SD card");
        partitionNames.put("/storage/sdcard1", "SD card 2");
        partitionNames.put("/storage/extsdcard", "External SD card");
        partitionNames.put("/mnt/extsdcard", "External SD card");
        partitionNames.put("/mnt/sdcard/external_sd", "External SD card");
        partitionNames.put("/mnt/emmc", "External SD card");
        partitionNames.put("/mnt/external_sd", "External SD card");
        partitionNames.put("/mnt/external_sd", "External SD card");
        partitionNames.put("/cache", "Cache");
        partitionNames.put("/mnt/cache", "Cache");
        partitionNames.put("/data/sdext", "SD-Ext");
        partitionNames.put("/data/sdext2", "SD-Ext2");
        partitionNames.put("/data/sdext3", "SD-Ext3");
        partitionNames.put("/data/sdext4", "SD-Ext4");
        partitionNames.put("/sd-ext", "SD-Ext");
        partitionNames.put("/sd-ext2", "SD-Ext2");
        partitionNames.put("/sd-ext3", "SD-Ext3");
        partitionNames.put("/sd-ext4", "SD-Ext4");
        partitionNames.put("/system/sd", "SD-Ext");
        partitionNames.put("/mnt/usbdisk", "USB drive");
    }    

    private void initialize()
    {
        this.name = "unknown";
        this.path = "";
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

    public StorageNode(String strName) {
        initialize();
        this.name = strName;
        this.path = ("--" + strName);
    }

    public StorageNode(String strName, long lSize, long lFree) {
        initialize();
        this.name = strName;
        this.path = ("--" + strName);
        this.size = lSize;
        this.free = lFree;
        this.used = lSize - lFree;
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
    
    public void setPath(String sPath)
    {
        this.path = sPath;
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
    
    public static final Parcelable.Creator<StorageNode> CREATOR = new Parcelable.Creator()
    {
        public StorageNode createFromParcel(Parcel parcelIn)
        {
            return new StorageNode(parcelIn);
        }

        public StorageNode[] newArray(int count)
        {
            return new StorageNode[count];
        }
    };    
}