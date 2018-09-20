package com.ahao.commons.html.page;

import com.ahao.commons.util.lang.StringHelper;
import com.ahao.commons.util.lang.math.NumberHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaginationBootstrap {
    private static final Logger logger = LoggerFactory.getLogger(PaginationBootstrap.class);

    public static String getBootstrap(long total, int page, int size) {
        int pageRange = 2;
        int firstIndex = 1;
        int lastIndex = (int) Math.ceil(total * 1.0 / size);


        if (!NumberHelper.isBetween(page, firstIndex, lastIndex)) {
            logger.warn("当前页数" + page + "必须在[" + firstIndex + "," + lastIndex + "]区间之中");
            page = page < firstIndex ? firstIndex : page;
            page = page > lastIndex ? lastIndex : page;
        }

        StringBuilder html = new StringBuilder("<ul class=\"pagination\">" + "\n");

        // 1. 首页按钮
        html.append(createLiTag(firstIndex, page <= firstIndex ? "disabled" : "", "&laquo;&laquo;") + "\n");

        // 2. 上一页
        html.append(createLiTag(page - 1, page <= firstIndex ? "disabled" : "", "&laquo;") + "\n");

        // 3. 前2页
        for (int i = page - pageRange; i < page; i++) {
            if (i < firstIndex) {
                continue;
            }
            html.append(createLiTag(i, "", String.valueOf(i)) + "\n");
        }
        // 4. 当前页
        html.append("<li class=\"active\"><a href=\"javascript:void(0)\">" + page + "</a></li>" + "\n");
        // 5. 后2页
        for (int i = page + 1; i <= page + pageRange; i++) {
            if (i > lastIndex) {
                continue;
            }
            html.append(createLiTag(i, "", String.valueOf(i)) + "\n");
        }
        // 7. 下一页
        html.append(createLiTag(page + 1, page >= lastIndex ? "disabled" : "", "&raquo;") + "\n");
        // 8. 尾页
        html.append(createLiTag(lastIndex, page <= lastIndex ? "disabled" : "", "&raquo;&raquo;") + "\n");
        html.append("</ul>");

        return html.toString();
    }

    private static String createLiTag(int page, String clazz, String text) {
        String js = StringUtils.equalsAny(clazz, "disabled", "active") ? "void(0)" : "jump(" + page + ")";
        return "<li class=\"" + clazz + "\"><a href=\"javascript:" + js + "\">" + text + "</a></li>";
    }
}
