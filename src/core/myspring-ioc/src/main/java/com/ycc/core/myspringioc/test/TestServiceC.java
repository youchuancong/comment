package com.ycc.core.myspringioc.test;

import javax.annotation.Resource;

import com.ycc.core.myspringioc.annotation.Service;

@Service
public class TestServiceC {
	@Resource(name="testserveiceb")
public	TestServiceB testServiceB;
}
