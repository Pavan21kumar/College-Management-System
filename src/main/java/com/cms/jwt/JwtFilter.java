package com.cms.jwt;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cms.exceptions.InvalidCreadentialsException;
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

	@Override // FilterChain is Belongs to Servlet FilterChain
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String at = null;
		String rt = null;
		System.out.println("inside Jwt Filter..............................");
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
		if (at != null && rt != null) {
			System.out.println("fileter is executing...");
			if (accessTokenRepo.existsByTokenAndIsBlocked(at, true)
					&& refreshTokenRepo.existsByTokenAndIsBlocked(rt, true))
				throw new InvalidCreadentialsException("invalid Creadentials  please enter Correct Details....");

			System.out.println("inside.................. Both Present....");
			String userName = jwtService.getUserName(at);
			String role = jwtService.getRole(at);
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null && role != null) {
				System.out.println("setting Authentication.......................");
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, null,
						Collections.singleton(new SimpleGrantedAuthority(role)));
				token.setDetails(new WebAuthenticationDetails(request));

				SecurityContextHolder.getContext().setAuthentication(token);

			}

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
