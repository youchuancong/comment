package com.ycc.core.daogenerate.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ycc.core.daogenerate.bean.Table;
import com.ycc.core.daogenerate.service.IFreeMarkerService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class DefaultFreeMarkerService implements IFreeMarkerService {
	private static Configuration cfg = new Configuration();
	static {
		try {
			cfg.setDirectoryForTemplateLoading(new File("template"));
			cfg.setDefaultEncoding("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generate(String templatedir, String outputdir, Table table,String pk) {
		Map<String,Object> m = new HashMap<String,Object>();
		try {
			Template tm = cfg.getTemplate("module.ftl");
			m.put("table", table);
			m.put("package", pk);
			FileWriter fos = new FileWriter("build"+File.separator+table.className+".java");
			tm.process(m, fos);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
	
		try {
			/*File ffff = new File("bb"+File.separator+"aaaaaaaaaa.txt");
			ffff.createNewFile();*/
			DefaultFreeMarkerService s = new DefaultFreeMarkerService();
			s.generate(null,null,null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
