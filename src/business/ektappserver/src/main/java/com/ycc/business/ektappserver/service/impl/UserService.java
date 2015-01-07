package com.ycc.business.ektappserver.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jfinal.plugin.activerecord.Record;
import com.ycc.business.ektappserver.bean.ErrorResponse;
import com.ycc.business.ektappserver.bean.SuccessResponse;
import com.ycc.business.ektappserver.def.AppServerDef;
import com.ycc.business.ektappserver.def.ErrorCode;
import com.ycc.business.ektappserver.module.Userinfo;
import com.ycc.business.ektappserver.service.IUserService;
import com.ycc.core.jfinal.db.DbFactory;
import com.ycc.core.jfinal.db.DbUtil;
import com.ycc.core.myspringioc.annotation.Service;
import com.ycc.core.util.json.JsonUtil;
import com.ycc.core.util.pool.ThreadPoolUtil;
import com.ycc.core.util.validator.CollectionUtil;
import com.ycc.core.util.validator.MapUtil;
import com.ycc.core.util.validator.StringUtil;

@Service("userService")
public class UserService implements IUserService {
	private Record getUserGroup(int uid) {
		Record ug = DbFactory
				.getDb(AppServerDef.MAIN_READ)
				.findFirst(
						"select ug.* from usergroup as ug,temp$user$group as t where t.f_userinfo_id=? and t.f_usergroup_id=ug.id  and ug.state=0",
						uid);
		return ug;
	}

	private boolean getRedisInfo(Map<String, Object> res) {
		List<Record> redisConfig = DbFactory.getDb(AppServerDef.MAIN_READ)
				.find("select * from config");
		if (CollectionUtil.isNotEmpty(redisConfig)) {
			for (Record r : redisConfig) {
				if (r.getStr("cfg_key").equals("REDIS_IP")) {
					res.put("redisIP", r.getStr("cfg_value"));
				}
				if (r.getStr("cfg_key").equals("REDIS_PORT")) {
					res.put("port", r.getStr("cfg_value"));
				}
				if (r.getStr("cfg_key").equals("REDIS_PWD")) {
					res.put("pwd", r.getStr("cfg_value"));
				}
			}
		}
		return true;
	}

	private boolean getWebZipInfo(Map<String, Object> res, int gid) {
		Record web = DbFactory
				.getDb(AppServerDef.MAIN_READ)
				.findFirst(
						"select p.zipUrl, p.zipCrc32 from projectpropperty as p,groupproject as g where p.pgid=g.id and g.f_usergroup_id=?",
						gid);
		if (web == null) {
			return false;
		}
		res.put("zipurl", AppServerDef.WEB_URL + web.getStr("zipUrl"));
		res.put("zipcrc32", web.getStr("zipCrc32"));
		return true;

	}

	public String login(String username, String password) {
		Record user = findUserByUserName(username);
		//Userinfo u = findUserInfoById(user.getInt("id"));
		if (user == null) {
			return ErrorResponse.getErrMsg(ErrorCode.USER_NOT_FOUND);
		}
		if (!user.getStr("password").equals(StringUtil.getMD5(password))) {
			return ErrorResponse.getErrMsg(ErrorCode.PASSWORD_ERROR);
		}
		if (user.getInt("islimit") > 0) {
			return ErrorResponse.getErrMsg(ErrorCode.USER_LIMIT);
		}

		if (user.getInt("loginstate") == 2) {
			user.set("token", UUID.randomUUID().toString());
		}
		Map<String, Object> res = SuccessResponse.getSuccessMap();
		res.put("userInfo", user.getColumns());

		Record ug = getUserGroup(user.getInt("id"));
		if (ug != null) {
			res.put("userGroup", new Object[]{ug.getColumns()});
		}

		getRedisInfo(res);
		if (!getWebZipInfo(res, ug.getInt("id"))) {
			return ErrorResponse.getErrMsg(ErrorCode.WEB_NOT_FOUND);
		}
		update(user.getInt("id"));
		return JsonUtil.getJsonStringFromMap(res);
	}

	private void update(final int userId) {
		Record user = new Record();
		user.set("id", userId);
		user.set("loginstate", 2);
		user.set("sitestate",0);
		user.set("logintime", new Date());
		DbFactory.getDb(AppServerDef.MAIN_WRITE).update("userinfo",user);
		//多线程异步方式在阿里云环境性能更低（数据库磁盘io性能较低）
		/*ThreadPoolUtil.execute(new Runnable() {
			public void run() {
				Thread.currentThread().setName("update login status");
				Record user = new Record();
				user.set("id", userId);
				user.set("loginstate", 2);
				user.set("sitestate",0);
				user.set("logintime", new Date());
				DbFactory.getDb(AppServerDef.MAIN_WRITE).update("userinfo",user);
				Thread.currentThread().setName("free");
			}
		});*/
	}

	public Record findUserByUserName(String username) {
		Record res = DbFactory.getDb(AppServerDef.MAIN_READ).findFirst(
				"select * from userinfo where username=?", username);
		return res;
	}

	public String logout(int userid) {
		String update = "update userinfo set loginstate=0,sitestate=1 where id=?";
		DbFactory.getDb(AppServerDef.MAIN_WRITE).update(update, userid);
		return SuccessResponse.getSuccessStr();
	}

	public String getAllCusLst(Map param) {
		int userid = MapUtil.getInt(param, "userid");
		int groupid = MapUtil.getInt(param, "groupid");
		int type = MapUtil.getInt(param, "type");
		int customerIndex = MapUtil.getInt(param, "customerIndex");
		if (userid == 0 || groupid == 0) {
			return ErrorResponse.getErrMsg(ErrorCode.PARAM_ERROR);
		}
		int state = 0;
		if (type == 1) {
			state = 1;
		} else if (type == 0) {
			state = 2;
		}
		String sql = "select mobileno,id as customerid,customername,sex,url,createtime from customerdistribution where f_userinfo_id =? and f_usergroup_id =? and state =? and id >?";
		List<Record> cuslist = DbFactory.getDb(AppServerDef.MAIN_READ).find(
				sql, userid, groupid, state, customerIndex);
		String cdsql = "select type,f_popularize_id from customerdec where f_customer_id=?";
		String gpsql = "select id from grouppopularize  where state = 0 and f_usergroup_id =? and id=?";
		if (CollectionUtil.isNotEmpty(cuslist)) {
			for (Record cus : cuslist) {
				cus.set("url", "www.ekt.so/"+cus.getStr("url"));
				List<Record> cdlist = DbFactory.getDb(AppServerDef.MAIN_READ)
						.find(cdsql, cus.getLong("customerid"));
				if (CollectionUtil.isEmpty(cdlist)) {
					cus.set("sayhello", 0);
					cus.set("popularize", 0);
				} else {
					boolean s = false, p = false;
					for (Record cd : cdlist) {
						if (cd.getInt("type") == 1) {
							s = true;
						} else if (cd.getInt("type") == 2) {
							Record gp = DbFactory.getDb(AppServerDef.MAIN_READ)
									.findFirst(gpsql, groupid,
											cd.getInt("f_popularize_id"));
							if (gp != null) {
								p = true;
							}
						}

					}
					cus.set("sayhello", s ? 1 : 0);
					cus.set("popularize", p ? 1 : 0);
				}
			}
		}
		return SuccessResponse.getSuccessArray(cuslist);
	}

	public String getPopularize(int groupId) {
		String sql = "select id,content,imgurl,link,descpt as descpt,title,type,state,createtime, f_usergroup_id as usergroupId from grouppopularize  where state = 0 and f_usergroup_id=?";
		List<Record> list = DbFactory.getDb(AppServerDef.MAIN_READ).find(sql,
				groupId);
		return SuccessResponse.getSuccessArray(list);
	}

	public void sayHelloFlag(int cusid, int userid) {
		String sql = "insert into customerdec(type,f_popularize_id,time,state,f_customer_id,f_userinfo_id) values(1,0,?,0,?,?)";
		DbFactory.getDb(AppServerDef.MAIN_WRITE).update(sql, new Date(), cusid,
				userid);
	}

	public void popularizeFlag(int pid, int cusid, int userid) {
		String sql = "insert into customerdec(type,f_popularize_id,time,state,f_customer_id,f_userinfo_id) values(2,?,?,0,?,?)";
		DbFactory.getDb(AppServerDef.MAIN_WRITE).update(sql, pid, new Date(),
				cusid, userid);
	}

	public void saveCusInfo(Map param) {
		int uid = MapUtil.getInt(param, "userid");
		int gid = MapUtil.getInt(param, "groupid");
		int type = MapUtil.getInt(param, "type");
		long cid = MapUtil.getInt(param, "customerid");
		String cname = MapUtil.getString(param, "customername", "");
		String mobileno = MapUtil.getString(param, "mobileno", "");
		String remark = MapUtil.getString(param, "remark", "");
		int sex = MapUtil.getInt(param, "sex", -1);
		int age = MapUtil.getInt(param, "age", -1);
		int intention = MapUtil.getInt(param, "intention", -1);
		int housetype = MapUtil.getInt(param, "housetype", -1);
		int area = MapUtil.getInt(param, "area", -1);
		if(type==1){//回访
			String sql = "update customerdistribution set  state=2,customername=?, mobileno=?,sex=?,gettime=? where id=?";
			DbFactory.getDb(AppServerDef.MAIN_WRITE).update(sql, cname,mobileno,sex,new Date(),cid);
		}else if(type==2){
			String sql="insert into customerdistribution(state,customername,mobileno,sex,gettime,createtime,url,f_usergroup_id,f_userinfo_id) values(2,?,?,?,?,?,'',?,?)";
			Record cus = new Record();
			cus.set("state", 2);
			cus.set("customername", cname);
			cus.set("mobileno", mobileno);
			cus.set("sex", sex);
			cus.set("gettime", new Date());
			cus.set("createtime", new Date());
			cus.set("url", "");
			cus.set("f_usergroup_id", gid);
			cus.set("f_userinfo_id", uid);
			DbFactory.getDb(AppServerDef.MAIN_WRITE).save("customerdistribution", cus);
			cid=cus.getLong("id");//采用jdbc3.0规范，Statement.RETURN_GENERATED_KEYS
			//select last_insert_id()与数据库连接关联，若两次语句取到的不是同一个连接，则存在问题
			/*DbFactory.getDb(AppServerDef.MAIN_WRITE).update(sql, cname,mobileno,sex,new Date(),new Date(),gid,uid);
			Number bi =  DbFactory.getDb(AppServerDef.MAIN_WRITE).queryNumber("select last_insert_id()");
			cid=bi.intValue();*/
		}
		String sql="insert into customerinfo(age,sex,intention,housetype,area,remark,type,f_customer_id,customername,mobileno) values(?,?,?,?,?,?,?,?,?,?)";
		DbFactory.getDb(AppServerDef.MAIN_WRITE).update(sql, age,sex,intention,housetype,area,remark,type,cid,cname,mobileno);
	
	}

	public String modifyuser(Map param) {
		int uid=MapUtil.getInt(param, "userid");
		String uname=MapUtil.getString(param, "username", null);
		String handphone=MapUtil.getString(param, "handphone", null);
		String nickname=MapUtil.getString(param, "nickname", null);
		String photo=MapUtil.getString(param, "photo", null);
		int sex = MapUtil.getInt(param, "sex", -1);
		Record user = findUserById(uid);
		if(user==null){
			return ErrorResponse.getErrMsg(ErrorCode.USER_NOT_FOUND_1);
		}
		if(StringUtil.isNotEmpty(uname)){
			String sql ="select count(id) from userinfo where username =? and id !=?";
			long count = DbFactory.getDb(AppServerDef.MAIN_READ).queryLong(sql, uname,uid);
			if(count>0){
				return ErrorResponse.getErrMsg(ErrorCode.USER_NAME_EXISTS);
			}
			user.set("username", uname);
		}
		user.set("sex", sex);
		if(StringUtil.isNotEmpty(handphone)){
			String sql = "select count(id) from userinfo where handphone =? and id !=?";
			long count = DbFactory.getDb(AppServerDef.MAIN_READ).queryLong(sql, handphone,uid);
			if(count>0){
				return ErrorResponse.getErrMsg(ErrorCode.PHONE_EXISTS);
			}
			user.set("handphone", handphone);
		}
		if(StringUtil.isNotEmpty(nickname)){
			user.set("nickname", nickname);
		}
		if(StringUtil.isNotEmpty(photo)){
			user.set("photo", photo);
		}
		DbFactory.getDb(AppServerDef.MAIN_WRITE).update("userinfo", user);
		return SuccessResponse.getSuccessStr();
	}

	public Record findUserById(int uid) {
		Record res = DbFactory.getDb(AppServerDef.MAIN_READ).findFirst(
				"select * from userinfo where id=?", uid);
		return res;
	}

	public String loginEx(int uid) {
		Map<String,Object> res = SuccessResponse.getSuccessMap();
		Record ug = getUserGroup(uid);
		if(ug==null){
			return ErrorResponse.getErrMsg(ErrorCode.LOGIN_EX_ERROR);
		}
		String sql = "select createtime as createTime,scale as scale,mobileNO as mobileNO,street as street, state as state,password as password,city as city,industry_sec as industrySec,id as id, f_usergroup_id as usergroupID,username as username,area as area,address as address,province as province,industry as industry,projectname as projectName from groupproject where f_usergroup_id=?";
		List<Record> gps = DbFactory.getDb(AppServerDef.MAIN_READ).find(sql,ug.getInt("id"));
		if(CollectionUtil.isNotEmpty(gps)){
			res.put("groupProject", change(gps));
		}
		sql="select createtime as createtime,state as state,content as content,id as id,f_usergroup_id as usergroupID,type as type from groupmsgmodal where f_usergroup_id=?";
		List<Record> ms = DbFactory.getDb(AppServerDef.MAIN_READ).find(sql,ug.getInt("id"));
		if(CollectionUtil.isNotEmpty(ms)){
			res.put("groupMsgModal", change(ms));
		}
		sql="select state as state,id as id, role as role, f_usergroup_id as usergroupID,f_userinfo_id as userinfoID,isanswer as isanswer from temp$user$group where f_usergroup_id=? and f_userinfo_id=?";
		List<Record> ts = DbFactory.getDb(AppServerDef.MAIN_READ).find(sql,ug.getInt("id"),uid);
		if(CollectionUtil.isNotEmpty(ts)){
			res.put("tempUserGroup", change(ts));
		}
		return JsonUtil.getJsonStringFromMap(res);
	}
	private Object[] change(List<Record> records){
		
		Object[] res = new Object[records.size()];
		for(int i=0;i<records.size();i++){
			res[i]=records.get(i).getColumns();
		}
		return res;
	}

	public Userinfo findUserInfoById(int uid) {
		return DbUtil.findById(AppServerDef.MAIN_READ, uid, Userinfo.class);
	}

}
