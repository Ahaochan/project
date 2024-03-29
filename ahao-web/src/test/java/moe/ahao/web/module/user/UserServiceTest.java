package moe.ahao.web.module.user;

import moe.ahao.web.module.user.entity.User;
import moe.ahao.web.module.user.mapper.AhaoUserMapper;
import moe.ahao.web.module.user.service.UserService;
import moe.ahao.web.module.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = UserServiceImpl.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private AhaoUserMapper userMapper;

    @Test
    public void testGetUserById() {
        Long userId = 1L;

        User user1 = new User();
        user1.setId(userId);
        user1.setUsername("Ahao");
        user1.setEmail("Ahao@moe.com");
        user1.setPassword("AhaoPW");

        BDDMockito.given(this.userMapper.selectById(ArgumentMatchers.anyLong())).willReturn(user1);

        User user2 = userService.getUserById(userId);

        Assertions.assertEquals(user1, user2);
    }
}
