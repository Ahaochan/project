package moe.ahao.spring.boot.webservice.cxf.controller;

import moe.ahao.spring.boot.webservice.cxf.entity.Student;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

import static moe.ahao.spring.boot.webservice.cxf.controller.StudentService.NS;
import static moe.ahao.spring.boot.webservice.cxf.controller.StudentService.SERVICE_NAME;

@Service
@WebService(serviceName = SERVICE_NAME, targetNamespace = NS,
    endpointInterface = "moe.ahao.spring.boot.webservice.cxf.controller.StudentService"
)
public class StudentServiceImpl implements StudentService {
    @Override
    public String echoMsg(String msg) {
        return msg;
    }
    @Override
    public Student getStudent(Long id) {
        return new Student(id, "Admin" + id);
    }
}
