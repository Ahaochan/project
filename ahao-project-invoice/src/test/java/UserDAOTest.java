import com.ahao.invoice.admin.auth.dao.AuthDAO;
import com.ahao.invoice.admin.user.dao.UserDAO;
import com.ahao.invoice.admin.user.entity.UserDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Ahaochan on 2017/7/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-dao.xml")
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AuthDAO authDAO;

    @Test
    public void login(){
        UserDO user = userDAO.loginByUsername("admin1");
        System.out.println(user);
        Set<GrantedAuthority> auth = authDAO.selectNameByUserId(user.getId())
                .stream()
                .peek(a -> System.out.println("权限:"+a.toString()))
                .map(a -> new SimpleGrantedAuthority(a.getName()))
                .collect(Collectors.toSet());

        System.out.println(auth);
    }

    @Test
    public void page(){
        Collection<UserDO> list = userDAO.selectPage(0, 25,"id","asc");
        for(UserDO userDO : list){
            System.out.println(userDO);
        }
    }

}
