package moe.ahao.spring.boot.mongdb;

import lombok.Data;
import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class MongoOperationsTest {
    @RegisterExtension
    static MongoDBExtension mongoDBExtension = new MongoDBExtension();

    @Autowired
    private MongoOperations operations;

    @BeforeEach
    void beforeEach() {
        int size = 100;
        for (int i = 0; i < size; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("Ahao" + i);
            operations.save(user);
        }
        Assertions.assertEquals(size, operations.findAll(User.class).size());
    }

    @AfterEach
    void tearDown() {
        operations.remove(new Query(), User.class);
        Assertions.assertEquals(0, operations.findAll(User.class).size());
    }


    @Test
    void findByUserName() {
        String query = "Ahao1";
        User user = operations.findOne(query(where("username").is(query)), User.class);
        System.out.println(user);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(query, user.getUsername());
    }

    @Data
    @Document(collection = "user")
    public static class User {
        @Id // 主键id
        @Indexed
        private Long id;

        //
        @Indexed(name = "idx_username", direction = IndexDirection.ASCENDING, background = true)
        private String username;

        @Field("expire_time")
        private Date expireTime;
    }
}
