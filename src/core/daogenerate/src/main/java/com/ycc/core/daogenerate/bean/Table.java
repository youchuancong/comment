package com.ycc.core.daogenerate.bean;

import java.util.List;

import lombok.Data;
@Data
public class Table {
public String tableName;
public String className;
public List<Column> columns;

}
