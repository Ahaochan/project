package com.ahao.web.module.tree;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TreeUtils {
    /**
     * 递归构造属性结构, 根据筛选条件, 过滤叶子节点.
     * 当叶子节点为空, 且当前节点不满足筛选条件, 则移除此节点
     * @param nodeId    父节点id
     * @param parentMap 节点集合
     * @param filter    筛选条件
     */
    public static <I, N extends TreeNode<I, N>> List<N> convertList2Tree(I nodeId, Map<I, List<N>> parentMap, Predicate<N> filter) {
        List<N> childTree = parentMap.get(nodeId);
        if(CollectionUtils.isEmpty(childTree)) {
            return Collections.emptyList();
        }

        Iterator<N> iterator = childTree.iterator();
        while (iterator.hasNext()) {
            N childNode = iterator.next();
            I childNodeId = childNode.getId();
            List<N> childChildTree = TreeUtils.convertList2Tree(childNodeId, parentMap, filter);
            childNode.setChildNodeList(childChildTree);

            if(!filter.evaluate(childNode) && CollectionUtils.isEmpty(childChildTree)) {
                iterator.remove(); // 当叶子节点为空, 且当前节点不满足筛选条件, 则移除此节点
            }
        }
        return childTree;
    }

    /**
     * 获取树的最大深度
     * @param list 一级节点集合
     */
    public static <I, N extends TreeNode<I, N>> int getMaxLevel(List<N> list) {
        if(CollectionUtils.isEmpty(list)) {
            return 0;
        }

        int max = Integer.MIN_VALUE;
        for (N item : list) {
            max = Math.max(max, TreeUtils.getMaxLevel(item.getChildNodeList()) + 1);
        }
        return max;
    }
}
