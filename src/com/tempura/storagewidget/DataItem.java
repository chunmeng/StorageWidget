package com.tempura.storagewidget;

public class DataItem {

	private Long blksize;
	private String fileSystem;
	private Long free;	
	private String mountType;
	private String name;
	private String path;
	private Long size;
	private Long used;
	
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
	
	public DataItem(String strName) {
		initialize();
		this.name = strName;
		this.path = ("--" + strName);
	}
	
	public DataItem(String strName, long lSize, long lFree) {
		initialize();
		this.name = strName;
		this.path = ("--" + strName);
		this.size = lSize;
		this.free = lFree;
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
}
