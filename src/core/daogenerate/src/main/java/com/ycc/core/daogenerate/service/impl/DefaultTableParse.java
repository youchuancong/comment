package com.ycc.core.daogenerate.service.impl;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.jfinal.plugin.activerecord.ModelBuilder;
import com.ycc.core.daogenerate.bean.Column;
import com.ycc.core.daogenerate.bean.Table;
import com.ycc.core.daogenerate.service.ITableParse;
import com.ycc.core.util.validator.StringUtil;

public class DefaultTableParse implements ITableParse {

	public Table parse(ResultSetMetaData md) {
		Table t = new Table();
		List<Column> cs = new LinkedList<Column>();
		t.columns=cs;
		try {
			t.tableName=md.getTableName(1);
			t.className=StringUtil.toUpperCaseFirstOne(t.tableName);
			for(int i=1;i<=md.getColumnCount();i++){
				Column c = new Column();
				/**
				 	if (types[i] < Types.BLOB)
					value = rs.getObject(i);
				else if (types[i] == Types.CLOB)
					value = ModelBuilder.handleClob(rs.getClob(i));
				else if (types[i] == Types.NCLOB)
					value = ModelBuilder.handleClob(rs.getNClob(i));
				else if (types[i] == Types.BLOB)
					value = ModelBuilder.handleBlob(rs.getBlob(i));
				else
					value = rs.getObject(i);
				 */
				c.name=md.getColumnName(i);
				c.proName=c.name;
				c.sqlType=md.getColumnType(i);
			 if(c.sqlType==Types.CLOB||c.sqlType==Types.NCLOB){
					c.javaType="String";
				}else if(c.sqlType==Types.BLOB||c.sqlType==Types.VARBINARY||c.sqlType==Types.LONGVARBINARY){
					c.javaType="byte[]";
				}else if(c.sqlType==Types.TIMESTAMP){
					c.javaType="Date";
				}
				else{
					c.fullJavaType=md.getColumnClassName(i);
					c.javaType=c.fullJavaType.substring(c.fullJavaType.lastIndexOf(".")+1);
				}
				cs.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}

}
