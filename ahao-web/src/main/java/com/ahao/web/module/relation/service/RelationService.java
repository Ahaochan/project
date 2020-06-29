package com.ahao.web.module.relation.service;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * 关联关系 Service
 */
public interface RelationService<M, S> {
    boolean addRelation(M mainId, S subId);
    boolean delRelation(M mainId, S subId);

    // @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    default boolean addRelation(List<M> mainIdList, List<S> subIdList) {
        return this.execRelation(mainIdList, subIdList, this::addRelation);
    }

    // @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    default boolean delRelation(List<M> mainIdList, List<S> subIdList) {
        return this.execRelation(mainIdList, subIdList, this::delRelation);
    }

    // @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    default boolean execRelation(List<M> mainIdList, List<S> subIdList, BiConsumer<M, S> consumer) {
        if(CollectionUtils.isEmpty(mainIdList) || CollectionUtils.isEmpty(subIdList)) {
            return false;
        }
        for (M mainId : mainIdList) {
            for (S subId : subIdList) {
                consumer.accept(mainId, subId);
            }
        }
        return true;
    }
}
