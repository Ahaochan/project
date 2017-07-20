package com.ahao.context;

/**
 * 分页参数
 * 
 * @author Ahaochan
 */
public class PageContext {
	public static final String PAGE = "page";
	public static final String PAGE_SIZE = "pageSize";
	public static final String ORDER = "order";
	public static final String SORT = "sort";

	public static final int DEFAULT_PAGE_SIZE = 25;
	public static final String DEFAULT_ORDER = "asc";
	public static final String DEFAULT_SORT = "id";

	/**
	 * 分页大小
	 */
	private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();
	/**
	 * 升序还是降序
	 */
	private static ThreadLocal<String> order = new ThreadLocal<String>();
	/**
	 * 根据那个字段排序
	 */
	private static ThreadLocal<String> sort = new ThreadLocal<String>();

	public static String getOrder() {
		return order.get();
	}

	public static void setOrder(String _order) {
		order.set(_order);
	}

	public static void removeOrder() {
		order.remove();
	}

	public static String getSort() {
		return sort.get();
	}

	public static void setSort(String _sort) {
		sort.set(_sort);
	}

	public static void removeSort() {
		sort.remove();
	}

	public static void setPageSize(int _pageSize) {
		pageSize.set(_pageSize);
	}

	public static int getPageSize() {
		return pageSize.get();
	}

	public static void removePageSize() {
		pageSize.remove();
	}
}
