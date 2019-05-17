//package com.ahao.rbac.shiro.credential;
//
//import com.ahao.rbac.shiro.dao.UserMapper;
//import com.ahao.rbac.shiro.entity.ShiroUser;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.LockedAccountException;
//import org.apache.shiro.authc.credential.CredentialsMatcher;
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * 限制登录次数 的 Credential 验证器
// * 使用 {@link org.apache.shiro.realm.AuthorizingRealm#setCredentialsMatcher(CredentialsMatcher)} 注入 Realm 即可.
// */
//public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
//    private static final Logger logger = LoggerFactory.getLogger(RetryLimitHashedCredentialsMatcher.class);
//
//    private int maxRetryCount = 3;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Override
//    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
//        // 1. 根据 principal 获取用户
//        String principal = (String) token.getPrincipal();
//        ShiroUser user = userMapper.selectByUsernameOrEmail(principal);
//        // 2. 查询获取用户登录次数, 没有则初始化
//        String redisKey = String.format(RedisKeys.SHIRO_RETRY, user.getId());
//        AtomicInteger retryCount = RedisHelper.get(redisKey, AtomicInteger.class);
//        if (retryCount == null) {
//            retryCount = new AtomicInteger(0);
//        }
//
//        // 3. 如果用户登陆失败次数大于 maxRetryCount 次, 则 锁定用户并抛出异常
//        if (retryCount.incrementAndGet() > maxRetryCount) {
//            if (user.getLocked() == null || !user.getLocked()){
//                user.setLocked(true);
//                userMapper.updateByPrimaryKeySelective(user);
//            }
//            logger.info("锁定用户{}", user.getId());
//            throw new LockedAccountException("重试次数过多, 该用户已被锁定!");
//        }
//
//        // 4. 正常的凭证判断逻辑, 登陆成功则删除, 否则更新
//        boolean matches = super.doCredentialsMatch(token, info);
//        if (matches) {
//            RedisHelper.del(redisKey);
//        } else {
//            RedisHelper.set(redisKey, retryCount);
//        }
//        return matches;
//    }
//
//    // ================================== Getter And Setter ========================================
//    public int getMaxRetryCount() {
//        return maxRetryCount;
//    }
//
//    public void setMaxRetryCount(int maxRetryCount) {
//        this.maxRetryCount = maxRetryCount;
//    }
//}
