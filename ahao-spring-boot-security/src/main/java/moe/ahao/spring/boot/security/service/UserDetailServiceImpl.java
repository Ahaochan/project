package moe.ahao.spring.boot.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        boolean exists = "admin".equals(username);
        if(!exists) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        char[] rawPassword = "admin".toCharArray();



        String password = passwordEncoder.encode(new String(rawPassword));
        return new User(username, password,
            true, true, true, true,
            AuthorityUtils.createAuthorityList("hhh"));
    }
}
