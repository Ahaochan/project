package com.ahao.spring.boot.wechat.mp;

import com.ahao.spring.boot.Starter;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
public class BaseMpTest {

    @Autowired
    protected WxMpService wxMpService;

    protected WxMpMenuService menuService;

    @Before
    public void init() {
        menuService = wxMpService.getMenuService();
    }
}
