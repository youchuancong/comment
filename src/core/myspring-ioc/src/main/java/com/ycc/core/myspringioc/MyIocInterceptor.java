package com.ycc.core.myspringioc;

import java.lang.reflect.Field;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.spring.Inject;

public class MyIocInterceptor implements Interceptor {
	static MyApplicatocContext ctx;
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		Field[] fields = controller.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object bean = null;
			if (field.isAnnotationPresent(Inject.BY_NAME.class))
				bean = ctx.getBean(field.getName().toLowerCase());
			else
				continue ;
			try {
				if (bean != null) {
					field.setAccessible(true);
					field.set(controller, bean);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		ai.invoke();

	}

}
