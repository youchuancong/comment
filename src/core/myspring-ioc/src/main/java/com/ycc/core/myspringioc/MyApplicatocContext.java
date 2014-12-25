package com.ycc.core.myspringioc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ycc.core.myspringioc.annotation.Service;
import com.ycc.core.util.config.PathEnum;
import com.ycc.core.util.config.SystemConfigUtil;
import com.ycc.core.util.search.ClassSearch;
import com.ycc.core.util.validator.CollectionUtil;
import com.ycc.core.util.validator.StringUtil;

public class MyApplicatocContext {
	private static final Log log = LogFactory.getLog(MyApplicatocContext.class);
	private  Map<String,Object> beans = new HashMap<String,Object>();
	private Map<String,Class> clazzs = new HashMap<String,Class>();
	private void initClass(List<Class> services ){
		for(Class clazz:services){
			Service s = (Service) clazz.getAnnotation(Service.class);
			if(s!=null){
				String name = s.value().toLowerCase();
				if(StringUtil.isEmpty(name)){
					name=clazz.getSimpleName().toLowerCase();
				}
				if(clazzs.containsKey(name)){
					log.error(name+" is aready be used");
				}else{
					clazzs.put(name, clazz);
				}
			}
		}
	}
	private void initBean(Class clazz){
		Service s =  (Service) clazz.getAnnotation(Service.class);
		String bn = s.value().toLowerCase();
		if(StringUtil.isEmpty(bn)){
			bn=clazz.getSimpleName().toLowerCase();
		}
		if(s!=null){
			if(beans.containsKey(s.value().toLowerCase())){
				return;
			}else{
				try {
					Object obj = clazz.newInstance();
					Field[] fields =  clazz.getFields();
					if(fields!=null&&fields.length>0){
						for(Field f:fields){
							Resource rs = f.getAnnotation(Resource.class);
							if(rs!=null){
								String name = rs.name().toLowerCase();
								if(StringUtil.isEmpty(name)){
									name=f.getName().toLowerCase();
								}
								initBean(clazzs.get(name));
								if(beans.containsKey(name)){//set属性值
									f.setAccessible(true);
									f.set(obj, beans.get(name));
								}else{
									log.error(clazz.getCanonicalName()+"  init error,"+name+"  not found");
								}
							}
						}
					}
					beans.put(bn, obj);
				} catch (InstantiationException e) {
					log.error("", e);
				} catch (IllegalAccessException e) {
					log.error("", e);
				}
			}
		}
	}
	private void scan(){
		try {
			List<Class> services = ClassSearch
					.anno(Service.class)
					.libDir(SystemConfigUtil.getPath(PathEnum.DEPLOY))
					.includeAllJarsInLib(true)
					.classpath(SystemConfigUtil.getPath(PathEnum.DEPLOY)).search();
			//初始化
			if(CollectionUtil.isNotEmpty(services)){
				initClass(services);
				for(Class s:services){
					initBean(s);
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}
	public MyApplicatocContext() {
		log.debug("begin scan ,scanBase:");
		scan();
		log.debug("end scan ,scanBase:");
	}
	public Object getBean(String beanName){
		return beans.get(beanName);
	}
	public static void main(String[] args) {
		//new MyApplicatocContext("com.ycc");
		String t="com.ycc";
		System.out.println(t.replaceAll("\\.", "\\\\"));
	}

}
