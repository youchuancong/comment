package com.ycc.business.ektappserver.bean;

import java.util.Date;

import lombok.Data;
@Data
public class FileInfo {
String name;
long size;
Date time;
public FileInfo(String name, long size, Date time) {
	super();
	this.name = name;
	this.size = size;
	this.time = time;
}
}
