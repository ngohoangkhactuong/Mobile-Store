package com.seo.mobilestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MobileStoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(MobileStoreApplication.class, args);
	}
}
