package com.orjujeng.timesheet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
@MapperScan("com.orjujeng.timesheet.mapper")
public class ThTimesheetApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ThTimesheetApiApplication.class, args);
	}
}


