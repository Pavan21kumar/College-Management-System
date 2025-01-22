package com.cms.jwt;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cms.exceptions.UserIsNotLoginException;
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
public class RefreshFilter extends OncePerRequestFilter {

	private JwtService jwtService;
	private RefreshTokenRepo refreshTokenRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String rt = null;
		System.out.println("inside Jwt Filter..............................");
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("rt"))
					rt = cookie.getValue();
			}
		}
		if (rt == null)
			throw new UserIsNotLoginException("User not logged in | no credentials found");
		if (refreshTokenRepo.existsByTokenAndIsBlocked(rt, true))
			throw new UserIsNotLoginException("Access blocked | try login again");

		String username = jwtService.getUserName(rt);
		String role = jwtService.getRole(rt);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && role != null) {
			System.out.println("setting Authentication.......................");
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null,
					Collections.singleton(new SimpleGrantedAuthority(role)));
			token.setDetails(new WebAuthenticationDetails(request));

			SecurityContextHolder.getContext().setAuthentication(token);
		}
		try {
			System.out.println("end of the Code................");
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, " JWT token Expaired..");

		} catch (JwtException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");

		}
	}

}
