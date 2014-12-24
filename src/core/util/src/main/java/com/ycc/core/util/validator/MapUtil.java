package com.ycc.core.util.validator;

import java.util.Map;

public class MapUtil {
public static int getInt(Map map,Object key){
	return getInt(map,key,0);
}
/**
 * 
 * @param map
 * @param key
 * @param df 默认值
 * @return
 */
public static int getInt(Map map,Object key,int df){
	if(map==null||key==null){
		return df;
	}
	if(!map.containsKey(key)){
		return df;
	}
	Object v = map.get(key);
	try{
		return (Integer)v;
	}catch(Exception e){
		try {
			return Integer.valueOf((String)v);
		} catch (NumberFormatException e1) {
			return df;
		}
	}
}
public static String getString(Map map,Object key){
	return getString(map,key,null);
}
public static String getString(Map map,Object key,String df){
	if(map==null||key==null){
		return df;
	}
	if(!map.containsKey(key)){
		return df;
	}
	Object v = map.get(key);
	try{
		return (String)v;
	}catch(Exception e){
		e.printStackTrace();
	}
	return df;
}
}
