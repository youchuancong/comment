package com.ycc.core.daogenerate.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.ycc.core.daogenerate.service.IConfigService;

public class DefaultConfigService implements IConfigService {
	private static Properties conf = null;
	private static String confFileName = "conf.txt";

	public static void loadConfFile() {
		if (conf != null) {
			return;
		}
		conf = new Properties();
		InputStream inputStream = null;
		try {
			//inputStream = DefaultConfigService.class.getResourceAsStream(confFileName); // properties.load(Prop.class.getResourceAsStream(fileName));
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(confFileName); // properties.load(Prop.class.getResourceAsStream(fileName));
			if (inputStream == null)
				throw new IllegalArgumentException(
						"Properties file not found : " + confFileName);
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

	public String get(String key) {
		if (conf == null) {
			loadConfFile();
		}
		return conf.getProperty(key);
	}

}
