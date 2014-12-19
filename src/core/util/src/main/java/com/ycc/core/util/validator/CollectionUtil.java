package com.ycc.core.util.validator;

import java.util.Collection;

public class CollectionUtil {
	public static boolean isEmpty(Collection col) {
		if (col == null || col.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotEmpty(Collection col) {
		return !isEmpty(col);
	}

}
