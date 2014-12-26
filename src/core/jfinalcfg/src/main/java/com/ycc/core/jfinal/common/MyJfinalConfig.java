package com.ycc.core.jfinal.common;

import java.io.File;
import java.util.List;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.ycc.core.jfinal.db.DbFactory;
import com.ycc.core.jfinal.db.DbServer;
import com.ycc.core.jfinal.ext.MyAutoBindRoutes;
import com.ycc.core.myspringioc.MySpringPlugin;
import com.ycc.core.util.config.PathEnum;
import com.ycc.core.util.config.SystemConfigUtil;
import com.ycc.core.util.validator.CollectionUtil;

public class MyJfinalConfig extends JFinalConfig {
	private final static Logger LOG = Logger.getLogger(MyJfinalConfig.class);
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile(new File(SystemConfigUtil.getConFileName()));
		me.setDevMode(getPropertyToBoolean("devMode", false));
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		//me.add(new AutoBindRoutes().includeAllJarsInLib(true));
		me.add(new MyAutoBindRoutes());
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		//me.add(new SpringPlugin("file:"+SystemConfigUtil.getPath(PathEnum.WEBAPPS)+File.separator+"WEB-INF"+File.separator+"applicationContext.xml"));
		//me.add(new SpringPlugin(SystemConfigUtil.getPath(PathEnum.CONF)+File.separator+File.separator+"applicationContext.xml"));
		//me.add(new SpringPlugin());
		me.add(new MySpringPlugin());
		DbFactory.loadDbServerConf(SystemConfigUtil.getPath(PathEnum.CONF)+File.separator+"dbServers.xml");
		// 配置C3p0数据库连接池插件
		List<DbServer>  servers = DbFactory.getDbServer();
		if(CollectionUtil.isEmpty(servers)){
			LOG.error("db server conf is null");
			return;
		}
		for(DbServer db:servers){
			C3p0Plugin cp = db.getC3p0Plugin();
			me.add(cp);
			ActiveRecordPlugin arp = new ActiveRecordPlugin(db.getName(),cp);
			me.add(arp);
		}
		
		EhCachePlugin ecp = new EhCachePlugin(SystemConfigUtil.getPath(PathEnum.CONF)+File.separator+"ehcache.xml");
		me.add(ecp);
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		me.add(new CommonInterceptorStack());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}
	
	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/", 5);
	}

}
