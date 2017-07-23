package com.ahao.entity;

import com.ahao.context.PageContext;
import com.ahao.util.MathUtils;
import com.ahao.util.PageUrlBuilder;
import com.ahao.util.UrlBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Ahaochan on 2017/7/15.
 *
 * 分页器的Data Transfer Object
 */
public class PageDTO {
    private static final int RANGE_HEAD = 2;
    private static final int RANGE_MID = 5;
    private static final int RANGE_TAIL = 2;


    private int current;
    private int count;
    private int pageNum;
    private int pageSize;
    private List<Item> head;
    private List<Item> mid;
    private List<Item> tail;
    private Item pre;
    private Item next;
    private UrlBuilder urlBuilder;

    public PageDTO(int count, int current, String baseUrl) {
        this.count = count;
        this.urlBuilder = new PageUrlBuilder(baseUrl);
        this.pageSize = PageContext.getPageSize();

        this.pageNum = (int) Math.ceil(this.count*1.0/pageSize);
        this.current = MathUtils.between(1, this.pageNum, current);

        if(this.current > 1 && this.current <= pageNum){
            int i = this.current-1;
            pre = new Item(i, "<<",
                    urlBuilder.restUrl(PageContext.PAGE, i).build(), false);
        } else {
            pre = new Item(-1, "<<", "javascript:void(0);", false);
        }


        if(this.current >= 1 && this.current < pageNum){
            int i = this.current+1;
            next = new Item(i, ">>",
                    urlBuilder.restUrl(PageContext.PAGE, i).build(), false);
        } else {
            next = new Item(-1, ">>", "javascript:void(0);", false);
        }


        initMid();
        initHead();
        initTail();
    }

    private void initMid(){
        int start = MathUtils.between(1, pageNum - RANGE_MID + 1, current - RANGE_MID / 2);

        mid = Stream.iterate(start, i->i+1)
                .limit(RANGE_MID)
                .filter(i->1<=i && i<=pageNum)
                .map(i-> new Item(i, i+"",
                        urlBuilder.restUrl(PageContext.PAGE, i).build(), current==i))
                .collect(Collectors.toList());
    }

    private void initHead(){
        head = new ArrayList<>(RANGE_HEAD);
        if(mid.isEmpty()){
            return;
        }

        head.addAll(Stream.iterate(1, i->i+1)
                .limit(RANGE_HEAD)
                .filter(i->1<=i && i<=mid.get(0).getIndex()-1)
                .map(i-> new Item(i, i+"",
                        urlBuilder.restUrl(PageContext.PAGE, i).build(), current==i))
                .collect(Collectors.toList()));
    }

    private void initTail(){
        tail = new ArrayList<>(RANGE_TAIL);
        if(mid.isEmpty()){
            return;
        }
        int start = Math.max(mid.get(mid.size()-1).getIndex()+1,pageNum-RANGE_TAIL+1);

        tail.addAll(Stream.iterate(start, i->i+1)
                .limit(RANGE_TAIL)
                .filter(i->1<=i && i<=pageNum)
                .map(i-> new Item(i, i+"",
                        urlBuilder.restUrl(PageContext.PAGE, i).build(), current==i))
                .collect(Collectors.toList()));
    }

    public boolean hasPreMiss(){
        if(mid.size()>0 && head.size()>0){
            if(mid.get(0).index - head.get(head.size() - 1).index > 1){
                return true;
            }
        }
        return false;
    }

    public boolean hasLastMiss(){
        if(tail.size()>0 && mid.size()>0){
            if(tail.get(0).index - mid.get(mid.size() - 1).index > 1){
                return true;
            }
        }
        return false;
    }


    public static class Item {
        private final int index;
        private final String name;
        private final String url;
        private final boolean active;

        Item(int index, String name, String url, boolean active) {
            this.index = index;
            this.name = name;
            this.url = url;
            this.active = active;
        }

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public boolean isActive() {
            return active;
        }

        @Override
        public String toString() {
            return index+"";
        }
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Item> getHead() {
        return head;
    }

    public void setHead(List<Item> head) {
        this.head = head;
    }

    public List<Item> getMid() {
        return mid;
    }

    public void setMid(List<Item> mid) {
        this.mid = mid;
    }

    public List<Item> getTail() {
        return tail;
    }

    public void setTail(List<Item> tail) {
        this.tail = tail;
    }

    public Item getPre() {
        return pre;
    }

    public void setPre(Item pre) {
        this.pre = pre;
    }

    public Item getNext() {
        return next;
    }

    public void setNext(Item next) {
        this.next = next;
    }

}
