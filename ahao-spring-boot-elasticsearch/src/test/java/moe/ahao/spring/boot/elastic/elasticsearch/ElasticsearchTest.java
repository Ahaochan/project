package moe.ahao.spring.boot.elastic.elasticsearch;

import moe.ahao.spring.boot.Starter;
import moe.ahao.util.commons.io.JSONHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
class ElasticsearchTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @BeforeEach
    void beforeEach() {
        // 创建索引
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(EsUserDO.class);
        Assertions.assertFalse(indexOperations.exists());
        Assertions.assertTrue(indexOperations.create());
        Assertions.assertTrue(indexOperations.exists());
    }

    @AfterEach
    void afterEach() {
        // 删除索引
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of("es_user"));
        Assertions.assertTrue(indexOperations.delete());
        Assertions.assertFalse(indexOperations.exists());
    }

    @Test
    void save() {
        EsUserDO user1 = new EsUserDO("es_id_1", 1L, "user1");
        EsUserDO response1 = elasticsearchRestTemplate.save(user1);
        System.out.println("写入user1, 自定义es的id, " + user1);
        System.out.println("写入user1, 自定义es的id, " + response1);
        Assertions.assertEquals(user1.getEsId(), response1.getEsId());
        Assertions.assertEquals(user1.getDbId(), response1.getDbId());
        Assertions.assertEquals(user1.getUsername(), response1.getUsername());

        EsUserDO user2 = new EsUserDO("es_id_2", 2L, "user2");
        IndexQuery indexQuery2 = new IndexQueryBuilder().withObject(user2).build();
        String esId2 = elasticsearchRestTemplate.index(indexQuery2, elasticsearchRestTemplate.getIndexCoordinatesFor(EsUserDO.class));
        System.out.println("写入user2, 自定义es的id, " + user2);
        System.out.println("写入user2, 自定义es的id, " + esId2);
        Assertions.assertNotNull(esId2);
        Assertions.assertEquals(user2.getEsId(), esId2);

        EsUserDO user3 = new EsUserDO(null, 3L, "user3");
        IndexQuery indexQuery3 = new IndexQueryBuilder().withObject(user3).build();
        String esId3 = elasticsearchRestTemplate.index(indexQuery3, elasticsearchRestTemplate.getIndexCoordinatesFor(EsUserDO.class));
        System.out.println("写入user3, 使用es自己生成的id, " + user3);
        System.out.println("写入user3, 使用es自己生成的id, " + esId3);
        Assertions.assertNotNull(esId3);
        Assertions.assertEquals(user3.getEsId(), esId3); // 回写id
    }

    @Test
    void saveByForLoop() {
        List<EsUserDO> list = Arrays.asList(
            new EsUserDO("es_id_1", 1L, "user1"),
            new EsUserDO("es_id_2", 2L, "user2"),
            new EsUserDO("es_id_3", 3L, "user3")
        );
        Iterable<EsUserDO> responseList = elasticsearchRestTemplate.save(list); // for循环插入
        Assertions.assertIterableEquals(list, responseList);
    }

    @Test
    void saveBulk() {
        List<EsUserDO> list = Arrays.asList(
            new EsUserDO("es_id_1", 1L, "user1"),
            new EsUserDO("es_id_2", 2L, "user2"),
            new EsUserDO("es_id_3", 3L, "user3")
        );
        List<IndexQuery> indexQueryList = list.stream().map(u -> new IndexQueryBuilder().withObject(u).build()).collect(Collectors.toList());
        List<IndexedObjectInformation> responseList = elasticsearchRestTemplate.bulkIndex(indexQueryList, BulkOptions.defaultOptions(), EsUserDO.class);
        for (int i = 0; i < responseList.size(); i++) {
            IndexedObjectInformation response = responseList.get(i);
            System.out.println("批量插入响应:" + JSONHelper.toString(response));

            String esId = response.getId();
            Assertions.assertEquals(list.get(i).getEsId(), esId);
        }
    }

    @Test
    void delete() throws Exception {
        List<EsUserDO> list = Arrays.asList(
            new EsUserDO("es_id_1", 1L, "user1"),
            new EsUserDO("es_id_2", 2L, "user2"),
            new EsUserDO("es_id_3", 3L, "user3")
        );
        Iterable<EsUserDO> responseList = elasticsearchRestTemplate.save(list); // for循环插入

        Thread.sleep(1000); // 和注解配置有关, @Setting(refreshInterval = "1s")
        Query query = new NativeSearchQueryBuilder().build();
        List<EsUserDO> searchList1 = elasticsearchRestTemplate.search(query, EsUserDO.class).get()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
        System.out.println("查询出结果:" + JSONHelper.toString(searchList1));
        Assertions.assertIterableEquals(list, searchList1);

        for (EsUserDO userDO : searchList1) {
            System.out.println("删除数据, esId:" + userDO.getEsId());
            elasticsearchRestTemplate.delete(userDO.getEsId(), EsUserDO.class);
        }

        Thread.sleep(1000); // 和注解配置有关, @Setting(refreshInterval = "1s")
        List<EsUserDO> searchList2 = elasticsearchRestTemplate.search(query, EsUserDO.class).get()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
        System.out.println("查询出结果:" + JSONHelper.toString(searchList2));
        Assertions.assertEquals(0, searchList2.size());
    }
}
