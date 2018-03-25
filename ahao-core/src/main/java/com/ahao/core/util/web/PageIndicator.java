package com.ahao.core.util.web;

public class PageIndicator {

    public static String getBootstarap(long total, int page, int size) {
        int pageRange = 2;
        int lastIndex = (int) Math.ceil(total * 1.0 / size);

        StringBuilder html = new StringBuilder("<ul class=\"pagination\">");

        // 1. 首页按钮
        html.append(createLiTag(1, page <= 1 ? "disabled" : "", "&laquo;&laquo;"));

        // 2. 上一页
        html.append(createLiTag(page - 1, page <= 1 ? "disabled" : "", "&laquo;"));

        // 3. 前2页
        for (int i = page - pageRange; i < page; i++) {
            if (i <= 0) {
                continue;
            }
            html.append(createLiTag(i, "", String.valueOf(i)));
        }
        // 4. 当前页
        html.append(createLiTag(page, "active", String.valueOf(page)));
        // 5. 后2页
        for (int i = page + 1; i <= page + pageRange; i++) {
            if (i > lastIndex) {
                continue;
            }
            html.append(createLiTag(i, "", String.valueOf(i)));
        }
        // 7. 下一页
        html.append(createLiTag(lastIndex-1, page >= lastIndex ? "disabled" : "", "&raquo;"));
        // 8. 尾页
        html.append(createLiTag(lastIndex, page <= lastIndex ? "disabled" : "", "&raquo;&raquo;"));
        html.append("</ul>");

        return html.toString();
    }

    private static String createLiTag(int page, String clazz, String text) {
        return "<li><a href=\"jump(" + page + ")\" class\"" + clazz + "\">" + text + "</a></li>";
    }
}
