package com.ycc.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
public static String getCurrentTimeStr(){
	return sf.format(new Date());
}
}
