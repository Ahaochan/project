package com.ahao.spring.boot.webservice.cxf.config;

import com.ahao.spring.boot.webservice.cxf.controller.StudentService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CxfConfig {
    @Bean
    public Endpoint endpoint(Bus bus, StudentService studentService) {
        EndpointImpl endpoint = new EndpointImpl(bus, studentService);
        endpoint.publish("/StudentEndpoint");
        return endpoint;
    }
}
