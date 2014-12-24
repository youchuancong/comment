package com.ycc.core.daogenerate.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.ycc.core.daogenerate.service.IDbConnection;

public class DefaultDbConnection implements IDbConnection {

	public List<ResultSetMetaData> getMetaData(String jdbcUrl, String user,
			String pwd, String[] tables) {
		String sql="select * from %s where 1=2";
		 List<ResultSetMetaData> res = new  LinkedList<ResultSetMetaData>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection con=null;
		try {
			 con = DriverManager.getConnection(jdbcUrl+"&user="+user+"&password="+pwd);
			Statement st = con.createStatement();
			for(String t:tables){
				ResultSet rs = st.executeQuery(String.format(sql, t));
				res.add(rs.getMetaData());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(con!=null){
			/*	try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		}
		
		return res;
	}

	public Connection openConnection(String jdbcUrl, String user, String pwd) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection con=null;
		try {
			 con = DriverManager.getConnection(jdbcUrl+"&user="+user+"&password="+pwd);
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}

	public ResultSetMetaData getMetaData(Connection conn,String table) {
		String sql="select * from %s where 1=2";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(String.format(sql, table));
			return rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void closeConnection(Connection conn) {
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
