package com.ahao.entity;

import java.util.Collection;

/**
 * Created by Ahaochan on 2017/7/15.
 */
public class TableDTO<T> {
    private String name;
    private Collection<String> header;
    private String sort;
    private Collection<T> cell;

    public TableDTO() {
    }

    public TableDTO(String name, Collection<String> header, String sort, Collection<T> cell) {
        this.name = name;
        this.header = header;
        this.sort = sort;
        this.cell = cell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getHeader() {
        return header;
    }

    public void setHeader(Collection<String> header) {
        this.header = header;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Collection<T> getCell() {
        return cell;
    }

    public void setCell(Collection<T> cell) {
        this.cell = cell;
    }
}
