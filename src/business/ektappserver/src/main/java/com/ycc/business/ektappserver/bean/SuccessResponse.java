package com.ycc.business.ektappserver.bean;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jfinal.plugin.activerecord.Record;
import com.ycc.core.util.json.JsonUtil;
import com.ycc.core.util.validator.CollectionUtil;

public class SuccessResponse {
	public static Map<String, Object> getSuccessMap() {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("msg", "success");
		return res;
	}
	public static String getSuccessArray(List<Record> list){
		if(CollectionUtil.isEmpty(list)){
			return "{\"msg\":\"success\",\"totle\":0,\"rows\":\"[]\"}";
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("msg", "success");
		map.put("totle", list.size());
		
		Object[] ro = new Object[list.size()];
		for(int i=0;i<list.size();i++){
			ro[i]=list.get(i).getColumns();
		}
		map.put("rows",ro);
		return JsonUtil.getJsonStringFromMap(map);
	}
	public static String getSuccessStr() {
		return "{\"msg\":\"success\"}";
	}
	public static void main(String[] args) {
		List<Record> list = new LinkedList<Record>();
		Record a = new Record();
		a.set("name", "a");
		a.set("age", 10);
		list.add(a);
		Record b = new Record();
		b.set("name", "b");
		b.set("age", 10);
		list.add(b);
		System.out.println(getSuccessArray(list));
	}
}
