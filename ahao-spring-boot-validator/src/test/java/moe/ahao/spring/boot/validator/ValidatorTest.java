package moe.ahao.spring.boot.validator;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.util.commons.io.JSONHelper;
import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Starter.class)
public class ValidatorTest {

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void test1() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String responseString = mockMvc.perform(get("/hello"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();   //将相应的数据转换为字符串
        AjaxDTO result = JSONHelper.parse(responseString, AjaxDTO.class);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AjaxDTO.FAILURE, result.getResult());
        Assertions.assertEquals("校验失败", result.getMsg());

        Object obj = result.getObj();
        Map error = JSONHelper.parse(JSONHelper.toString(obj), Map.class);
        Assertions.assertNotNull(error);
        Assertions.assertEquals(2, error.size());
        Assertions.assertEquals("手机号码不能为空", error.get("phone"));
        Assertions.assertEquals("名称不能为空", error.get("name"));
    }
}
