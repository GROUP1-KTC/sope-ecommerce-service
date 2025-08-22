package com.sope.sope_ecommerce_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.sope.sope_ecommerce_backend.client")
public class SopeEcommerceBackendApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		SpringApplication.run(SopeEcommerceBackendApplication.class, args);
	}

}
