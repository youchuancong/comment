package com.ycc.core.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
@Service
public class SpringAwareContainer  implements ApplicationContextAware{
	private static ApplicationContext applicationContext;
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringAwareContainer.applicationContext = applicationContext;
	}
	public static Object getBean(String id){
		return SpringAwareContainer.applicationContext.getBean(id);
	}
}
