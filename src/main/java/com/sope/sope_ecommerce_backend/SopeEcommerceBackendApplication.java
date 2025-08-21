package com.sope.sope_ecommerce_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.sope.sope_ecommerce_backend.client")
public class SopeEcommerceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SopeEcommerceBackendApplication.class, args);
	}

}
