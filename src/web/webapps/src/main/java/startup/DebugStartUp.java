package startup;

import com.ycc.startup.Startup;

public class DebugStartUp {

	public static void main(String[] args) {
		System.setProperty("web.home", "D:\\src\\commponet\\runtime");
	//	JFinal.start("src/main/webapp", 80, "/", 5);
		
		Startup s = new Startup();
		s.loadConf(null);
		s.init(80, "src/main/webapp");
		s.doStart();
	}

}
