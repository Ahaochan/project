//package com.ahao.entity;
//
//import com.ahao.util.UrlBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//import java.util.stream.*;
//
///**
// * Created by Ahaochan on 2017/5/13.
// */
//public class PageView<T> {
//    private static final Logger logger = LoggerFactory.getLogger(PageView.class);
//    public static final String TAG = "pageView";
//
//    private LinkedList<PageItem> urls = new LinkedList<>();  // 页面
//    private PageItem prePage;   // 上一页
//    private PageItem nextPage;  // 下一页
//
//    private Collection<AView> sizeView = new ArrayList<>();   // 分页限制Url
//    private Collection<T> data = new ArrayList<>();           // 当前页的数据
//
//
//    private Builder build;
//
//    public PageView(int totalRecord){
//        this.build = new Builder(totalRecord);
//    }
//
//    public Builder get(){
//        return build;
//    }
//
//    /*-------------------Getter And Setter---------------------*/
//    public int getTotalRecord() {
//        return build.getTotalRecord();
//    }
//
//    public int getTotalPage() {
//        return build.getTotalPage();
//    }
//
//    public UrlBuilder getUrlBuilder(){
//        return build.getUrlBuilder();
//    }
//
//
//    public PageItem getPrePage() {
//        return prePage;
//    }
//
//    public PageItem getNextPage() {
//        return nextPage;
//    }
//
//    public Collection<AView> getSizeView() {
//        return sizeView;
//    }
//
//    public Collection<T> getData() {
//        return data;
//    }
//
//    public Collection<PageItem> getUrls() {
//        return urls;
//    }
//
//    /*-------------------Getter And Setter---------------------*/
//
//
//    public class Builder {
//        private static final int PAGE_MISS_INDEX = -1;
//        private static final String PAGE_MISS_NAME = "...";     // 页数过多时显示省略号
//        private static final String PAGE_MISS_URL = "javascript:void(0);";
//        private static final String PAGE_FIRST_NAME = "<<";     // 向前翻页
//        private static final String PAGE_LAST_NAME = ">>";      // 向后翻页
//
//        private static final int PAGE_MIN_INDEX = 1;
//        private static final int DEFAULT_HEAD_RANGE = 2;   // 默认头部页数大小
//        private static final int DEFAULT_MID_RANGE = 5;   // 默认中间页数大小   1,2,...,4,5,6,7,8,...,99,100
//        private static final int DEFAULT_TAIL_RANGE = 2;   // 默认尾部页数大小
//
//        private static final String DEFAULT_PAGE_KEY = "page";
//        private static final int DEFAULT_PAGE_NUM = 1;
//        private static final String DEFAULT_SIZE_KEY = "size";
//        private static final int DEFAULT_SIZE_NUM = 25;           // 默认一页记录条数
//
//        private int totalRecord;     // 查询的记录总数
//        private int totalPage;       // 总页数，记录总数/一页记录数
//
//        private int headRange = DEFAULT_HEAD_RANGE;    // 头部页数大小
//        private int midRange = DEFAULT_MID_RANGE;     // 中间页数大小   1,2,...,4,5,6,7,8,...,99,100
//        private int tailRange = DEFAULT_TAIL_RANGE;    // 尾部页数大小
//
//        private String pageKey = DEFAULT_PAGE_KEY;   // 当前页数的key
//        private int pageNum = DEFAULT_PAGE_NUM;      // 当前页数
//        private String sizeKey = DEFAULT_SIZE_KEY;
//        private int sizeNum = DEFAULT_SIZE_NUM;      // 一页记录数
//
//        private UrlBuilder urlBuilder;    // url构建器
//
//        public Builder(int totalRecord) {
//            this.totalRecord = totalRecord;
//        }
//
//        public Builder pageRange(int headRange, int midRange, int tailRange) {
//            this.headRange = headRange;
//            this.midRange = midRange;
//            this.tailRange = tailRange;
//            return this;
//        }
//
//        public Builder current(int pageNum) {
//            return current(DEFAULT_PAGE_KEY, pageNum);
//        }
//
//        public Builder current(String pageKey, int pageNum) {
//            this.pageKey = pageKey;
//            this.pageNum = pageNum;
//            return this;
//        }
//
//        public Builder pageSize(int sizeNum) {
//            return pageSize(DEFAULT_SIZE_KEY, sizeNum);
//        }
//
//        public Builder pageSize(String sizeKey, int sizeNum) {
//            this.sizeKey = sizeKey;
//            this.sizeNum = sizeNum;
//            return this;
//        }
//
//        public Builder urlBuilder(UrlBuilder builder) {
//            this.urlBuilder = builder;
//            return this;
//        }
//
//        public Builder addPageData(Collection<T> d) {
//            data.addAll(d);
//            return this;
//        }
//
//        public Builder sizeView(int... size) {
//            if(urlBuilder == null){
//                throw new NullPointerException("must inject urlBuilder");
//            }
//            sizeView.addAll(IntStream.of(size)
//                    .mapToObj(s -> s + "")
//                    .map(s -> new AView(s, urlBuilder.param(sizeKey, s).build(pageKey, 1)))
//                    .collect(Collectors.toList()));
//            return this;
//        }
//
//        public PageView<T> build() {
//            prepare();
//
//            buildPreMaker();
//            buildHeadRange();
//            buildMidRange();
//            buildTailRange();
//            buildNextMaker();
//
//            return (PageView<T>) PageView.this;
//        }
//
//        private void prepare() {
//            this.totalPage = (int) Math.ceil(totalRecord * 1.0 / sizeNum);
//            if (urlBuilder == null) {
//                throw new NullPointerException("urlBuilder must not be null!");
//            }
//        }
//
//        private void buildPreMaker() {
//            boolean hasPre = pageNum > PAGE_MIN_INDEX;
//            if (hasPre) {
//                int page = pageNum - 1;
//                prePage = new PageItem(page, PAGE_FIRST_NAME, urlBuilder.buildUrl(pageKey, page),
//                        false, false);
//            } else {
//                prePage = new PageItem(PAGE_MISS_INDEX, PAGE_FIRST_NAME, PAGE_MISS_URL,
//                        false, true);
//            }
//        }
//
//        private void buildNextMaker() {
//            boolean hasNext = pageNum < totalPage;
//            if (hasNext) {
//                int page = pageNum + 1;
//                nextPage = new PageItem(page, PAGE_LAST_NAME, urlBuilder.buildUrl(pageKey, page),
//                        false, false);
//            } else {
//                nextPage = new PageItem(PAGE_MISS_INDEX, PAGE_LAST_NAME, PAGE_MISS_URL,
//                        false, true);
//            }
//        }
//
//        private int fakeCurrentPage() {
//            int currentPage = this.pageNum;
//            if (currentPage <= headRange) {
//                currentPage = headRange + 1;
//            } else if (currentPage > totalPage - tailRange) {
//                currentPage = totalPage - tailRange;
//            }
//            return currentPage;
//        }
//
//        private void buildHeadRange() {
//            int fakeCurrentPage = fakeCurrentPage();
//            // 添加省略号
//            boolean hasMissPage = fakeCurrentPage - midRange / 2 > headRange + 1;
//            if (hasMissPage) {
//                urls.addFirst(new PageItem(PAGE_MISS_INDEX, PAGE_MISS_NAME, PAGE_MISS_URL,
//                        false, true));
//            }
//
//            // 添加前置页码
//            boolean hasHeadPage = fakeCurrentPage - midRange / 2 > PAGE_MIN_INDEX;
//            if (hasHeadPage) {
//                int size = fakeCurrentPage - midRange / 2 > headRange ?
//                        headRange : headRange - (fakeCurrentPage - midRange / 2 - PAGE_MIN_INDEX);
//                Stream.iterate(PAGE_MIN_INDEX + size - 1, i -> i - 1)
//                        .limit(size)
//                        .filter(i -> i >= PAGE_MIN_INDEX)
//                        .filter(i -> i <= totalPage)
//                        .forEach(this::addFirstPageUrl);
//            }
//        }
//
//        private void buildMidRange() {
//            int fakeCurrentPage = fakeCurrentPage();
//            Stream.iterate(fakeCurrentPage - midRange / 2, i -> i + 1)
//                    .limit(midRange)
//                    .filter(i -> i >= PAGE_MIN_INDEX)
//                    .filter(i -> i <= totalPage)
//                    .forEach(this::addPageUrl);
//        }
//
//        private void buildTailRange() {
//            int fakeCurrentPage = fakeCurrentPage();
//            // 添加省略号
//            boolean hasMiss = fakeCurrentPage + midRange / 2 < totalPage - tailRange;
//            if (hasMiss) {
//                urls.addLast(new PageItem(PAGE_MISS_INDEX, PAGE_MISS_NAME, PAGE_MISS_URL,
//                        false, true));
//            }
//
//            // 添加后置代码
//            int size = fakeCurrentPage + midRange / 2 > totalPage - tailRange ?
//                    tailRange - (fakeCurrentPage + midRange / 2 - (totalPage - tailRange))
//                    : tailRange;
//            size = Math.abs(size);
//            Stream.iterate(totalPage - size + 1, i -> i + 1)
//                    .limit(size)
//                    .filter(i -> i >= PAGE_MIN_INDEX)
//                    .filter(i -> i <= totalPage)
//                    .forEach(this::addLastPageUrl);
//        }
//
//        private Collection addFirstPageUrl(int page) {
//            if (page > 0 && page <= totalPage) {
//                urls.addFirst(new PageItem(page, urlBuilder.buildUrl(pageKey, page)));
//            }
//            return urls;
//        }
//
//        private Collection addLastPageUrl(int page) {
//            if (page > 0 && page <= totalPage) {
//                urls.addLast(new PageItem(page, urlBuilder.buildUrl(pageKey, page)));
//            }
//            return urls;
//        }
//
//        private Collection addPageUrl(int page) {
//            if (page > 0 && page <= totalPage) {
//                urls.add(new PageItem(page, page + "", urlBuilder.buildUrl(pageKey, page),
//                        pageNum == page, false));
//            }
//            return urls;
//        }
//
//        private int getTotalRecord() {
//            return totalRecord;
//        }
//
//        public int getTotalPage() {
//            return totalPage;
//        }
//
//        public UrlBuilder getUrlBuilder() {
//            return urlBuilder;
//        }
//
//        @Override
//        public String toString() {
//            return "Builder{" +
//                    "totalRecord=" + totalRecord +
//                    ", totalPage=" + totalPage +
//                    ", headRange=" + headRange +
//                    ", midRange=" + midRange +
//                    ", tailRange=" + tailRange +
//                    ", pageKey='" + pageKey + '\'' +
//                    ", pageNum=" + pageNum +
//                    ", sizeKey='" + sizeKey + '\'' +
//                    ", sizeNum=" + sizeNum +
//                    ", urlBuilder=" + urlBuilder +
//                    '}';
//        }
//    }
//
//    /**
//     * 页面Item
//     */
//    public static class PageItem {
//        private final int index;
//        private final String name;
//        private final String url;
//        private final boolean active;
//        private final boolean disabled;
//
//        PageItem(int index, String url) {
//            this(index, index + "", url,
//                    false, false);
//        }
//
//        PageItem(int index, String name, String url, boolean active, boolean disabled) {
//            this.index = index;
//            this.name = name;
//            this.url = url;
//            this.active = active;
//            this.disabled = disabled;
//        }
//
//        public int getIndex() {
//            return index;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public boolean isActive() {
//            return active;
//        }
//
//        public boolean isDisabled() {
//            return disabled;
//        }
//
//        @Override
//        public String toString() {
//            return "PageItem{" +
//                    "index=" + index +
//                    ", name='" + name + '\'' +
//                    ", url='" + url + '\'' +
//                    ", active=" + active +
//                    ", disabled=" + disabled +
//                    '}';
//        }
//    }
//
//    public static class AView {
//        private final String text;
//        private final String href;
//
//        public AView(String text, String href) {
//            this.text = text;
//            this.href = href;
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public String getHref() {
//            return href;
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "PageView{" +
//                "urls=" + urls +
//                ", prePage=" + prePage +
//                ", nextPage=" + nextPage +
//                ", sizeView=" + sizeView +
//                ", data=" + data +
//                ", build=" + build +
//                '}';
//    }
//}
