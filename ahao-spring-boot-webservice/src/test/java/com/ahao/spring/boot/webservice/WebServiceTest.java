package com.ahao.spring.boot.webservice;

import com.ahao.spring.boot.Starter;
import com.ahao.spring.boot.webservice.controller.StudentEndpoint;
import com.ahao.spring.boot.webservice.entity.GetStudentResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = Starter.class)
class WebServiceTest {

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5})
    void selectById_Http(int id) throws Exception {
        String param = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
            "                  xmlns:gs=\""+StudentEndpoint.NS+"\">" +
            "   <soapenv:Header/>" +
            "   <soapenv:Body>" +
            "      <gs:GetStudentRequest>" +
            "         <gs:id>"+id+"</gs:id>" +
            "      </gs:GetStudentRequest>" +
            "   </soapenv:Body>" +
            "</soapenv:Envelope>";

        HttpPost post = new HttpPost("http://127.0.0.1:8080"+StudentEndpoint.URI);
        post.setEntity(new StringEntity(param, ContentType.APPLICATION_XML));
        post.setHeader("Content-type", "text/xml; charset=UTF-8");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(post);) {

            HttpEntity entity = response.getEntity();
            String xml = EntityUtils.toString(entity);
            System.out.println(xml);

            GetStudentResponse module = soap2Obj(xml, GetStudentResponse.class);

            Assertions.assertAll("",
                () -> Assertions.assertNotNull(module),
                () -> Assertions.assertEquals(id, module.getStudent().getId()),
                () -> Assertions.assertEquals("Admin" + id, module.getStudent().getName()),
                () -> Assertions.assertEquals("China", module.getStudent().getAddress())
            );


            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        } finally {
            post.releaseConnection();
        }
    }

    private <T> T soap2Obj(String xml, Class clazz) {
        try {
            SOAPMessage message = MessageFactory.newInstance()
                    .createMessage(null, new ByteArrayInputStream(xml.getBytes()));
            Unmarshaller um = JAXBContext.newInstance(clazz)
                    .createUnmarshaller();
            T entity = (T) um.unmarshal(message.getSOAPBody().extractContentAsDocument());
            return entity;
        } catch (IOException | SOAPException | JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
