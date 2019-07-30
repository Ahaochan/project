package moe.ahao.spring.boot.cache;

import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
@ActiveProfiles("simple")
class CacheTest {

    @Autowired
    private TestCacheService service;

    @Test
    void test() {
        String id = UUID.randomUUID().toString();

        String realData1 = service.execute(id);
        System.out.println("第1次访问, 得到真实数据: " + realData1);
        Assertions.assertNotNull(realData1);

        String cacheData2 = service.execute(id);
        System.out.println("第2次访问, 得到缓存数据: " + cacheData2);
        Assertions.assertEquals(realData1, cacheData2);

        String realData3 = service.execute(UUID.randomUUID().toString());
        System.out.println("第3次访问, 查询新的数据: " + realData3);
        Assertions.assertNotEquals(realData1, realData3);

        String realData4 = service.doExecute(id);
        System.out.println("第4次访问, 模拟缓存失效, 得到真实数据: " + realData4);
        Assertions.assertNotEquals(realData1, realData4);

        String realData5 = service.updateCache(id);
        System.out.println("第5次访问, 更新缓存, 得到真实数据: " + realData5);
        Assertions.assertNotNull(realData5);

        String cacheData6 = service.execute(id);
        System.out.println("第6次访问, 得到更新后的缓存数据: " + cacheData6);
        Assertions.assertEquals(realData5, cacheData6);

        service.removeCache(id);
        System.out.println("删除缓存: " + id);
    }

}
