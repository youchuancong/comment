package com.ycc.business.ektappserver.bean;

public class ErrorResponse {
	private static String str = "{\"msg\":\"error\",\"errorCode\":\"%s\"}";

	public static String getErrMsg(String msg) {
		return String.format(str, msg);
	}
}
