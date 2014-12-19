package com.ycc.business.ektappserver.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jfinal.plugin.activerecord.Record;
import com.ycc.business.ektappserver.bean.ErrorResponse;
import com.ycc.business.ektappserver.bean.SuccessResponse;
import com.ycc.business.ektappserver.def.AppServerDef;
import com.ycc.business.ektappserver.def.ErrorCode;
import com.ycc.business.ektappserver.service.IUserService;
import com.ycc.core.jfinal.db.DbFactory;
import com.ycc.core.util.json.JsonUtil;
import com.ycc.core.util.pool.ThreadPoolUtil;
import com.ycc.core.util.validator.CollectionUtil;
import com.ycc.core.util.validator.StringUtil;
@Service("userService")
public class UserService implements IUserService{
	private Record getUserGroup(int uid){
		Record ug = DbFactory.getDb(AppServerDef.MAIN_READ).findFirst("select ug.* from usergroup as ug,temp$user$group as t where t.f_userinfo_id=? and t.f_usergroup_id=ug.id ", uid);
		return ug;
	}
	private boolean getRedisInfo(Map<String,Object> res){
		List<Record> redisConfig =  DbFactory.getDb(AppServerDef.MAIN_READ).find("select * from config");
		if(CollectionUtil.isNotEmpty(redisConfig)){
			for(Record r:redisConfig){
				if(r.getStr("cfg_key").equals("REDIS_IP")){
					res.put("redisIP", r.getStr("cfg_value"));
				}
				if(r.getStr("cfg_key").equals("REDIS_PORT")){
					res.put("port", r.getStr("cfg_value"));
				}
				if(r.getStr("cfg_key").equals("REDIS_PWD")){
					res.put("pwd", r.getStr("cfg_value"));
				}
			}
		}
		return true;
	}
	private boolean getWebZipInfo(Map<String,Object> res,int gid){
		Record web =  DbFactory.getDb(AppServerDef.MAIN_READ).findFirst("select p.zipUrl, p.zipCrc32 from projectpropperty as p,groupproject as g where p.pgid=g.id and g.f_usergroup_id=?",gid);
		if(web==null){
			return false;
		}
		res.put("zipurl",AppServerDef.WEB_URL+ web.getStr("zipUrl"));
		res.put("zipcrc32", web.getStr("zipCrc32"));
		return true;
		
	}
	public String login(String username,String password){
		Record user = findUserByUserName(username);
		if(user==null){
			return ErrorResponse.getErrMsg(ErrorCode.USER_NOT_FOUND);
		}
		if(!user.getStr("password").equals(StringUtil.getMD5(password))){
			return ErrorResponse.getErrMsg(ErrorCode.PASSWORD_ERROR);
		}
		if(user.getInt("islimit")>0){
			return ErrorResponse.getErrMsg(ErrorCode.USER_LIMIT);
		}
		
		if(user.getInt("loginstate")==2){
			user.set("token", UUID.randomUUID().toString());
		}
		Map<String,Object> res = SuccessResponse.getSuccessMap();
		res.put("userInfo", user.getColumns());
		
		Record ug = getUserGroup(user.getInt("id"));
		if(ug!=null){
			res.put("userGroup", ug.getColumns());
		}
		
		getRedisInfo(res);
		if(!getWebZipInfo(res,ug.getInt("id"))){
			return ErrorResponse.getErrMsg(ErrorCode.WEB_NOT_FOUND);
		}
		update(user.getInt("id"));
		return JsonUtil.getJsonStringFromMap(res);
	}
	private void update(final int userId ){
		ThreadPoolUtil.execute(new Runnable(){
			public void run() {
				Thread.currentThread().setName("update login status");
				String update="update userinfo set loginstate=2,sitestate=0,logintime=? where id=?";
				DbFactory.getDb(AppServerDef.MAIN_WRITE).update(update, new Date(),userId);
				Thread.currentThread().setName("free");
			}
		});
	}
	public Record findUserByUserName(String username) {
		Record res = DbFactory.getDb(AppServerDef.MAIN_READ).findFirst("select * from userinfo where username=?", username);
		return res;
	}

}
