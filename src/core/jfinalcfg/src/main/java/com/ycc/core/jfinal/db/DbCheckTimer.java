package com.ycc.core.jfinal.db;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.TimerTask;

import com.jfinal.log.Logger;
import com.ycc.core.util.validator.CollectionUtil;

public class DbCheckTimer extends TimerTask {
	private final static Logger log = Logger.getLogger(DbCheckTimer.class);
	private static boolean check(String ip,int port){
		Socket s =null;
		try {
			 s = new Socket(ip,port);
			s.close();
		} catch (UnknownHostException e) {
			log.error("database connection error", e);
			//需要发邮件告警
			return false;
		} catch (IOException e) {
			log.error("database connection error", e);
			//需要发邮件告警
			return false;
		}finally{
			if(s!=null){
				try {
					s.close();
				} catch (IOException e) {
					log.error("database close connection error", e);
				}
			}
		}
		return true;
	}
	@Override
	public void run() {
		Thread.currentThread().setName("DbCheckTimer");
		List<DbServer> servers = DbFactory.getDbServer();
		if(CollectionUtil.isNotEmpty(servers)){
			for(DbServer s:servers){
				s.active=check(s.ipAddress,s.port);
			}
		}
	}
public static void main(String[] args) {
	System.out.println(check("192.168.17.142",3307));
}
}
