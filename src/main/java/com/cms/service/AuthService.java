package com.cms.service;

import org.springframework.http.ResponseEntity;

import com.cms.requestdto.LoginRequest;
import com.cms.requestdto.UserRequest;
import com.cms.responsedto.AuthResponse;
import com.cms.util.ResponseStructure;
import com.cms.util.SimpleResponseStructure;

import jakarta.validation.Valid;

public interface AuthService {

	ResponseEntity<SimpleResponseStructure> register(UserRequest userRequest);

	ResponseEntity<ResponseStructure<AuthResponse>> login(@Valid LoginRequest longinREquest, String accessToken,
			String refreshToken);

	ResponseEntity<SimpleResponseStructure> logout(String accessToken, String refreshToken);

	ResponseEntity<ResponseStructure<AuthResponse>> refreshToken(String accessToken, String refreshToken);

	
}
