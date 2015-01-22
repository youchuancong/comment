package com.ycc.business.ektappserver.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.log.Logger;
import com.ycc.core.jfinal.common.CommonInterceptor;
import com.ycc.core.util.def.CommonDef;
import com.ycc.core.util.json.JsonUtil;
import com.ycc.core.util.validator.StringUtil;

/**
 * appServer拦截器
 * 
 * @author Administrator
 * 
 */
public class AppServerInterceptor implements Interceptor {
	Logger log = Logger.getLogger(AppServerInterceptor.class);

	private void initParam(ActionInvocation ai) {
		try {
			HttpServletRequest request = ai.getController().getRequest();
			Map map = new HashMap();
			String type=request.getContentType();
			if(StringUtil.isNotEmpty(type)&&type.startsWith("multipart/form-data")){//文件上传类型的请求
				map=request.getParameterMap();
				CommonInterceptor.REQUEST_STR.set(JsonUtil.getJsonStringFromMap(map));
			}else{
				ServletInputStream ins = request.getInputStream();
				String str = IOUtils.toString(ins,CommonDef.CHARSET);
				if(StringUtil.isNotEmpty(str)){
					 map = JsonUtil.getMapFromJsonString(str);
				}
				CommonInterceptor.REQUEST_STR.set(str);
			}
			CommonInterceptor.PARAM.set(map);
			//request.setAttribute(AppServerDef.AGENT_PARAM, str);
		} catch (IOException e) {
			log.error("initParam error", e);
		}
	}

	public void intercept(ActionInvocation ai) {
		// 1、从流中去除参数
		// 2、并将参数字符串放置在"agentParam"属性中
		initParam(ai);
		// 3、进行token校验

		try {
			ai.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
