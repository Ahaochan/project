package moe.ahao.web.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import moe.ahao.spring.web.converter.MappingJackson2HttpMessageConverterRegister;
import moe.ahao.util.commons.lang.reflect.ReflectHelper;
import moe.ahao.web.AhaoApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = {AhaoApplication.class, PropertyNamingStrategyControllerTest.TestController.class})
public class PropertyNamingStrategyControllerTest {

    @Autowired
    protected WebApplicationContext wac;

    @Test
    public void test() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Map<PropertyNamingStrategy, MediaType> map = ReflectHelper.getValue(MappingJackson2HttpMessageConverterRegister.class, "strategyMediaTypeMap");

        for (Map.Entry<PropertyNamingStrategy, MediaType> entry : map.entrySet()) {
            PropertyNamingStrategy strategy = entry.getKey();
            MediaType mediaType = entry.getValue();

            ObjectMapper om = new ObjectMapper();
            om.setPropertyNamingStrategy(strategy);

            int testId = 1;
            String testName = "name1";

            String response = mockMvc.perform(post("/test")
                .accept(mediaType)
                .contentType(mediaType)
                .content(om.writeValueAsString(new TestDTO(testId, testName))))
                .andExpect(status().isOk())
                // .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();

            System.out.println(response);
            TestDTO testDTO = om.readValue(response, TestDTO.class);
            Assertions.assertEquals(testId + 100, testDTO.testId);
            Assertions.assertEquals(testName + "!!!", testDTO.testName);
        }
    }


    @RestController
    public static class TestController {
        @PostMapping("/test")
        public TestDTO test(@RequestBody TestDTO dto) {
            return new TestDTO(dto.getTestId() + 100, dto.getTestName() + "!!!");
        }
    }

    public static class TestDTO {
        private Integer testId;
        private String testName;

        public TestDTO() {}

        public TestDTO(Integer testId, String testName) {
            this.testId = testId;
            this.testName = testName;
        }

        public Integer getTestId() {
            return testId;
        }

        public void setTestId(Integer testId) {
            this.testId = testId;
        }

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        @Override
        public String toString() {
            return "TestDTO{" +
                "testId=" + testId +
                ", testName='" + testName + '\'' +
                '}';
        }
    }
}
