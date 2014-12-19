package com.ycc.business.ektappserver.service;

import com.jfinal.plugin.activerecord.Record;

public interface IUserService {
	public Record findUserByUserName(String username);
	public String  login(String username,String password)  ;
}
