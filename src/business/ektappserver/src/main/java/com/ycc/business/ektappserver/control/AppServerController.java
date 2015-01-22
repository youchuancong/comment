package com.ycc.business.ektappserver.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.spring.Inject;
import com.jfinal.upload.UploadFile;
import com.ycc.business.ektappserver.bean.ErrorResponse;
import com.ycc.business.ektappserver.bean.FileInfo;
import com.ycc.business.ektappserver.bean.SuccessResponse;
import com.ycc.business.ektappserver.def.ErrorCode;
import com.ycc.business.ektappserver.interceptor.MainInterceptors;
import com.ycc.business.ektappserver.service.IUserService;
import com.ycc.core.jfinal.common.CommonInterceptor;
import com.ycc.core.jfinal.common.JsonLogRender;
import com.ycc.core.util.config.SystemConfigUtil;
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
	
	public void uploadlog(){
		UploadFile log = getFile();
		render(new JsonLogRender(SuccessResponse.getSuccessStr()));
	}
	public void filelist(){
		List<FileInfo> list = new LinkedList<FileInfo>();
		File uploaddir = new File(SystemConfigUtil.getWebHome()+File.separator+"upload");
		if(uploaddir.isDirectory()){
			File[] fs = uploaddir.listFiles();
			for(File f:fs){
				list.add(new FileInfo(f.getName().split("\\.")[0],f.length(),new Date(f.lastModified())));
			}
		}
		Collections.sort(list, new Comparator<FileInfo>(){

			public int compare(FileInfo o1, FileInfo o2) {
				return (int)(o2.getTime().getTime()-o1.getTime().getTime());
			}
			
		} );
		setAttr("files", list);
		renderFreeMarker("UploadFileList.html");
	}
	public void downloadfile(){
		String name = getPara(0);
		renderFile(new File(SystemConfigUtil.getWebHome()+File.separator+"upload"+File.separator+name+".log"));
	}
	
	/**
	 * 查看服务器日志（只查看最新的10000个字符内容）
	 */
	public void serverlog(){
		String logfile=getLogFile();
		int size=100000;
		try {
			RandomAccessFile rf = new RandomAccessFile(logfile, "r");
			if(rf.length()>size){
				rf.seek(rf.length()-size);
			}
			byte[] bytes = new byte[size];
			int rsize = rf.read(bytes, 0, size);
			String context = new String(bytes,0,rsize,"utf-8");
			setAttr("logcontext", context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		renderFreeMarker("serverlog.html");
	}
	public void downloadlog(){
		String logfile=getLogFile();
		renderFile(new File(logfile));
	}
	/**
	 * 优先取startup.log日志文件，该文件不存在时则取output.log
	 * @return
	 */
	private String getLogFile(){
		String logpath=SystemConfigUtil.getWebHome()+File.separator+"log";
		String logfile="startup.log";
		File f = new File(logpath+File.separator+logfile);
		if(!f.exists()){
			logfile="output.log";
		}
		return logpath+File.separator+logfile;
	}
}
