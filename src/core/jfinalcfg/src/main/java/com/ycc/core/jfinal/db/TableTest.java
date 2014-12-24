package com.ycc.core.jfinal.db;

import com.ycc.core.util.db.RColumn;

public class TableTest implements Module {
	@RColumn("id")
	public int id;
	@RColumn("name")
	public String name;
}
