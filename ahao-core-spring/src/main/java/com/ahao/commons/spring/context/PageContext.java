package com.ahao.commons.spring.context;

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

	private static ThreadLocal<Integer> pageSize = new ThreadLocal<>();
	private static ThreadLocal<Integer> page = new ThreadLocal<>();
	private static ThreadLocal<String> sort = new ThreadLocal<>();
	private static ThreadLocal<String> order = new ThreadLocal<>();
	static {
		pageSize.set(DEFAULT_PAGE_SIZE);
		page.set(1);
		sort.set(DEFAULT_SORT);
		order.set(DEFAULT_SORT);
	}


	public static String getOrder() {
		return order.get();
	}

	public static void setOrder(String order) {
		PageContext.order.set(order);
	}

	public static String getSort() {
		return sort.get();
	}

	public static void setSort(String sort) {
		PageContext.sort.set(sort);
	}

	public static int getPageSize() {
		return pageSize.get();
	}

	public static void setPageSize(int pageSize) {
		PageContext.pageSize.set(pageSize);
	}
}
