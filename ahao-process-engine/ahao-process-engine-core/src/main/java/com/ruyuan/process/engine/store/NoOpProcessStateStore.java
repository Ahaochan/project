package com.ruyuan.process.engine.store;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class NoOpProcessStateStore implements ProcessStateStore {
    @Override
    public void recordProcessMetadata(String name, Map<String, String> metadata) {

    }

    @Override
    public void updateMetadata(String name, String key, String value) {

    }

    @Override
    public void clearMetadata(String name) {

    }

    @Override
    public boolean lock(String key) {
        return false;
    }

    @Override
    public void unlock(String key) {

    }

    @Override
    public Collection<String> pollUnCompletedProcess(int timeout, TimeUnit unit, boolean refresh) {
        return new HashSet<>();
    }

    @Override
    public Map<String, String> getMap(String key) {
        return new HashMap<>();
    }
}
