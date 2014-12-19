package com.ycc.core.jfinal.db;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.ycc.core.util.config.PathEnum;
import com.ycc.core.util.config.SystemConfigUtil;
import com.ycc.core.util.validator.CollectionUtil;
import com.ycc.core.util.xml.Dom4jTool;
/**
 * 目前尚未维护数据库状态
 * @author Administrator
 *
 */
public class DbFactory {
	private final static Logger LOG = Logger.getLogger(DbFactory.class);
	private static List<DbServer> servers = null;
	private static Map<String, DbServer> pools = null;
	private static Map<String,List<String>> virture = null;
	private static Map<String,Integer> curse = null;
	private static String mysqJdbcUrl = "jdbc:mysql://%s:%d/%s?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
	private static void init(){
		servers = new LinkedList<DbServer>();
		pools = new HashMap<String,DbServer>();
		virture = new HashMap<String,List<String>>();
		curse = new HashMap<String,Integer>();
	}
	private static void parseReal(Document doc){
		List<Element> real = Dom4jTool.getNodes(doc,
				"/root/dbServer[@real='true']");
		if (CollectionUtil.isEmpty(real)) {
			LOG.error("数据库配置异常，未找到真实的数据库配置");
			return;
		}
		init();
		for (Element n : real) {
			try {
				String ps = n.attributeValue("parent");
				DbServer d = null;
				if (pools.containsKey(ps)) {
					d = (DbServer) pools.get(ps).clone();
					parse(n, d);
				} else {// 解析父节点
					Element pe = (Element) Dom4jTool.getSingleNode(doc,
							String.format("/root/dbServer[@name='%s']", ps));
					DbServer p = new DbServer();
					parse(pe, p);
					pools.put(ps, p);
					d = (DbServer) p.clone();
					parse(n,d);
				}
				if(d!=null){
					servers.add(d);
					pools.put(d.getName(), d);
				}
			} catch (CloneNotSupportedException e) {
				LOG.error("clone error", e);
			}
		}
	}
	private static void parseVirture(Document doc){
		List<Element> vl = Dom4jTool.getNodes(doc,
				"/root/dbServer[@virtual='true']");
		if (CollectionUtil.isEmpty(vl)) {
			LOG.error("数据库配置异常，未找到虚拟的数据库配置");
			return;
		}
		for (Element n : vl) {
			String vname = n.attributeValue("name");
			Iterator it = n.elementIterator("property");
			while(it.hasNext()){
				Element i = (Element) it.next();
				if(i.attributeValue("name").equals("poolNames")){
					String s = i.getTextTrim();
					String[] ss = s.split(",");
					if(ss!=null&&ss.length>0){
						List<String> rsl = new LinkedList<String>();
						for(String rs:ss){
							if(pools.containsKey(rs)){
								rsl.add(rs);
							}else{
								LOG.error("真实数据库名配置错误，name:"+rs);
							}
						}
						virture.put(vname, rsl);
						curse.put(vname, 0);
					}
				}
			}
		}
	}
	public static synchronized void loadDbServerConf(String fileName) {
		if (servers != null) {
			return;
		}
		if (fileName == null) {
			fileName = SystemConfigUtil.getPath(PathEnum.CONF) + File.separator
					+ "dbServers.xml";
		}
		Document doc = Dom4jTool.parseFromFile(fileName);
		if (doc == null) {
			LOG.error("数据库配置异常，不是合法的xml文件");
			return;
		}
		parseReal(doc);
		parseVirture(doc);
		System.out.println();
	}

	private static void parse(Element ele, DbServer s) {
		if (s == null || ele == null) {
			return;
		}
		s.setName(ele.attributeValue("name"));
		Iterator pro = ele.elementIterator("property");
		while (pro.hasNext()) {
			Element i = (Element) pro.next();
			if (i.attributeValue("name").equals("database")) {
				s.setDatabase(i.getTextTrim());
				continue;
			}
			if (i.attributeValue("name").equals("port")) {
				s.setPort(Integer.valueOf(i.getTextTrim()));
				continue;
			}
			if (i.attributeValue("name").equals("ipAddress")) {
				s.setIpAddress(i.getTextTrim());
				continue;
			}
			if (i.attributeValue("name").equals("user")) {
				s.setUser(i.getTextTrim());
				continue;
			}
			if (i.attributeValue("name").equals("password")) {
				s.setPassword(i.getTextTrim());
				continue;
			}
			if (i.attributeValue("name").equals("maxPoolSize")) {
				s.setMaxPoolSize(Integer.valueOf(i.getTextTrim()));
				continue;
			}
			if (i.attributeValue("name").equals("minPoolSize")) {
				s.setMinPoolSize(Integer.valueOf(i.getTextTrim()));
				continue;
			}
			if (i.attributeValue("name").equals("initialPoolSize")) {
				s.setInitialPoolSize(Integer.valueOf(i.getTextTrim()));
				continue;
			}
			if (i.attributeValue("name").equals("maxIdleTime")) {
				s.setMaxIdleTime(Integer.valueOf(i.getTextTrim()));
				continue;
			}
			if (i.attributeValue("name").equals("acquireIncrement")) {
				s.setAcquireIncrement(Integer.valueOf(i.getTextTrim()));
				continue;
			}
			if (i.attributeValue("name").equals("driverClass")) {
				s.setDriverClass(i.getTextTrim());
				continue;
			}
		}
		s.setJdbcUrl(mysqJdbcUrl.format(mysqJdbcUrl, s.getIpAddress(),s.getPort(),s.getDatabase()));
	}

	public static List<DbServer> getDbServer() {
		return servers;
	}

	public static DbPro getDb(String key) {
		List<String>  ds =virture.get(key);
		Integer cus = curse.get(key);
		cus++;
		if(cus>=65535){
			cus=0;
		}
		curse.put(key, cus);
		return Db.use(ds.get(cus%ds.size()));
	}

	public static void main(String[] args) {
		loadDbServerConf("D:\\src\\commponet\\runtime\\conf\\dbServers.xml");
	}
}
