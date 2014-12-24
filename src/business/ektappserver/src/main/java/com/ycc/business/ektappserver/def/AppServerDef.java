package com.ycc.business.ektappserver.def;

import com.ycc.core.util.config.SystemConfigUtil;
import com.ycc.core.util.validator.StringUtil;

public class AppServerDef {
public static final String AGENT_PARAM="AGENT_PARAM";
public static String WEB_URL="http://chat.9onlylove.com/mgr";
public static String MAIN_READ="mainread";
public static String MAIN_WRITE="mainwrite";
static{
	String u = SystemConfigUtil.get("WEB_URL");
	if(StringUtil.isNotEmpty(u)){
		WEB_URL=u;
	}
}
}
