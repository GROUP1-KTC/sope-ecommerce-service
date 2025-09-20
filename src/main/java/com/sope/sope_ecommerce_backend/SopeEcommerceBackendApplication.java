package com.sope.sope_ecommerce_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.sope.sope_ecommerce_backend.client")
@EnableScheduling
@EnableCaching
@EnableAsync
public class SopeEcommerceBackendApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		SpringApplication.run(SopeEcommerceBackendApplication.class, args);
	}

}
