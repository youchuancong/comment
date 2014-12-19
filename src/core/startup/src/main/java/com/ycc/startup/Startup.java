package com.ycc.startup;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import com.jfinal.core.Const;
import com.jfinal.kit.FileKit;
import com.ycc.core.util.config.PathEnum;
import com.ycc.core.util.config.SystemConfigUtil;
import com.ycc.core.util.validator.StringUtil;

public class Startup {
	private String webAppDir = "D:\\src\\commponet\\runtime\\webapps";
	private int port = 81;
	private int maxThreads=500;
	private int	minThreads=10;
	private int	acceptors=8;
	private String context = "/";
	private Server server;
	private WebAppContext webApp;

	/**
	 * 带1个参数 配置文件路径(缺省时为conf/conf.txt)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String conf = null;
		if (args.length > 0) {
			conf = args[0];
		}
		Startup server = new Startup();
		server.loadConf(conf);
		server.init(server.port,SystemConfigUtil.getPath(PathEnum.WEBAPPS));
		// 启动server
		server.doStart();
	}

	public void init(int port, String webAppDir) {
		this.port = port;
		this.webAppDir = webAppDir;
	}

	public void loadConf(String cffile) {
		PropertyConfigurator.configureAndWatch(SystemConfigUtil.getPath(PathEnum.CONF) + File.separator
				+ "log4j.properties", 60000);
		String conf = null;
		if (StringUtil.isNotEmpty(cffile)) {
			conf = cffile;
		} else {
			conf = SystemConfigUtil.getPath(PathEnum.CONF) + File.separator
					+ "conf.txt";
		}
		SystemConfigUtil.loadConfFile(conf);
		String port = SystemConfigUtil.get("port");
		if(StringUtil.isNotEmpty(port)){
			this.port = Integer.valueOf(port);
		}
		String context = SystemConfigUtil.get("context");
		if(StringUtil.isNotEmpty(context)){
			this.context=context;
		}
		if(StringUtil.isNotEmpty(SystemConfigUtil.get("maxThreads"))){
			this.maxThreads=Integer.valueOf(SystemConfigUtil.get("maxThreads"));
		}
		if(StringUtil.isNotEmpty(SystemConfigUtil.get("minThreads"))){
			this.minThreads=Integer.valueOf(SystemConfigUtil.get("minThreads"));
		}
		if(StringUtil.isNotEmpty(SystemConfigUtil.get("acceptors"))){
			this.acceptors=Integer.valueOf(SystemConfigUtil.get("acceptors"));
		}
	}

	public void doStart() {
		if (!available(port))
			throw new IllegalStateException("port: " + port
					+ " already in use!");

		deleteSessionData();

		System.out.println("Starting JFinal " + Const.JFINAL_VERSION);
		server = new Server();
		QueuedThreadPool threadPool = new QueuedThreadPool();
	     threadPool.setMinThreads(minThreads);
	     threadPool.setMaxThreads(maxThreads);
	     server.setThreadPool(threadPool);
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		//每个请求被accept前允许等待的连接数
	     connector.setAcceptQueueSize(50);
	     //同事监听read事件的线程数
	     connector.setAcceptors(acceptors);
	     //连接最大空闲时间，默认是200000，-1表示一直连接
	     connector.setMaxIdleTime(200000);
		server.addConnector(connector);
		webApp = new WebAppContext();
		webApp.setContextPath(context);
		webApp.setResourceBase(webAppDir); // webApp.setWar(webAppDir);
		webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed",
				"false");
		webApp.setInitParameter(
				"org.eclipse.jetty.servlet.Default.useFileMappedBuffer",
				"false"); // webApp.setInitParams(Collections.singletonMap("org.mortbay.jetty.servlet.Default.useFileMappedBuffer",
							// "false"));
		persistSession(webApp);
		server.setHandler(webApp);
		try {
			System.out.println("Starting web server on port: " + port);
			server.start();
			System.out
					.println("Starting Complete. Welcome To The JFinal World :)");
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
		return;
	}

	private void deleteSessionData() {
		try {
			FileKit.delete(new File(getStoreDir()));
		} catch (Exception e) {
		}
	}

	private String getStoreDir() {
		String storeDir = SystemConfigUtil.getPath(PathEnum.SESSION)
				+ File.separator + "session_" + context;
		if ("\\".equals(File.separator))
			storeDir = storeDir.replaceAll("/", "\\\\");
		return storeDir;
	}

	private void persistSession(WebAppContext webApp) {
		String storeDir = getStoreDir();

		SessionManager sm = webApp.getSessionHandler().getSessionManager();
		if (sm instanceof HashSessionManager) {
			((HashSessionManager) sm).setStoreDirectory(new File(storeDir));
			return;
		}

		HashSessionManager hsm = new HashSessionManager();
		hsm.setStoreDirectory(new File(storeDir));
		SessionHandler sh = new SessionHandler();
		sh.setSessionManager(hsm);
		webApp.setSessionHandler(sh);
	}

	private static boolean available(int port) {
		if (port <= 0) {
			throw new IllegalArgumentException("Invalid start port: " + port);
		}

		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					// should not be thrown, just detect port available.
				}
			}
		}
		return false;
	}
}
