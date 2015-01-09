package com.ycc.core.jfinal.common;

import com.jfinal.core.Controller;

public class IndexController extends Controller {
	public void index() {
		renderJsp("index.html");
	}
}
