package com.ahao.invoice.admin.user.entity;

import com.ahao.invoice.admin.role.entity.RoleDO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Avalon on 2017/6/7.
 */
public class UserDetailView {
    public static final String TAG =  "userDetailView";
    private static final Logger logger = LoggerFactory.getLogger(UserDetailView.class);

    private final UserDO userData;
    private final Map<RoleDO, Boolean> roles;


//    private final Map<String, Map<AuthDO, Boolean>> panel;

    public UserDetailView(UserDO userData, Set<RoleDO> allRole, Set<RoleDO> userRoles) {
        this.userData = userData;
        this.roles = allRole.stream()
                .collect(Collectors.toMap(r->r, r->!userRoles.add(r)));


        // TODO 优化权限列表缓存
//        this.panel = allAuths.stream()
//                .collect(Collectors.groupingBy(
//                        a -> a.getName().substring(0, StringUtils.ordinalIndexOf(a.getName(), "_", 2)),
//                        Collectors.toMap(a -> a, a->userAuths!=null && !userAuths.add(a)))
//                );
    }

    public Map<RoleDO, Boolean> getRoles() {
        return roles;
    }

    public UserDO getUserData() {
        return userData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserDetailView that = (UserDetailView) o;

        return new EqualsBuilder()
                .append(userData, that.userData)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(userData)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "UserDetailView{" +
                "userData=" + userData +
                '}';
    }
}
