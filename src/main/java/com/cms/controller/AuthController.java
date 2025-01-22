package com.cms.controller;

import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {

	private AuthService authService;

	@PostMapping("/user/register")
	public ResponseEntity<SimpleResponseStructure> rerister(@Valid @RequestBody UserRequest userRequest) {
		return authService.register(userRequest);
	}

	@PostMapping("/user/login")
	public ResponseEntity<ResponseStructure<AuthResponse>> login(@Valid @RequestBody LoginRequest longinREquest,
			@CookieValue(name = "at", required = false) String accessToken,
			@CookieValue(name = "at", required = false) String refreshToken) {
		return authService.login(longinREquest, accessToken, refreshToken);
	}

	@PostMapping("/user/logout")
	public ResponseEntity<SimpleResponseStructure> logout(
			@CookieValue(name = "at", required = false) String accessToken,
			@CookieValue(name = "rt", required = false) String refreshToken) {
		return authService.logout(accessToken, refreshToken);
	}

	@PostMapping("/login/refresh")
	public ResponseEntity<ResponseStructure<AuthResponse>> refreshToken(
			@CookieValue(name = "at", required = false) String accessToken,
			@CookieValue(name = "rt", required = false) String refreshToken) {
		return authService.refreshToken(accessToken, refreshToken);

	}
}
