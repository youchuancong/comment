package com.ycc.business.ektappserver.control;

import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.spring.Inject;
import com.ycc.business.ektappserver.def.AppServerDef;
import com.ycc.business.ektappserver.interceptor.MainInterceptors;
import com.ycc.business.ektappserver.service.IUserService;
import com.ycc.core.util.json.JsonUtil;
@ControllerBind(controllerKey="/appServer")
@Before(MainInterceptors.class)
public class AppServerController extends Controller {
	Logger log = Logger.getLogger(AppServerController.class);
	@Inject.BY_NAME
	IUserService userService;
	public void index() {
		renderText("hello wordl");
	}

	public void login(){
		String param = getAttrForStr(AppServerDef.AGENT_PARAM);
		Map map = JsonUtil.getMapFromJsonString(param);
		String res = userService.login((String)map.get("username"),(String)map.get("password"));
		renderJson(res);
	}
}
