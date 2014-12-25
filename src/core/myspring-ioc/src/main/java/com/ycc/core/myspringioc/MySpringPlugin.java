package com.ycc.core.myspringioc;

import com.jfinal.plugin.IPlugin;

public class MySpringPlugin implements IPlugin {
	private MyApplicatocContext ctx;
	public MySpringPlugin(){
	}
	public boolean start() {
		ctx=new MyApplicatocContext();
		MyIocInterceptor.ctx=ctx;
		return true;
	}

	public boolean stop() {
		return false;
	}

}
