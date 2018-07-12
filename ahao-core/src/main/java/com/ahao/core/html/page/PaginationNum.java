package com.ahao.core.html.page;

import com.ahao.core.util.lang.math.NumberHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/8/11.
 * 首页 上一页 1 2 3 ... 4 5 下一页 尾页
 * <p>
 * 生成<li>标签的分页器
 * <li class=""><a href="abc.com/page-1" onclick="jump(1);return false;">首页</a></li>
 * <li class=""><a href="abc.com/page-7" onclick="jump(7);return false;">上一页</a></li>
 * <li class=""><a href="abc.com/page-7" onclick="jump(7);return false;">7</a></li>
 * <li class="active"><a href="abc.com/page-8" onclick="jump(8);return false;">8</a></li>
 * <li class=""><a href="abc.com/page-9" onclick="jump(9);return false;">9</a></li>
 * <li class=""><a href="abc.com/page-10" onclick="jump(10);return false;">10</a></li>
 * <li class=""><a href="abc.com/page-9" onclick="jump(9);return false;">下一页</a></li>
 * <li class=""><a href="abc.com/page-10" onclick="jump(10);return false;">尾页</a></li>
 */
public class PaginationNum extends Builder<PaginationNum> {
    private static final Logger logger = LoggerFactory.getLogger(PaginationNum.class);

    private static final int RANGE_HEAD = 3;
    private static final int RANGE_TAIL = 2;

    // 前导页面数量
    private int headRange;
    // 后导页面数量
    private int tailRange;


    public static PaginationNum create(int total, int page, int size) {
        return new PaginationNum(total, page, size);
    }

    private PaginationNum(int total, int page, int size) {
        super(total, page, size);

        this.headRange = RANGE_HEAD;
        this.tailRange = RANGE_TAIL;
    }

    // 构建html
    @Override
    protected void buildHtml() {
        if (!NumberHelper.isBetween(this.page, firstIndex, lastIndex)) {
            logger.warn("当前页数" + this.page + "必须在[" + firstIndex + "," + lastIndex + "]区间之中");
            page = page < firstIndex ? firstIndex : page;
            page = page > lastIndex ? lastIndex : page;
        }

        // 首页标签
        appendHtml(item(firstIndex, firstText, page <= firstIndex ? "disabled" : ""));

        // 上一页标签
        appendHtml(item(page - 1, beforeText, page <= firstIndex ? "disabled hidden-xs" : "hidden-xs"));

        // 前导页面
        for (int i = 0; i < headRange; i++) {
            int index = page -1 +i;
            if (index < firstIndex || index > lastIndex) {
                continue;
            }
            appendHtml(item(index, String.valueOf(index), page == index ? "active" : ""));
        }

        if(page + 1 < lastIndex - tailRange){
            appendHtml("<li><span>...</span></li>");
        }

        // 后导页面
        for (int i = lastIndex - tailRange +1; i <= lastIndex; i++) {
            if (i <= page + 1 || i > lastIndex) {
                continue;
            }
            appendHtml(item(i, String.valueOf(i), ""));
        }

        // 下一页标签
        appendHtml(item(page + 1, afterText, page >= lastIndex ? "disabled hidden-xs": "hidden-xs"));

        // 尾页标签
        appendHtml(item(lastIndex, lastText, page >= lastIndex ? "disabled": ""));
    }


    /**
     * 生成 <li><a></a></li> 标签,
     * 如: <li><a href="http://url?page=${page}"
     *              onclick="js();return false;">text</a>
     *     </li>的形式
     * @param page  页数
     * @param text  显示文字
     * @param liClass li标签的class
     * @return 返回格式化后的<li><a></a></li>标签
     */
    private String item(int page, String text, String liClass) {
        StringBuilder li = new StringBuilder();
        li.append("<li class=\"").append(liClass).append("\">");
        if (StringUtils.isNotEmpty(js)) {
            li.append("<a href=\"")
                    .append(StringUtils.replace(href, "${page}", String.valueOf(page)))
                    .append("\" onclick=\"")
                    .append(js).append("(").append(page).append(");return false;\"")
                    .append(">").append(text).append("</a>");
        } else {
            li.append("<span>").append(text).append("</span>");
        }
        li.append("</li>");
        return li.toString();
    }


    // ------------------------ Setter And Getter 链式调用 --------------------------------
    public PaginationNum headPage(int range) {
        this.headRange = range;
        return this;
    }

    public PaginationNum tailPage(int range) {
        this.tailRange = range;
        return this;
    }
}
