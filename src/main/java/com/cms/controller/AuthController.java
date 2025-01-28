package com.cms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cms.requestdto.LoginRequest;
import com.cms.requestdto.UserRequest;
import com.cms.responsedto.AuthResponse;
import com.cms.service.AuthService;
import com.cms.util.ResponseStructure;
import com.cms.util.SimpleResponseStructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // Logger instance

	private AuthService authService;

	@Operation(summary = "Register a new user", description = "This endpoint is used to register a new user by providing user details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(schema = @Schema(implementation = SimpleResponseStructure.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = SimpleResponseStructure.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = SimpleResponseStructure.class))) })
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/user/register")
	public ResponseEntity<SimpleResponseStructure> rerister(@Valid @RequestBody UserRequest userRequest) {
		logger.info("Received request to register a new user: {}", userRequest);
		try {
			ResponseEntity<SimpleResponseStructure> response = authService.register(userRequest);
			logger.info("User registration successful: {}", userRequest.getEmail().split("@")[0]);
			return response;
		} catch (Exception ex) {
			logger.error("Error occurred during user registration: {}", ex.getMessage(), ex);
			throw ex;
		}
	}

	@Operation(summary = "User login", description = "This endpoint is used for user login. It accepts login credentials and generates access and refresh tokens.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = ResponseStructure.class))),
			@ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ResponseStructure.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseStructure.class))) })
	@PostMapping("/user/login")
	public ResponseEntity<ResponseStructure<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest,
			@CookieValue(name = "at", required = false) String accessToken,
			@CookieValue(name = "rt", required = false) String refreshToken) {
		logger.info("Received login request for username: {}", loginRequest.getEmailOrUsername().split("@")[0]);

		ResponseEntity<ResponseStructure<AuthResponse>> response = authService.login(loginRequest, accessToken,
				refreshToken);
		logger.info("Login successful for username: {}", loginRequest.getEmailOrUsername().split("@")[0]);
		return response;

	}

	@Operation(summary = "User logout", description = "This endpoint is used to log out a user by invalidating access and refresh tokens.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Logout successful", content = @Content(schema = @Schema(implementation = SimpleResponseStructure.class))),
			@ApiResponse(responseCode = "400", description = "Invalid tokens", content = @Content(schema = @Schema(implementation = SimpleResponseStructure.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = SimpleResponseStructure.class))) })
	@PostMapping("/user/logout")
	public ResponseEntity<SimpleResponseStructure> logout(
			@CookieValue(name = "at", required = false) String accessToken,
			@CookieValue(name = "rt", required = false) String refreshToken) {
		logger.info("Received logout request");
		try {
			ResponseEntity<SimpleResponseStructure> response = authService.logout(accessToken, refreshToken);
			logger.info("Logout successful");
			return response;
		} catch (Exception ex) {
			logger.error("Error occurred during logout: {}", ex.getMessage(), ex);
			throw ex;
		}
	}

	@Operation(summary = "Refresh access token", description = "This endpoint generates a new access token using the refresh token.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(schema = @Schema(implementation = ResponseStructure.class))),
			@ApiResponse(responseCode = "400", description = "Invalid refresh token", content = @Content(schema = @Schema(implementation = ResponseStructure.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseStructure.class))) })
	@PostMapping("/login/refresh")
	public ResponseEntity<ResponseStructure<AuthResponse>> refreshToken(
			@CookieValue(name = "at", required = false) String accessToken,
			@CookieValue(name = "rt", required = false) String refreshToken) {
		logger.info("Received request to refresh access token");
		try {
			ResponseEntity<ResponseStructure<AuthResponse>> response = authService.refreshToken(accessToken,
					refreshToken);
			logger.info("Token refreshed successfully");
			return response;
		} catch (Exception ex) {
			logger.error("Error occurred during token refresh: {}", ex.getMessage(), ex);
			throw ex;
		}
	}
}