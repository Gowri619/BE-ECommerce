package com.gowri.commerceflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CommerceFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceFlowApplication.class, args);
	}

}
