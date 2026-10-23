package com.api.universidad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//Activar mensajería asíncrona
@EnableAsync 
public class UniversidadApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversidadApplication.class, args);
	}

}
