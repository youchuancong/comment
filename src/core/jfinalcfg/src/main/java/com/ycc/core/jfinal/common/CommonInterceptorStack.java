package com.ycc.core.jfinal.common;

import com.jfinal.aop.InterceptorStack;
import com.ycc.core.myspringioc.MyIocInterceptor;

public class CommonInterceptorStack extends InterceptorStack {

	@Override
	public void config() {
		addInterceptors(new CommonInterceptor(),new MyIocInterceptor());
	}

}
