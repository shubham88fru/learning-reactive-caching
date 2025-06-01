package com.learning.springreactiveredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringReactiveRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveRedisApplication.class, args);
	}

}
