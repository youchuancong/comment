package com.ycc.core.jfinal.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.log.Logger;
/**
 * 全局拦截器
 * @author Administrator
 *
 */
public class CommonInterceptor implements Interceptor {
	Logger log = Logger.getLogger(CommonInterceptor.class);
	public void intercept(ActionInvocation ai) {
		String key = ai.getActionKey();
		System.out.println("Before invoking " + key);
		long start = System.currentTimeMillis();
		ai.invoke();
		long end =  System.currentTimeMillis();
		log.debug("key:"+key+"  ;use:"+(end-start)+"ms");
		System.out.println("After invoking " + key);
	}

}
