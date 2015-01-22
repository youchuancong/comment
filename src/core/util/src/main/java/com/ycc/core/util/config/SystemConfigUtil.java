package com.ycc.core.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import com.ycc.core.util.validator.StringUtil;

public class SystemConfigUtil {
	private  static Properties conf =null;
	private static String confFileName = "conf.txt";
	public static void loadConfFile(String fileName) {
		if(conf!=null){
			return;
		}
		conf = new Properties();
		if(StringUtil.isNotEmpty(fileName)){
			confFileName=fileName;
		}
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileName); // properties.load(Prop.class.getResourceAsStream(fileName));
			if (inputStream == null)
				throw new IllegalArgumentException(
						"Properties file not found : " + fileName);
			conf.load(new InputStreamReader(inputStream, "utf-8"));
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static String get(String key) {
		if(conf!=null){
			return conf.getProperty(key);
		}
		return null;
	}

	public static String getWebHome() {
		return System.getProperty("web.home");
	}
	public static String getConFileName(){
		return confFileName;
	}
	public static String getPath(PathEnum path) {
		switch (path) {
		case LOG:
			return getWebHome() + File.separator + "log";
		case CONF:
			return getWebHome() + File.separator + "conf";
		case WEBAPPS:
			return getWebHome() + File.separator + "webapps";
		case DEPLOY:
			return getWebHome() + File.separator + "deploy";
		case SESSION:
			return getWebHome() + File.separator + "session";
		default:
			return null;
		}
	}
	public static void main(String[] args) {
		Date b = new Date();
		System.out.println(b.getTime());
	}
}
