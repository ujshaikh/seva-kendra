package com.rtcsoft.sevakendra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "com.rtcsoft.sevakendra" })
@SpringBootApplication
public class SevaKendraApplication {

	public static void main(String[] args) {
		SpringApplication.run(SevaKendraApplication.class, args);
	}

}
