package com.ycc.business.ektappserver.interceptor;

import com.jfinal.aop.InterceptorStack;

public class MainInterceptors extends InterceptorStack {

	@Override
	public void config() {
		addInterceptors(new AppServerInterceptor());
	}

}
