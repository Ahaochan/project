package com.ahao.core.html.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/8/14.
 * 生成分页器html代码, 抽取各种分页器共用部分。
 * 有助于SEO
 */
public abstract class Builder<T extends Builder> {
    private static final Logger logger = LoggerFactory.getLogger(Builder.class);

    private static final String TEXT_FIRST = "首页";
    private static final String TEXT_LAST = "尾页";
    private static final String TEXT_BEFORE = "上一页";
    private static final String TEXT_AFTER = "下一页";

    // 总记录数
//    private int total;
    // 当前页数
    protected int page;
    // 每页记录数
//    private int size;
    // 首页
    protected int firstIndex;
    // 首页文字
    protected String firstText;
    // 尾页
    protected int lastIndex;
    // 尾页文字
    protected String lastText;
    // 上一页文字
    protected String beforeText;
    // 下一页文字
    protected String afterText;
    // 跳转页面的js事件
    protected String js;
    // 跳转链接
    protected String href;

    // 生成的html代码
    private StringBuilder html;

    protected Builder(int total, int page, int size) {
//        this.total = total;
        this.page = page;
//        this.size = size;
        this.firstIndex = 1;
        this.firstText = TEXT_FIRST;
        this.lastIndex = (int) Math.ceil(total * 1.0 / size);
        this.lastText = TEXT_LAST;
        this.beforeText = TEXT_BEFORE;
        this.afterText = TEXT_AFTER;
        this.js = "jump";
        this.href = "javascript:void(0)";
    }


    protected abstract void buildHtml();

    // ------------------------ Setter And Getter 链式调用 --------------------------------
    @SuppressWarnings("unchecked") // 类型安全, 因为this是T的示例, T继承自BaseXMLBuilder
    public T js(String onclick) {
        this.js = onclick;
        return (T) this;
    }
    @SuppressWarnings("unchecked") // 类型安全, 因为this是T的示例, T继承自BaseXMLBuilder
    public T href(String href) {
        this.href = href;
        return (T) this;
    }

    @SuppressWarnings("unchecked") // 类型安全, 因为this是T的示例, T继承自BaseXMLBuilder
    public T first(int index, String text) {
        this.firstIndex = index;
        this.firstText = text;
        return (T) this;
    }
    @SuppressWarnings("unchecked") // 类型安全, 因为this是T的示例, T继承自BaseXMLBuilder
    public T last(int index, String text) {
        this.lastIndex = index;
        this.lastText = text;
        return (T) this;
    }

    public String html() {
        return html(true);
    }

    public String html(boolean rebuild) {
        if (rebuild) {
            html = new StringBuilder(100);
            buildHtml();
        }
        return html.toString();
    }

    public StringBuilder appendHtml(String html) {
        return this.html.append(html);
    }

}
