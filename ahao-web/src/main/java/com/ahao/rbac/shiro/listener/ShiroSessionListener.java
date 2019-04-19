package com.ahao.rbac.shiro.listener;

import com.ahao.redis.config.RedisKeys;
import com.ahao.redis.util.RedisHelper;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

public class ShiroSessionListener extends SessionListenerAdapter {

    @Override
    public void onStart(Session session) {
        RedisHelper.incr(RedisKeys.SHIRO_SESSION_COUNT);
    }

    @Override
    public void onStop(Session session) {
        RedisHelper.decr(RedisKeys.SHIRO_SESSION_COUNT);
    }

    @Override
    public void onExpiration(Session session) {
        RedisHelper.decr(RedisKeys.SHIRO_SESSION_COUNT);
    }
}
