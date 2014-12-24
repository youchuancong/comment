package com.ycc.business.ektappserver.service;

import java.util.Map;

import com.jfinal.plugin.activerecord.Record;
import com.ycc.business.ektappserver.module.Userinfo;

public interface IUserService {
	public Record findUserByUserName(String username);
	public Record findUserById(int uid);
	public Userinfo findUserInfoById(int uid);
	public String  login(String username,String password)  ;
	public String  loginEx(int uid)  ;
	public String logout(int userid);
	public String getAllCusLst(Map param);
	public String getPopularize(int groupId);
	public void sayHelloFlag(int cusid,int userid);
	public void popularizeFlag(int pid,int cusid,int userid);
	public void saveCusInfo(Map param);
	public String modifyuser(Map param);
}
