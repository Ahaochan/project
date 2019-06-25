package com.ahao.spring.boot.shiro.realm;

import com.ahao.spring.boot.shiro.dao.UserMapper;
import com.ahao.spring.boot.shiro.entity.ShiroUser;
import org.apache.commons.compress.utils.Sets;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 密码登陆 的 Realm
 * 可以实现以下登陆方式
 * 1. 用户名密码登陆
 * 2. 邮箱密码登陆
 */
@Service
public class PasswordRealm extends AuthorizingRealm implements InitializingBean {
    public static final String REALM_NAME = PasswordRealm.class.getSimpleName();

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public void setName(String name) {
        super.setName(REALM_NAME);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // TODO 待实现
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(Sets.newHashSet("role1", "role2"));
        authorizationInfo.setStringPermissions(Sets.newHashSet("permission1", "permission2"));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 1. 获取 用户名 或 邮箱
        String principal = (String) token.getPrincipal();

        // 2. 查询持久化的用户记录, 校验是否异常
        ShiroUser user = userMapper.selectByUsernameOrEmail(principal);
        ShiroUser.assertShiro(user);

        // 3. 密码加盐处理
        String salt = user.getSalt();
        ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
//        String hashedPassword = (new Sha512Hash(new String((char[]) token.getCredentials()), credentialsSalt, 1024)).toString();

        // 4. 返回正确的鉴权信息, 交由 Shiro 校验
        String password = user.getPassword();
        return new SimpleAuthenticationInfo(principal, password, credentialsSalt, getName());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 1. 开启缓存
        this.setCachingEnabled(true);
        this.setAuthenticationCachingEnabled(true);
        this.setAuthenticationCacheName("authenticationCache");
        this.setAuthorizationCachingEnabled(true);
        this.setAuthorizationCacheName("authorizationCache");

        // 2. 设置密码校验器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("SHA-512");
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        this.setCredentialsMatcher(credentialsMatcher);
    }
}
