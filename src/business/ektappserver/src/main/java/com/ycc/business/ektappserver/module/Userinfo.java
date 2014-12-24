package com.ycc.business.ektappserver.module;

import java.util.Date;
import lombok.Data;
import com.ycc.core.jfinal.db.Module; 
import com.ycc.core.util.db.*;

@Data
@RTable("userinfo")
public class Userinfo  implements Module{
	public static final String TABLE="userinfo";
	
	public static final String ID="id";
	public static final String USERNAME="username";
	public static final String PASSWORD="password";
	public static final String HANDPHONE="handphone";
	public static final String CREATETIME="createtime";
	public static final String TOKEN="token";
	public static final String ISLIMIT="islimit";
	public static final String RECODE="recode";
	public static final String PHOTO="photo";
	public static final String SITESTATE="sitestate";
	public static final String LOGINSTATE="loginstate";
	public static final String ISANSWER="isanswer";
	public static final String SEX="sex";
	public static final String EMAIL="email";
	public static final String ADDRESS="address";
	public static final String NICKNAME="nickname";
	public static final String USERNAME$TCC="username$TCC";
	public static final String PASSWORD$TCC="password$TCC";
	public static final String LEVEL="level";
	public static final String FILECRC="filecrc";
	public static final String BALANCE="balance";
	public static final String PREINDEX="preIndex";
	public static final String THISINDEX="thisIndex";
	public static final String LOGINTIME="logintime";


	@RColumn("id")
	public Integer id;
	@RColumn("username")
	public String username;
	@RColumn("password")
	public String password;
	@RColumn("handphone")
	public String handphone;
	@RColumn("createtime")
	public Date createtime;
	@RColumn("token")
	public String token;
	@RColumn("islimit")
	public Integer islimit;
	@RColumn("recode")
	public String recode;
	@RColumn("photo")
	public String photo;
	@RColumn("sitestate")
	public Integer sitestate;
	@RColumn("loginstate")
	public Integer loginstate;
	@RColumn("isanswer")
	public Integer isanswer;
	@RColumn("sex")
	public Integer sex;
	@RColumn("email")
	public String email;
	@RColumn("address")
	public String address;
	@RColumn("nickname")
	public String nickname;
	@RColumn("username$TCC")
	public String username$TCC;
	@RColumn("password$TCC")
	public String password$TCC;
	@RColumn("level")
	public Integer level;
	@RColumn("filecrc")
	public String filecrc;
	@RColumn("balance")
	public Integer balance;
	@RColumn("preIndex")
	public Integer preIndex;
	@RColumn("thisIndex")
	public Integer thisIndex;
	@RColumn("logintime")
	public Date logintime;
}