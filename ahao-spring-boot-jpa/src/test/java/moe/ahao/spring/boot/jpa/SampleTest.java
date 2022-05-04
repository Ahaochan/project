package moe.ahao.spring.boot.jpa;

import moe.ahao.spring.boot.Starter;
import moe.ahao.transaction.jpa.entity.User;
import moe.ahao.transaction.jpa.entity.UserSpecifications;
import moe.ahao.transaction.jpa.repository.UserJPARepository;
import moe.ahao.util.commons.io.JSONHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
@ActiveProfiles("test")
class SampleTest {

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private EntityManager em;

    @Test
    void testNativeSql() throws Exception {
        int count = userJPARepository.getCount();
        Assertions.assertEquals(5, count);

        Date date = DateUtils.parseDate("2019-11-13 12:00:00", "yyyy-MM-dd HH:mm:ss");
        List<User> list = userJPARepository.findByCreateTimeAfter(date);
        Assertions.assertEquals(2, list.size());
        System.out.println(JSONHelper.toString(list));
    }

    @Test
    void testLikeQuery() {
        List<User> leftLikeList = userJPARepository.getUsersByUsernameLike("user%");
        Assertions.assertEquals(5, leftLikeList.size());

        List<User> allLikeList = userJPARepository.findByNameAllLike("user");
        Assertions.assertEquals(5, allLikeList.size());
    }

    @Test
    void testHql() throws Exception {
        Date start = DateUtils.parseDate("2019-11-12 12:00:00", "yyyy-MM-dd HH:mm:ss");
        Date end = DateUtils.parseDate("2019-11-14 12:00:00", "yyyy-MM-dd HH:mm:ss");
        List<User> between = userJPARepository.findByCreateTimeBetween(start, end);
        Assertions.assertEquals(2, between.size());
    }

    @Test
    @Transactional
    void testInsert() {
        User user1 = new User();
        user1.setUsername("username");
        user1.setPassword("password");
        User user2 = userJPARepository.save(user1);
        System.out.println(user2.getId());
        Assertions.assertNotNull(user2.getId());

        User user3 = userJPARepository.getOne(user2.getId());
        Assertions.assertEquals(user1.getUsername(), user3.getUsername());
        Assertions.assertEquals(user1.getPassword(), user3.getPassword());
    }

    @Test
    void testCriteria() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        Predicate username = builder.like(root.get("username"), "%user%");
        query.where(username);
        List<User> resultList = em.createQuery(query.select(root)).getResultList();
        Assertions.assertEquals(5, resultList.size());
    }

    @Test
    void testSpecification() {
        Specification<User> specification = (Specification<User>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate username = criteriaBuilder.like(root.get("username"), "%user%");
            return username;
        };
        List<User> resultList = userJPARepository.findAll(specification);
        Assertions.assertEquals(5, resultList.size());
    }

    @Test
    void testSpecifications() {
        Specification<User> specification = UserSpecifications.sexIn(1, 2)
            .and(UserSpecifications.usernameAllLike("user"));
        List<User> resultList = userJPARepository.findAll(specification);
        Assertions.assertEquals(5, resultList.size());
    }

    @Test
    void testPageAndSort() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Arrays.asList(
            new Sort.Order(Sort.Direction.ASC, "id"),
            new Sort.Order(Sort.Direction.DESC, "username"))));

        Page<User> page = userJPARepository.findAll((root, criteriaQuery, criteriaBuilder) -> null, pageable);
        Assertions.assertEquals(5, page.getTotalElements());
        Assertions.assertEquals(2, page.getContent().size());
    }
}
