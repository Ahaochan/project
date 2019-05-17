package com.ahao.rbac.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

public class ShiroSessionListener extends SessionListenerAdapter {
    private AtomicInteger sessionCount = new AtomicInteger();

    @Override
    public void onStart(Session session) {
        sessionCount.getAndIncrement();
//        RedisHelper.incr(RedisKeys.SHIRO_SESSION_COUNT);
    }

    @Override
    public void onStop(Session session) {
        sessionCount.getAndDecrement();
//        RedisHelper.decr(RedisKeys.SHIRO_SESSION_COUNT);
    }

    @Override
    public void onExpiration(Session session) {
        sessionCount.getAndDecrement();
//        RedisHelper.decr(RedisKeys.SHIRO_SESSION_COUNT);
    }
}
