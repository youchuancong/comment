package com.ycc.core.daogenerate;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.util.Date;

import com.ycc.core.daogenerate.bean.Table;
import com.ycc.core.daogenerate.service.IConfigService;
import com.ycc.core.daogenerate.service.IDbConnection;
import com.ycc.core.daogenerate.service.IFreeMarkerService;
import com.ycc.core.daogenerate.service.ITableParse;
import com.ycc.core.daogenerate.service.impl.DefaultConfigService;
import com.ycc.core.daogenerate.service.impl.DefaultDbConnection;
import com.ycc.core.daogenerate.service.impl.DefaultFreeMarkerService;
import com.ycc.core.daogenerate.service.impl.DefaultTableParse;

public class GenerateMain {
	private static IConfigService configService=new DefaultConfigService();
	private static IDbConnection dbConnection=new DefaultDbConnection();
	private static ITableParse tableParse=new DefaultTableParse();
	private static IFreeMarkerService freeMarkerService = new DefaultFreeMarkerService();
	public static void main(String[] args) {
		String jdbcUrl = configService.get("jdbcUrl");
		String user = configService.get("user");
		String pwd = configService.get("pwd");
		String table=configService.get("table");
		String pk=configService.get("package");
		String[] tables = table.split(",");
		Date d = new Date();
		Connection conn = dbConnection.openConnection(jdbcUrl, user, pwd);
		for(String t:tables){
			ResultSetMetaData md = dbConnection.getMetaData(conn, t);
			Table  ta = tableParse.parse(md);
			System.out.println(ta.className);
			freeMarkerService.generate("template", "build", ta,pk);
		}
		
	}
}
