package com.cms.serviceimpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.entity.AccessToken;
import com.cms.entity.RefreshToken;
import com.cms.entity.User;
import com.cms.exceptions.AccessTokenExpireException;
import com.cms.exceptions.InvalidCreadentialsException;
import com.cms.exceptions.InvalidRoleException;
import com.cms.exceptions.PleaseGiveRefreshAccessTokenRequest;
import com.cms.exceptions.UserAllReadyRegisteredException;
import com.cms.exceptions.UserIsAllreadyLoginException;
import com.cms.exceptions.UserIsNotLoginException;
import com.cms.jwt.JwtService;
import com.cms.repository.AccessTokenRepo;
import com.cms.repository.RefreshTokenRepo;
import com.cms.repository.UserRepository;
import com.cms.requestdto.LoginRequest;
import com.cms.requestdto.UserRequest;
import com.cms.responsedto.AuthResponse;
import com.cms.service.AuthService;
import com.cms.util.ResponseStructure;
import com.cms.util.SimpleResponseStructure;

import jakarta.validation.Valid;

@Service
public class AuthServiceImpl implements AuthService {

	private UserRepository userRepo;
	private SimpleResponseStructure simpleResponseStructure;
	private PasswordEncoder encoder;
	private AuthenticationManager authenticationManager;
	private AccessTokenRepo accessTokenRepo;
	private RefreshTokenRepo refreshTokenRepo;
	private ResponseStructure<AuthResponse> authStructure;
	private JwtService jwtService;
	@Value("${myapp.jwt.access.expairation}")
	private long accessExpairation;
	@Value("${myapp.jwt.refresh.expairation}")
	private long refreshExpairation;

	public AuthServiceImpl(UserRepository userRepo, SimpleResponseStructure simpleResponseStructure,
			PasswordEncoder encoder, AuthenticationManager authenticationManager, AccessTokenRepo accessTokenRepo,
			RefreshTokenRepo refreshTokenRepo, ResponseStructure<AuthResponse> authStructure, JwtService jwtService) {
		super();
		this.userRepo = userRepo;
		this.simpleResponseStructure = simpleResponseStructure;
		this.encoder = encoder;
		this.authenticationManager = authenticationManager;
		this.accessTokenRepo = accessTokenRepo;
		this.refreshTokenRepo = refreshTokenRepo;
		this.authStructure = authStructure;
		this.jwtService = jwtService;
	}

	@Override
	public ResponseEntity<SimpleResponseStructure> register(UserRequest userRequest) {
		// TODO Auto-generated method stub
		if (userRepo.existsByUsername(userRequest.getEmail().split("@")[0])) {
			throw new UserAllReadyRegisteredException("User AllReady Register in Perticuler UserName");
		}
		User user = mapToUSer(userRequest);
		if (userRepo.existsByRole(user.getRole()))
			throw new InvalidRoleException("Allready Admin Is present Change the Role");
		userRepo.save(user);

		return ResponseEntity
				.ok(simpleResponseStructure.setStatusCode(HttpStatus.OK.value()).setMessage("user is added"));
	}

	private String getUserName(String email) {
		return email.split("@")[0];
	}

	private User mapToUSer(UserRequest userRequest) {
		return User.builder().name(userRequest.getName()).role(userRequest.getRole())
				.username(getUserName(userRequest.getEmail())).password(encoder.encode(userRequest.getPassword()))
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> login(LoginRequest loginRequest, String accessToken,
			String refreshToken) {

		if (accessToken != null && refreshToken != null)
			throw new UserIsAllreadyLoginException("You Allready Login....");
		if (accessToken == null && refreshToken != null)
			throw new AccessTokenExpireException("your Accestone expire please Regenerate Your AccessToken");
		String username = loginRequest.getEmailOrUsername().split("@")[0];
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
		if (!authenticate.isAuthenticated())
			throw new InvalidCreadentialsException("please enter valid Credentials....");
		SecurityContextHolder.getContext().setAuthentication(authenticate);

		HttpHeaders headers = new HttpHeaders();
		userRepo.findByUsername(username).ifPresent(user -> {
			AccessToken at = createAccessToken(user, headers);
			RefreshToken rt = createRefreshToken(user, headers);
			at.setUser(user);
			rt.setUser(user);
			at = accessTokenRepo.save(at);
			rt = refreshTokenRepo.save(rt);
		});

		return ResponseEntity.ok().headers(headers)
				.body(authStructure.setStatusCode(HttpStatus.OK.value()).setMessage("succefully login")
						.setBody(mapToAuthResponse(username, accessExpairation, refreshExpairation)));

	}

	private AuthResponse mapToAuthResponse(String username, long accessExpairation, long refreshExpairation) {
		// TODO Auto-generated method stub

		User user = userRepo.findByUsername(username).get();

		return AuthResponse.builder().name(user.getName()).userName(username).accessExpiration(accessExpairation)
				.refreshExpiration(refreshExpairation).userRole(user.getRole()).build();
	}

	private RefreshToken createRefreshToken(User user, HttpHeaders headers) {

		String token = jwtService.generateRefreshToken(user.getUsername(), user.getRole().name());
		headers.add(HttpHeaders.SET_COOKIE, configureCookie("rt", token, refreshExpairation));

		return RefreshToken.builder().token(token).expiration(LocalDateTime.now().plusDays(15)).isBlocked(false)
				.build();

	}

	private AccessToken createAccessToken(User user, HttpHeaders headers) {
		String token = jwtService.generateAccessToken(user.getUsername(), user.getRole().name());
		headers.add(HttpHeaders.SET_COOKIE, configureCookie("at", token, accessExpairation));

		return AccessToken.builder().token(token).expiration(LocalDateTime.now().plusHours(1)).isBlocked(false).build();

	}

	private String configureCookie(String name, String value, long accessExpairation) {
		// TODO Auto-generated method stub
		return ResponseCookie.from(name, value).domain("localhost").path("/").httpOnly(true).secure(false)
				.maxAge(Duration.ofMillis(accessExpairation)).sameSite("Lax").build().toString();

	}

	@Override
	public ResponseEntity<SimpleResponseStructure> logout(String accessToken, String refreshToken) {
		if (accessToken == null && refreshToken == null)
			throw new UserIsNotLoginException("user is not Login");

		if (refreshToken == null)
			throw new UserIsNotLoginException("user is not Login");
		HttpHeaders headers = new HttpHeaders();

		if (accessToken == null)
			throw new PleaseGiveRefreshAccessTokenRequest("give refresh AccessToken Request");
		refreshTokenRepo.findByToken(refreshToken).ifPresent(refresh -> {
			accessTokenRepo.findByToken(accessToken).ifPresent(access -> {

				refresh.setBlocked(true);
				refreshTokenRepo.save(refresh);
				access.setBlocked(true);
				accessTokenRepo.save(access);
			});
		});

		removeAccess("at", headers);
		removeAccess("rt", headers);

		return ResponseEntity.ok().headers(headers)
				.body(simpleResponseStructure.setMessage("LogOut Sucessfully...").setStatusCode(HttpStatus.OK.value()));

	}

	private void removeAccess(String value, HttpHeaders headers) {
		headers.add(HttpHeaders.SET_COOKIE, removeCookie(value));
	}

	private String removeCookie(String name) {
		return ResponseCookie.from(name, "").domain("localhost").path("/").httpOnly(true).secure(false).maxAge(0)
				.sameSite("Lax").build().toString();
	}

	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> refreshToken(String accessToken, String refreshToken) {
		if (accessToken != null) {
			accessTokenRepo.findByToken(accessToken).ifPresent(at -> {

				at.setBlocked(true);
				accessTokenRepo.save(at);
			});
		}
		HttpHeaders headers = new HttpHeaders();
		if (refreshToken == null)
			throw new UserIsNotLoginException("user is not login");
		// check if the token is blocked.
		if (refreshTokenRepo.existsByTokenAndIsBlocked(refreshToken, true))
			throw new UserIsNotLoginException("User Is Not LogedIn...");
		// extract issuedAt from rt
		return refreshTokenRepo.findByToken(refreshToken).map(refresh -> {

			Date date = jwtService.getDate(refresh.getToken());
			LocalDateTime.now();
			if (date.getDate() == new Date().getDate()) {
				AccessToken at = createAccessToken(refresh.getUser(), headers);
				at.setUser(refresh.getUser());
				accessTokenRepo.save(at);
				RefreshToken rt = refreshTokenRepo.findByToken(refreshToken).get();
				headers.add(HttpHeaders.SET_COOKIE, configureCookie("rt", rt.getToken(), refreshExpairation));

			} else {
				refresh.setBlocked(true);
				refreshTokenRepo.save(refresh);
				AccessToken at = createAccessToken(refresh.getUser(), headers);
				RefreshToken rt = createRefreshToken(refresh.getUser(), headers);
				at.setUser(refresh.getUser());
				rt.setUser(refresh.getUser());
				accessTokenRepo.save(at);
				refreshTokenRepo.save(rt);
			}
			return ResponseEntity.ok().headers(headers).body(
					authStructure.setStatusCode(HttpStatus.OK.value()).setMessage("generated AccessToken").setBody(
							mapToAuthResponse(refresh.getUser().getUsername(), accessExpairation, refreshExpairation)));
		}).orElseThrow(() -> new UserIsNotLoginException("user is not Login..."));

	}

}
