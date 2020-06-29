package com.ahao.web.module.tree;

import java.util.List;

public class TreeNode<I, C extends TreeNode<I, C>> {
    private I id;
    private I rootId;
    private I parentId;
    private Boolean isLeaf;
    private Integer level;
    private String relationLink;

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

    public String getRelationLink() {
        return relationLink;
    }

    public void setRelationLink(String relationLink) {
        this.relationLink = relationLink;
    }

    public List<C> getChildNodeList() {
        return childNodeList;
    }

    public void setChildNodeList(List<C> childNodeList) {
        this.childNodeList = childNodeList;
    }
}
