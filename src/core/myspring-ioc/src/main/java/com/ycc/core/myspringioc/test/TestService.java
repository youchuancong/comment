package com.ycc.core.myspringioc.test;

import com.ycc.core.myspringioc.annotation.Service;
import javax.annotation.Resource;
@Service("testserveice")
public class TestService {
	@Resource(name="TestServiceC")
	public TestServiceC TestServiceC;
}
