package com.ycc.core.daogenerate.service;

import java.sql.ResultSetMetaData;

import com.ycc.core.daogenerate.bean.Table;

public interface ITableParse {
	Table parse(ResultSetMetaData md);
}
