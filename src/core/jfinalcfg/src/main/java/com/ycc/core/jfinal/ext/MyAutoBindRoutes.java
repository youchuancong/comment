package com.ycc.core.jfinal.ext;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.ycc.core.util.config.PathEnum;
import com.ycc.core.util.config.SystemConfigUtil;
import com.ycc.core.util.search.ClassSearch;

public class MyAutoBindRoutes extends AutoBindRoutes {
	private boolean autoScan = true;
	private List<Class<? extends Controller>> excludeClasses = Lists
			.newArrayList();
	protected final Logger logger = Logger.getLogger(getClass());
	private String suffix = "Controller";

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void config() {
		List<Class> controllerClasses = ClassSearch
				.anno(ControllerBind.class)
				.libDir(SystemConfigUtil.getPath(PathEnum.DEPLOY))
				.includeAllJarsInLib(true)
				.classpath(SystemConfigUtil.getPath(PathEnum.DEPLOY)).search();
		ControllerBind controllerBind = null;
		for (Class controller : controllerClasses) {
			if (excludeClasses.contains(controller)) {
				continue;
			}
			controllerBind = (ControllerBind) controller
					.getAnnotation(ControllerBind.class);
			if (controllerBind == null) {
				if (!autoScan) {
					continue;
				}
				this.add(controllerKey(controller), controller);
				logger.debug("routes.add(" + controllerKey(controller) + ", "
						+ controller.getName() + ")");
			} else if (StrKit.isBlank(controllerBind.viewPath())) {
				this.add(controllerBind.controllerKey(), controller);
				logger.debug("routes.add(" + controllerBind.controllerKey()
						+ ", " + controller.getName() + ")");
			} else {
				this.add(controllerBind.controllerKey(), controller,
						controllerBind.viewPath());
				logger.debug("routes.add(" + controllerBind.controllerKey()
						+ ", " + controller + "," + controllerBind.viewPath()
						+ ")");
			}
		}
	}

	private String controllerKey(Class<Controller> clazz) {
		Preconditions.checkArgument(clazz.getSimpleName().endsWith(suffix),
				" does not has a @ControllerBind annotation and it's name is not end with "
						+ suffix);
		String controllerKey = "/"
				+ StrKit.firstCharToLowerCase(clazz.getSimpleName());
		controllerKey = controllerKey.substring(0,
				controllerKey.indexOf(suffix));
		return controllerKey;
	}
}
