package moe.ahao.spring.boot.webservice.cxf;

import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.webservice.cxf.controller.StudentService;
import moe.ahao.spring.boot.webservice.cxf.entity.Student;
import moe.ahao.util.commons.lang.reflect.ReflectHelper;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = Starter.class)
class WebServiceCxfTest {
    static final String wsdlAddress = "http://127.0.0.1:8080/ahao-cxf/StudentEndpoint?wsdl";

    @Test
    void wsdl() throws Exception {
        HttpGet get = new HttpGet(wsdlAddress);
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(get)) {
            HttpEntity entity = response.getEntity();

            String wsdl = EntityUtils.toString(entity);
            Assertions.assertNotNull(wsdl);
            System.out.println(wsdl);
        }
   }


    @Test
    void call1() {
        // 1. 创建代理工厂
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setAddress(wsdlAddress); // 设置代理地址
        factoryBean.setServiceClass(StudentService.class); // 设置接口类型

        // 2. 创建动态代理
        StudentService ws = (StudentService) factoryBean.create();

        // 3. 断言
        String msg = "Hello World";
        Assertions.assertEquals(msg, ws.echoMsg(msg));
        Student student = ws.getStudent(1L);
        Assertions.assertEquals(1L, student.getId().longValue());
        Assertions.assertEquals("Admin"+1, student.getName());
    }

    @Test
    @Deprecated
    void call2() throws Exception {
        // 1. 创建动态客户端
        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
        Client client = factory.createClient(wsdlAddress);

        // 2. 断言
        String msg = "Hello World";
        Object[] echo = client.invoke("echoMsg", msg);
        Assertions.assertEquals(String.class, echo[0].getClass());
        Assertions.assertEquals(msg, echo[0]);

        Object[] student = client.invoke("getStudent", 1L);
        Assertions.assertNotEquals(Student.class, student[0].getClass());
        Assertions.assertEquals("moe.ahao.cxf.Student", student[0].getClass().getName());
        Assertions.assertEquals(1L, ((Long) ReflectHelper.getValue(student[0], "id")).longValue());
        Assertions.assertEquals("Admin"+1, ReflectHelper.getValue(student[0], "name"));
    }

//    @Test
//    void call3() {
//        // 1. 使用 jdk 自带的 wsimport 工具生成客户端代码
//        // windows: wsimport.exe -encoding utf-8 -p com.ahao.spring.boot.webservice.cxf -keep http://127.0.0.1:8080/ahao-cxf/StudentEndpoint?wsdl -s ./ -B-XautoNameResolution
//        // linux  : wsimport     -encoding utf-8 -p com.ahao.spring.boot.webservice.cxf -keep http://127.0.0.1:8080/ahao-cxf/StudentEndpoint?wsdl -s ./ -B-XautoNameResolution
//
//        // 2. 创建客户端
//        StudentService_Service service = new StudentService_Service();
//
//        // 3. 断言
//        String msg = "Hello World";
//        Assertions.assertEquals(msg, service.getStudentServiceImplPort().echoMsg(msg));
//
//        com.ahao.spring.boot.webservice.cxf.Student student = service.getStudentServiceImplPort().getStudent(1L);
//        Assertions.assertEquals(1L, student.getId().longValue());
//        Assertions.assertEquals("Admin"+1, student.getName());
//    }
}
