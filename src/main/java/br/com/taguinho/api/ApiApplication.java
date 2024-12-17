package br.com.taguinho.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {

		Dotenv.configure().systemProperties().load();

		SpringApplication.run(ApiApplication.class, args);
	}

}
