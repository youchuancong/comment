package com.ycc.core.jfinal.db;

import lombok.Data;

import com.jfinal.plugin.druid.DruidPlugin;
import com.ycc.core.jfinal.ext.MyC3p0Plugin;
@Data
public class DbServer   implements Cloneable{
	String name;
	boolean active=true;//活跃状态
	String ipAddress;
	int port;
	String database;
	private String jdbcUrl;
	private String user;
	private String password;
	private String driverClass = "com.mysql.jdbc.Driver";
	private int maxPoolSize = 100;
	private int minPoolSize = 10;
	private int initialPoolSize = 10;
	private int maxIdleTime = 20;
	private int acquireIncrement = 2;
	public DbServer() {
	}
	/**
     * @return 创建并返回此对象的一个副本。
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        //直接调用父类的clone()方法,返回克隆副本
        return super.clone();
    }
    //String jdbcUrl, String user, String password, String driverClass, Integer maxPoolSize, Integer minPoolSize, Integer initialPoolSize, Integer maxIdleTime, Integer acquireIncrement
    public MyC3p0Plugin getC3p0Plugin(){
    	MyC3p0Plugin c = new MyC3p0Plugin(this.jdbcUrl,this.user,this.password,this.driverClass,this.maxPoolSize,this.minPoolSize,this.initialPoolSize,this.maxIdleTime,this.acquireIncrement);
    	return c;
    }
    
    public DruidPlugin getDruidPlugin(){
    	DruidPlugin c = new DruidPlugin(this.jdbcUrl,this.user,this.password,this.driverClass);
    	return c;
    }
}
