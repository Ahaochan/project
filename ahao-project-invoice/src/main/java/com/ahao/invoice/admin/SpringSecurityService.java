package com.ahao.invoice.admin;

import com.ahao.invoice.admin.auth.dao.AuthDAO;
import com.ahao.invoice.admin.user.dao.UserDAO;
import com.ahao.invoice.admin.user.entity.UserDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Ahaochan on 2017/7/12.
 *
 * Spring Security权限验证Service层
 */
@Service
public class SpringSecurityService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(SpringSecurityService.class);

    private UserDAO userDAO;
    private AuthDAO authDAO;

    @Autowired
    public SpringSecurityService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = (User) this.userCache.getUserFromCache(username);
        UserDetails user = null;
        if (user == null) {
            UserDO dbUser = userDAO.loginByUsername(username);
            if (dbUser == null) {
                throw new UsernameNotFoundException("用户名不存在");
            }
            logger.debug("登录用户: "+dbUser.getUsername());
            Set<GrantedAuthority> auth = authDAO.selectNameByUserId(dbUser.getId())
                    .stream()
                    .peek(a -> logger.debug("拥有权限: "+a.getName()))
                    .map(a -> new SimpleGrantedAuthority(a.getName()))
                    .collect(Collectors.toSet());
            user = new User(dbUser.getUsername(), dbUser.getPassword(), dbUser.getEnabled(),
                    !dbUser.getAccountExpired(), !dbUser.getCredentialsExpired(), !dbUser.getAccountLocked(),
                    auth);
        }

//        this.userCache.putUserInCache(user);
        return user;
    }
}
