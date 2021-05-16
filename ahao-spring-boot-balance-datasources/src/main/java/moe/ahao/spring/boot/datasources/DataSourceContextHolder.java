package moe.ahao.spring.boot.datasources;

/**
 * 当前数据源 key
 */
public class DataSourceContextHolder {
	private static final ThreadLocal<String> LOCAL = new ThreadLocal<String>();

	public static void set(String profile) {
	    LOCAL.set(profile);
    }

	public static String get() {
		return LOCAL.get();
	}

	public static void clear() {
		LOCAL.remove();
	}
}
