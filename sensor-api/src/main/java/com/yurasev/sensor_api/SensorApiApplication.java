package com.yurasev.sensor_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SensorApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SensorApiApplication.class, args);
	}

}
