package com.ycc.business.ektappserver.bean;

import java.util.HashMap;
import java.util.Map;

public class SuccessResponse {
public static Map<String,Object> getSuccessMap(){
	Map<String,Object> res = new HashMap<String,Object>();
	res.put("msg", "success");
	return res;
}
}
