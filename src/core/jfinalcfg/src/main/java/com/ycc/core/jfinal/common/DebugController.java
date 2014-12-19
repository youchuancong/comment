package com.ycc.core.jfinal.common;

import com.jfinal.core.Controller;

public class DebugController  extends Controller{
public void index(){
	renderJsp("debuginfo.jsp");
}
}
