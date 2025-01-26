package com.jeanbarcellos.project111;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Project111Application {

	public static void main(String[] args) {
		SpringApplication.run(Project111Application.class, args);
	}

}
