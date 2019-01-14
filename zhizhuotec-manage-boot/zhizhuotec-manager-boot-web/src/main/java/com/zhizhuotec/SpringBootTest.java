package com.zhizhuotec;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhizhuotec.mapper")
public class SpringBootTest {
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootTest.class, args);
	}
}
