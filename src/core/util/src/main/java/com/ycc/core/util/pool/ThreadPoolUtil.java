package com.ycc.core.util.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
	private static int corePoolSize=10;
	private static  int maximumPoolSize=500;
	private static  long keepAliveTime=120;
	private static   int queue=300;
	private static final ExecutorService POOL = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(queue));
	public static void execute(Runnable thread){
		POOL.submit(thread);
	}
}
