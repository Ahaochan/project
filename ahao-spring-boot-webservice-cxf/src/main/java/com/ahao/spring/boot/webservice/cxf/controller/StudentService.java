package com.ahao.spring.boot.webservice.cxf.controller;

import com.ahao.spring.boot.webservice.cxf.entity.Student;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import static com.ahao.spring.boot.webservice.cxf.controller.StudentService.NS;
import static com.ahao.spring.boot.webservice.cxf.controller.StudentService.SERVICE_NAME;

@WebService(name  = SERVICE_NAME, targetNamespace = NS)
public interface StudentService {
    String SERVICE_NAME = "StudentService";
    String NS = "http://cxf.ahao.moe/";

    @WebMethod
    String echoMsg(@WebParam(name = "msg") String msg);

    @WebMethod
    Student getStudent(@WebParam(name = "id") Long id);
}
