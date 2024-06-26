package org.django.tennis;

import org.django.tennis.entities.UserEntity;
import org.django.tennis.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TennisApplication {



    public static void main(String[] args) {
		SpringApplication.run(TennisApplication.class, args);
	}




}
