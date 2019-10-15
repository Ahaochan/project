package com.ahao.spring.cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EurekaProviderApplication {

	public static void main(String[] args) {
	    SpringApplication.run(EurekaProviderApplication.class, args);
	}

}

