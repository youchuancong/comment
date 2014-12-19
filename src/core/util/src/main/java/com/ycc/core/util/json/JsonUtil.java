package com.ycc.core.util.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONStringer;

public class JsonUtil {
	private static JsonConfig jf = new JsonConfig();
	static {
		jf.registerJsonValueProcessor(java.sql.Timestamp.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		jf.registerJsonValueProcessor(java.sql.Timestamp.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		jf.registerJsonValueProcessor(java.util.Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 从json字符串转换成java对象
	 * 
	 * @param jsonString
	 * @param pojoCalss
	 * @return
	 */
	public static <T> T jsonToObject(String jsonString, Class<T> pojoCalss) {
		Object pojo;
		JSONObject jsonObject = JSONObject.fromObject(jsonString, jf);
		pojo = JSONObject.toBean(jsonObject, pojoCalss);
		return (T) pojo;
	}

	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param javaObj
	 * @return
	 */
	public static String getJsonStringFromObject(Object javaObj) {
		JSONObject json;
		json = JSONObject.fromObject(javaObj, jf);
		return json.toString();
	}

	/**
	 * 从json HASH表达式中获取一个map
	 * 
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map getMapFromJsonString(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString, jf);
		Iterator keyIter = jsonObject.keys();
		String key;
		Object value;
		Map valueMap = new HashMap();
		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}
		return valueMap;
	}

	/**
	 * 从Map对象得到Json字串
	 * 
	 * @param map
	 * @return
	 */
	public static String getJsonStringFromMap(Map map) {
		JSONObject json = JSONObject.fromObject(map, jf);
		return json.toString();
	}

	/**
	 * 从json字串中得到相应java数组
	 * 
	 * @param jsonString
	 *            like "[\"李斯\",100]"
	 * @return
	 */
	public static Object[] getObjectArrayFromJsonString(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString, jf);
		return jsonArray.toArray();
	}

	public static void printMap(Map map) {
		for (Object key : map.keySet()) {
			System.out.println(key + ":" + map.get(key));
		}
	}

	public static void main(String[] args) {
		Map m = new HashMap() {
			{
				put("JSon", "HelloWorld");
				put("Flex", "Ok");
			}
		};
		System.out.println(new JSONStringer().object().key("JSON")
				.value("Hello, World!").key("Flex").value("OK").endObject()
				.toString());
	}
}
