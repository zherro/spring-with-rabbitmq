package br.com.zherro.rabbitwithspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableAutoConfiguration
@SpringBootApplication
public class RabbitWithSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitWithSpringApplication.class, args);
	}

}
