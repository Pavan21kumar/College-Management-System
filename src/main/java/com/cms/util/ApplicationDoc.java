package com.cms.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition
public class ApplicationDoc {

	Contact contact() {
		return new Contact().name("pavan Kumar").url("https://leetcode.com/").email("budurupavankumar268@gmail.com");
	}

	@Bean
	Info info() {
		return new Info().title("College Management System").description(
				"College Management System  with Spring Security and JWT .RESTFul APIS to perform  CRUD Operation")
				.version("vi").contact(contact());

	}

	@Bean
	OpenAPI openApi() {
		return new OpenAPI().info(info());
	}
}
