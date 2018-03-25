package com.ahao.forum.guitar.shiro.realm;

import com.ahao.core.annotation.Realm;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.forum.guitar.module.admin.auth.dao.AuthMapper;
import com.ahao.forum.guitar.module.admin.role.dao.RoleMapper;
import com.ahao.forum.guitar.module.admin.user.dao.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Realm
public class AuthorizingRealm extends org.apache.shiro.realm.AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizingRealm.class);


    private UserMapper userMapper;
    private RoleMapper roleMapper;
    private AuthMapper authMapper;

    @Autowired
    public AuthorizingRealm(UserMapper userMapper, RoleMapper roleMapper, AuthMapper authMapper){
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.authMapper = authMapper;
    }

    // 用于认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1. 从token获取用户信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();

        // 2. 从数据库获取用户信息, 和用户输入进行对比
        IDataSet userData = userMapper.selectByUsername(username);
        // TODO 2.1. 判断验证码是否正确
        // 2.2. 判断用户是否存在
        if (userData == null) {
            String msg = "该用户不存在:"+username;
            logger.warn(msg);
            throw new UnknownAccountException(msg);
        }
        // 2.3. 判断密码是否正确
        String password = userData.getString("password");
        if(!new String(token.getPassword()).equals(password)){
            String msg = "用户名/密码错误:"+username;
            logger.warn(msg);
            throw new IncorrectCredentialsException(msg);
        }
        // 2.4. 判断用户是否被锁定
        if(Boolean.FALSE.equals(userData.getBoolean("enabled"))) {
            String msg = "该帐号被锁定:"+username;
            logger.warn(msg);
            throw new LockedAccountException(msg);
        }

        // TODO 3. 更新最后登录时间 ip

        // 4. 返回 AuthenticationInfo 对象, 将 userData 存入 Shiro, 方便获取
        AuthenticationInfo authorizationInfo =
                new SimpleAuthenticationInfo(userData, password, this.getName());
        return authorizationInfo;
    }

    // 用于授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.debug("测试"+principals);
        // 1. 从 Shiro 中获取已 认证 的用户信息
        IDataSet userData = (IDataSet) principals.getPrimaryPrincipal();
        long userId = userData.getLong("id");

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 2. 根据 userId 获取所有角色
        List<IDataSet> roleDatas = roleMapper.selectRoleByUserId(userId, "name");
        Set<String> roles = new HashSet<>(roleDatas.size());
        for (IDataSet roleData : roleDatas) {
            roles.add(roleData.getString("name"));
        }
        authorizationInfo.setRoles(roles);

        // 3. 根据 userId 获取所有权限
        List<IDataSet> authDatas = authMapper.selectAuthByUserId(userId, "name");
        Set<String> auths = new HashSet<>(authDatas.size());
        for (IDataSet authData : authDatas) {
            auths.add(authData.getString("name"));
        }
        authorizationInfo.setStringPermissions(auths);

        //查到权限数据，返回
        return authorizationInfo;
    }
}
