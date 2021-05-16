package moe.ahao.spring.boot.webservice.controller;

import moe.ahao.spring.boot.webservice.entity.GetStudentRequest;
import moe.ahao.spring.boot.webservice.entity.GetStudentResponse;
import moe.ahao.spring.boot.webservice.entity.Student;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class StudentEndpoint {
	public static final String PORT_TYPE_NAME = "StudentPort";
	public static final String URI = "/ws/student"; // /ws 是 MessageDispatcherServlet 决定的
	public static final String NS = "http://ahao.com/spring/boot/webservice/entity"; // 和 xsd 保持一致

	@PayloadRoot(namespace = NS, localPart = "GetStudentRequest")
	@ResponsePayload
	public GetStudentResponse process(@RequestPayload GetStudentRequest request) {
		GetStudentResponse response = new GetStudentResponse();

		Student student = new Student();
		student.setId(request.getId());
		student.setName("Admin"+request.getId());
		student.setAddress("China");

		response.setStudent(student);
		return response;
	}
}
