package com.ycc.core.util.validator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	public static String getMD5(String value){
		if(isEmpty(value)){
			return null;
		}
		String str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(value.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return str;
	}
}
