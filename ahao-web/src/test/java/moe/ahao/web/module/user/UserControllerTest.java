package moe.ahao.web.module.user;

import moe.ahao.web.module.user.controller.UserController;
import moe.ahao.web.module.user.entity.User;
import moe.ahao.web.module.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {UserController.class,
    WebMvcAutoConfiguration.class, JacksonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
})
public class UserControllerTest {
    @MockBean
    private UserService userService;
    private MockMvc mockMvc;
    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGet() throws Exception {
        Long userId = 1L;

        User user1 = new User();
        user1.setId(userId);
        user1.setUsername("Ahao");
        user1.setEmail("Ahao@moe.com");
        user1.setPassword("AhaoPW");

        BDDMockito.given(this.userService.getUserById(ArgumentMatchers.anyLong())).willReturn(user1);

        mockMvc.perform(get("/user/get").param("id", userId.toString()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(user1.getId()))
            .andExpect(jsonPath("$.username").value(user1.getUsername()))
            .andExpect(jsonPath("$.email").value(user1.getEmail()))
            .andExpect(jsonPath("$.password").value(user1.getPassword()));
    }


}
