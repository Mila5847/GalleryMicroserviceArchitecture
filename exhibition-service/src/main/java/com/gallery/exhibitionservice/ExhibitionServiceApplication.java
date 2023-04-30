package com.gallery.exhibitionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ExhibitionServiceApplication {

	@Bean
	RestTemplate restTemplate() {return new RestTemplate();}
	public static void main(String[] args) {
		SpringApplication.run(ExhibitionServiceApplication.class, args);
	}

}
