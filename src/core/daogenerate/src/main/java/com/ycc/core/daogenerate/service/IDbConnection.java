package com.ycc.core.daogenerate.service;

import java.sql.Connection;
import java.sql.ResultSetMetaData;

public interface IDbConnection {
	public Connection openConnection(String jdbcUrl,String user,String pwd);

	public ResultSetMetaData getMetaData(Connection conn, String table);
	
	public void closeConnection(Connection conn);
}
