package com.ycc.business.ektappserver.control;

import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.spring.Inject;
import com.ycc.business.ektappserver.bean.ErrorResponse;
import com.ycc.business.ektappserver.bean.SuccessResponse;
import com.ycc.business.ektappserver.def.ErrorCode;
import com.ycc.business.ektappserver.interceptor.MainInterceptors;
import com.ycc.business.ektappserver.service.IUserService;
import com.ycc.core.jfinal.common.CommonInterceptor;
import com.ycc.core.jfinal.common.JsonLogRender;
import com.ycc.core.util.validator.MapUtil;
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
		Map lm = (Map)CommonInterceptor.PARAM.get();
		String res = userService.login(MapUtil.getString(lm,"username"),MapUtil.getString(lm,"password"));
		render(new JsonLogRender(res));
	}
	public void loginEx(){
		Map lm = (Map)CommonInterceptor.PARAM.get();
		String res = userService.loginEx(MapUtil.getInt(lm,"uid"));
		render(new JsonLogRender(res));
	}
	public void logout(){
		Map map =(Map)CommonInterceptor.PARAM.get();
		String res = userService.logout(MapUtil.getInt(map,"userid"));
		render(new JsonLogRender(res));
	}
	
	public void getAllCusLst(){
		Map map =(Map)CommonInterceptor.PARAM.get();
		String res = userService.getAllCusLst(map);
		render(new JsonLogRender(res));
	}
	public void getPopularize(){
		Map map = (Map)CommonInterceptor.PARAM.get();
		String res = userService.getPopularize(MapUtil.getInt(map, "groupid"));
		render(new JsonLogRender(res));
		
	}
	public void sayHelloFlag(){
		Map map =(Map)CommonInterceptor.PARAM.get();
		if(!map.containsKey("customerid")||!map.containsKey("userinfoid")){
			renderJson(ErrorResponse.getErrMsg(ErrorCode.PARAM_ERROR_1));
			return;
		}
		userService.sayHelloFlag(MapUtil.getInt(map,"customerid"),MapUtil.getInt(map,"userinfoid"));
		render(new JsonLogRender(SuccessResponse.getSuccessStr()));
	}
	
	public void popularizeFlag(){
		Map map = (Map)CommonInterceptor.PARAM.get();
		int pid = MapUtil.getInt(map, "popularizeid");
		int cid = MapUtil.getInt(map, "customerid");
		int uid = MapUtil.getInt(map, "userinfoid");
		if(pid==0||cid==0||uid==0){
			renderJson(ErrorResponse.getErrMsg(ErrorCode.PARAM_ERROR_1));
			return;
		}
		userService.popularizeFlag(pid, cid, uid);
		render(new JsonLogRender(SuccessResponse.getSuccessStr()));
	}
	public void saveCusInfo(){
		Map map = (Map)CommonInterceptor.PARAM.get();
		int uid = MapUtil.getInt(map, "userid");
		int gid = MapUtil.getInt(map, "groupid");
		int type = MapUtil.getInt(map, "type");
		int cid = MapUtil.getInt(map, "customerid");
		if(uid==0||gid==0){
			renderJson(ErrorResponse.getErrMsg(ErrorCode.PARAM_ERROR));
			return;
		}
		if(type==1&&cid==0){
			renderJson(ErrorResponse.getErrMsg(ErrorCode.PARAM_ERROR_1));
			return;
		}
		userService.saveCusInfo(map);
		render(new JsonLogRender(SuccessResponse.getSuccessStr()));
	}
	
	public void modifyuser(){
		Map map = (Map)CommonInterceptor.PARAM.get();
		String res = userService.modifyuser(map);
		render(new JsonLogRender(res));
	}
}
