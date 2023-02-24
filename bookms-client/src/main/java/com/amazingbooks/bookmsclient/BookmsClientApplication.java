package com.amazingbooks.bookmsclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix
public class BookmsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmsClientApplication.class, args);
	}

}
