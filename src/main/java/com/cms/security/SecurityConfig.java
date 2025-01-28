package com.cms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cms.jwt.JwtFilter;
import com.cms.jwt.JwtService;
import com.cms.jwt.RefreshFilter;
import com.cms.repository.AccessTokenRepo;
import com.cms.repository.RefreshTokenRepo;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

	private CustomeUserDetailservice userDetailservice;
	private AccessTokenRepo accessTokenRepo;
	private RefreshTokenRepo refreshTokenRepo;
	private JwtService jwtService;
	private CustomAuthenticationFailureHandler authenticationFailureHandler;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailservice);
		return provider;
	}

	@Bean
	@Order(2)
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()) // Disable CSRF protection
				.authorizeHttpRequests(auth -> auth.requestMatchers("/user/register", "/user/login", "/swagger-ui/**", // Swagger
																														// UI
																														// resources
						"/swagger-ui.html", // Swagger HTML
						"/v3/api-docs/**" // OpenAPI docs
				).permitAll() // Allow Swagger without authentication
						.anyRequest().authenticated() // Protect other endpoints
				).exceptionHandling(
						exception -> exception.authenticationEntryPoint((request, response, authException) -> {
							// Handle unauthenticated access (401)
							response.setStatus(HttpStatus.UNAUTHORIZED.value());
							response.setContentType("application/json");
							response.getWriter().write(
									"{\"statusCode\":401,\"message\":\"Authentication Required: Please log in.\",\"rootCouse\":\"Login is required to access this resource.\"}");
						}).accessDeniedHandler((request, response, accessDeniedException) -> {
							// Handle forbidden access (403)
							response.setStatus(HttpStatus.FORBIDDEN.value());
							response.setContentType("application/json");
							response.getWriter().write(
									"{\"statusCode\":403,\"message\":\"Access Denied: You are not authorized to access this resource.\",\"rootCouse\":\"Insufficient permissions.\"}");
						}))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new JwtFilter(jwtService, accessTokenRepo, refreshTokenRepo),
						UsernamePasswordAuthenticationFilter.class) // Add JWT filter
				.authenticationProvider(authenticationProvider()).build();
	}

	@Bean
	@Order(1)
	SecurityFilterChain refreshFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.securityMatchers(matcher -> matcher.requestMatchers("/login/refresh/**"))
				.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new RefreshFilter(jwtService, refreshTokenRepo),
						UsernamePasswordAuthenticationFilter.class)
				.authenticationProvider(authenticationProvider()).build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}