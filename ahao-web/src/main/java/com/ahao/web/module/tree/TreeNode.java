package com.ahao.web.module.tree;

import java.util.List;

public class TreeNode<I, C extends TreeNode<I, C>> {
    private I id;
    private I rootId;
    private I parentId;
    private String idPath;
    private Boolean isLeaf;
    private Integer level;

    private List<C> childNodeList;

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }

    public I getRootId() {
        return rootId;
    }

    public void setRootId(I rootId) {
        this.rootId = rootId;
    }

    public I getParentId() {
        return parentId;
    }

    public void setParentId(I parentId) {
        this.parentId = parentId;
    }

    public String getIdPath() {
        return idPath;
    }

    public void setIdPath(String idPath) {
        this.idPath = idPath;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<C> getChildNodeList() {
        return childNodeList;
    }

    public void setChildNodeList(List<C> childNodeList) {
        this.childNodeList = childNodeList;
    }
}
