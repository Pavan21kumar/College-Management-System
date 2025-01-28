package com.cms.jwt;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cms.exceptions.AccessTokenExpireException;
import com.cms.exceptions.InvalidCreadentialsException;
import com.cms.exceptions.UserIsNotLoginException;
import com.cms.repository.AccessTokenRepo;
import com.cms.repository.RefreshTokenRepo;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private JwtService jwtService;
	private AccessTokenRepo accessTokenRepo;
	private RefreshTokenRepo refreshTokenRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String at = null;
		String rt = null;
		System.out.println("Inside Jwt Filter...");

		if (request.getCookies() != null) {
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals("at")) {
					at = c.getValue();
				}
				if (c.getName().equals("rt")) {
					rt = c.getValue();
				}
			}
		}

		try {
			if (at == null && rt != null) {
				throw new AccessTokenExpireException("Access Token Is Expired. Please Refresh The Access Token.");
			}

			if (at != null && rt != null) {
				System.out.println("Filter is executing...");
				if (accessTokenRepo.existsByTokenAndIsBlocked(at, true)
						&& refreshTokenRepo.existsByTokenAndIsBlocked(rt, true)) {
					throw new InvalidCreadentialsException("Invalid Credentials. Please enter correct details.");
				}

				System.out.println("Inside... Both tokens are present.");
				String userName = jwtService.getUserName(at);
				String role = jwtService.getRole(at);
				if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null
						&& role != null) {
					System.out.println("Setting Authentication...");
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, null,
							Collections.singleton(new SimpleGrantedAuthority(role)));
					token.setDetails(new WebAuthenticationDetails(request));
					SecurityContextHolder.getContext().setAuthentication(token);
				}
			}

			filterChain.doFilter(request, response);

		} catch (AccessTokenExpireException ex) {
			handleException(response, HttpStatus.UNAUTHORIZED, ex.getMessage(),
					"Token expired. Please login or refresh.");
		} catch (InvalidCreadentialsException ex) {
			handleException(response, HttpStatus.UNAUTHORIZED, ex.getMessage(), "Invalid credentials.");
		} catch (ExpiredJwtException ex) {
			handleException(response, HttpStatus.UNAUTHORIZED, "JWT token expired.", ex.getMessage());
		} catch (JwtException ex) {
			handleException(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token.", ex.getMessage());
		}
	}

	private void handleException(HttpServletResponse response, HttpStatus status, String message, String rootCause)
			throws IOException {
		response.setStatus(status.value());
		response.setContentType("application/json");
		response.getWriter().write(String.format("{\"statusCode\":%d,\"message\":\"%s\",\"rootCause\":\"%s\"}",
				status.value(), message, rootCause));
	}
}
