import com.ahao.invoice.admin.role.dao.RoleDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Ahaochan on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-dao.xml")
public class RoleDAOTest {

    @Autowired
    private RoleDAO roleDAO;

    @Test
    public void addRelate(){
        long userId = 100L;
        Long[] roleId = Stream.of(50,60,70,80).map(Long::valueOf).toArray(Long[]::new);

        roleDAO.addRelate(userId, roleId);
    }

    @Test
    public void getNameByUserId() {
        long userId = 2L;
        List<Map<String, Object>> list = roleDAO.selectNameByUserId(userId);
        for (Map<String, Object> item : list) {
            System.out.println("测试1");
            for (Map.Entry<String, Object> entry : item.entrySet()) {
                System.out.println("测试:" + entry.getKey() + " : " + entry.getValue());
            }
        }
    }
}
