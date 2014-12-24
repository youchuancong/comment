package com.ycc.core.daogenerate.service.impl;

import java.security.Timestamp;

import lombok.Data;

import com.ycc.core.util.db.*;

@Data
@RTable("projectpropperty")
public class Projectpropperty {
	@RColumn("id")
	Integer id;
	@RColumn("pgid")
	Integer pgid;
	@RColumn("conf")
	String conf;
	@RColumn("bd_user")
	String bd_user;
	@RColumn("bd_pwd")
	String bd_pwd;
	@RColumn("bd_code")
	String bd_code;
	@RColumn("url")
	String url;
	@RColumn("uploadTime")
	Timestamp uploadTime;
	@RColumn("zipUrl")
	String zipUrl;
	@RColumn("zipCrc32")
	String zipCrc32;
}