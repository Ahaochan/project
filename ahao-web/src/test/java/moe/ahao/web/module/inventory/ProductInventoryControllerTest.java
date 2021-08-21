package moe.ahao.web.module.inventory;

import moe.ahao.web.module.inventory.config.ProductInventoryConfig;
import moe.ahao.web.module.inventory.controller.ProductInventoryController;
import moe.ahao.web.module.inventory.mapper.ProductInventoryMapper;
import moe.ahao.web.module.inventory.service.impl.ProductInventoryServiceImpl;
import moe.ahao.web.module.inventory.service.impl.RequestAsyncProcessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {ProductInventoryController.class, ProductInventoryConfig.class, ProductInventoryMapper.class,
    RequestAsyncProcessServiceImpl.class, ProductInventoryServiceImpl.class,
    WebMvcAutoConfiguration.class, JacksonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
})
public class ProductInventoryControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void test () throws Exception {
        Integer productId = 1;
        Integer productInventoryCount = 100;
        // 1. 一个写请求过来, 会先删除缓存, 然后处理业务5s
        mockMvc.perform(get("/async/updateProductInventory")
            .param("productId", productId.toString())
            .param("productInventoryCount", productInventoryCount.toString()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(Boolean.TRUE.toString()));

        // 2. 5秒内, 一个读请求过来, 会阻塞到同一个队列, 并hang住200ms
        mockMvc.perform(get("/async/getProductInventoryCount")
            .param("productId", productId.toString()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(""));

        // 3. 等5秒后, 写请求完成了数据库的更新之后, 读请求才会执行
        Thread.sleep(6000);
        mockMvc.perform(get("/async/getProductInventoryCount")
            .param("productId", productId.toString()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(productInventoryCount.toString()));

        // 4. 读请求执行的时候, 会将最新的库存从数据库中查出来, 更新到缓存
    }
}
