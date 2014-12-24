package com.ycc.core.jfinal.db;

import java.lang.reflect.Field;

import com.jfinal.plugin.activerecord.Record;
import com.ycc.core.util.db.RColumn;
import com.ycc.core.util.db.RTable;

public class DbUtil {
	public static <T extends Module> T findById(String dbkey, int id,
			Class<T> clazz) {
		try {
			RTable table = clazz.getAnnotation(RTable.class);
			Record r = DbFactory.getDb(dbkey).findById(table.value(), id);
			return recordToModle(r, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean insert(String dbkey, Module modle) {
		Record r = modleToRecord(modle);
		RTable table = modle.getClass().getAnnotation(RTable.class);
		DbFactory.getDb(dbkey).save(table.value(), r);
		return true;
	}

	public static boolean update(String dbKey, Module modle) {
		Record r = modleToRecord(modle);
		RTable table = modle.getClass().getAnnotation(RTable.class);
		DbFactory.getDb(dbKey).update(table.value(), r);
		return true;
	}

	private static Record modleToRecord(Module modle) {
		Field[] fields = modle.getClass().getFields();
		Record r = new Record();
		for (Field f : fields) {
			if (f.isAnnotationPresent(RColumn.class)) {
				RColumn c = f.getAnnotation(RColumn.class);
				try {
					Object v = f.get(modle);
					if (v != null) {
						r.set(c.value(), f.get(modle));
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return r;
	}

	private static <T extends Module> T recordToModle(Record record,
			Class<T> clazz) {
		try {
			T res = clazz.newInstance();
			Field[] fields = clazz.getFields();
			for (Field f : fields) {
				if (f.isAnnotationPresent(RColumn.class)) {
					RColumn c = f.getAnnotation(RColumn.class);
					f.set(res, record.get(c.value()));
				}
			}
			return res;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T recordToObj(Record r, Class<T> clazz) {
		try {
			T res = clazz.newInstance();
			Field[] fields = clazz.getFields();
			for (Field f : fields) {
				f.set(res, r.get(f.getName()));
			}
			return res;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		Record r = new Record();
		r.set("id", 1);
		r.set("name", "aaa");
		TableTest t = recordToModle(r, TableTest.class);
		System.out.println();
	}
}
