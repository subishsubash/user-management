package com.subash.user.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the User Management Spring Boot application.
 *
 * <p>
 * This application provides RESTful APIs for user registration, retrieval,
 * and deletion, with built-in security and logging.
 * </p>
 *
 */
@SpringBootApplication
public class UserManagementApplication {

	/**
	 * Main method to launch the Spring Boot application.
	 *
	 * @param args Command-line arguments (if any)
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}

}
