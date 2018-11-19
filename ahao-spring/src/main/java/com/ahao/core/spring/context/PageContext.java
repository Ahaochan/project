package com.ahao.core.spring.context;

import com.ahao.core.spring.util.web.SessionHelper;

import javax.servlet.http.HttpSession;

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

	public static String getOrder() {
		HttpSession session = SessionHelper.getSession();
		return SessionHelper.get(ORDER, DEFAULT_ORDER, session);
	}

	public static void setOrder(String order) {
		HttpSession session = SessionHelper.getSession();
	 	SessionHelper.set(ORDER, order, session);
	}

	public static String getSort() {
		HttpSession session = SessionHelper.getSession();
		return SessionHelper.get(SORT, DEFAULT_SORT, session);
	}

	public static void setSort(String sort) {
		HttpSession session = SessionHelper.getSession();
		SessionHelper.set(SORT, sort, session);
	}

	public static void setPageSize(int pageSize) {
		HttpSession session = SessionHelper.getSession();
		SessionHelper.set(PAGE_SIZE, pageSize, session);
	}

    public static int getPageSize() {
        HttpSession session = SessionHelper.getSession();
        return SessionHelper.get(PAGE_SIZE, DEFAULT_PAGE_SIZE, session);
    }
}
