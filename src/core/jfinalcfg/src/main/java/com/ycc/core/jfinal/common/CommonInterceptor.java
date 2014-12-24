package com.ycc.core.jfinal.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
/**
 * 全局拦截器
 * @author Administrator
 *
 */
public class CommonInterceptor implements Interceptor {
	public final static ThreadLocal PARAM = new ThreadLocal();
	public final static ThreadLocal STARTTIME = new ThreadLocal();
	public final static ThreadLocal ACTIONKEY = new ThreadLocal();
	public final static ThreadLocal REQUEST_STR = new ThreadLocal();

	public void intercept(ActionInvocation ai) {
		String key = ai.getActionKey();
		ACTIONKEY.set(key);
		long start = System.currentTimeMillis();
		STARTTIME.set(start);
		ai.invoke();
	}

}
