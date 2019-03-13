package com.ahao.jdbc;

/**
 * 当前数据源 key
 */
public class DataSourceContextHolder {
	private static final ThreadLocal<String> LOCAL = new ThreadLocal<String>();

	public static final String MASTER = "master";
	public static final String SLAVE  = "slave";

	public static boolean isMaster() {
		return MASTER.equals(LOCAL.get());
	}

	public static void master() {
		LOCAL.set(MASTER);
	}

	public static void slave() {
		LOCAL.set(SLAVE);
	}

	public static String get() {
		return LOCAL.get();
	}

	public static void clear() {
		LOCAL.remove();
	}
}
