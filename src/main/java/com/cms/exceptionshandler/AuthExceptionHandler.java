package com.cms.exceptionshandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cms.exceptions.AccessTokenExpireException;
import com.cms.exceptions.CustomAccessDeniedException;
import com.cms.exceptions.EmailAllReadyPresentexception;
import com.cms.exceptions.InvalidCreadentialsException;
import com.cms.exceptions.InvalidRoleException;
import com.cms.exceptions.PasswordNotValidException;
import com.cms.exceptions.PleaseGiveRefreshAccessTokenRequest;
import com.cms.exceptions.ReftreshTokenExpireException;
import com.cms.exceptions.UnauthorizedException;
import com.cms.exceptions.UserAllReadyRegisteredException;
import com.cms.exceptions.UserIsAllreadyLoginException;
import com.cms.exceptions.UserIsNotLoginException;
import com.cms.exceptions.UsernameNotFoundException;
import com.cms.util.ErrorStructure;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class AuthExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(AuthExceptionHandler.class);
	private ErrorStructure<String> errorStructure;

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleAccessTockenExpairedException(AccessTokenExpireException ex) {

		logger.error("AccessTokenExpireException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("AccessTocken Is Expaired Please Refresh The Access Token"));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleRefreshTockenExpairedException(
			ReftreshTokenExpireException ex) {
		logger.error("ReftreshTokenExpireException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("RefreshTocken Is Expaired Please Login Again"));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserIsAllReadyLoginExpairedException(
			UserIsAllreadyLoginException ex) {
		logger.error("UserIsAllreadyLoginException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("User Is Allready Login ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserIsAllReadyRegisterExpairedException(
			UserAllReadyRegisteredException ex) {
		logger.error("UserAllReadyRegisteredException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("User Is Allready Registered. please login ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserNotLoginExpairedException(UserIsNotLoginException ex) {
		logger.error("UserIsNotLoginException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("User Is Not Login ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleInvalidRoleException(InvalidRoleException ex) {
		logger.error("InvalidRoleException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessaString()).setRootCouse("Invalid Role Please choose Correct Role  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleInvalidCredadentialException(InvalidCreadentialsException ex) {
		logger.error("InvalidCreadentialsException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Invalid Details Please choose Correct details  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleEmailAllReadyPresentException(
			EmailAllReadyPresentexception ex) {
		logger.error("EmailAllReadyPresentexception occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Email  is  allready Present  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUnathorizedException(UnauthorizedException ex) {
		logger.error("UnauthorizedException occurred: {}", ex.getMessage());
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Unathorzed for your Role  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
		logger.error("UsernameNotFoundException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest()
				.body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value()).setMessage(ex.getMessage())
						.setRootCouse("Invalid UsernameDetails Please choose Correct details  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlePleaseGiveRefreshAccessTokenRequest(
			PleaseGiveRefreshAccessTokenRequest ex) {
		logger.error("PleaseGiveRefreshAccessTokenRequest occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("AccessToken Expired please Refresh It  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlePasswordNotValidException(PasswordNotValidException ex) {
		logger.error("PasswordNotValidException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Password  Incorrect please  aGive Proper Password  ..."));
	}

//	@ExceptionHandler
//	public ResponseEntity<ErrorStructure<String>> handleAccessTokenExpireException(AccessTokenExpireException ex) {
//		logger.error("AccessTokenExpireException occurred: {}", ex.getMessage(), ex);
//		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
//				.setMessage(ex.getMessage()).setRootCouse("Access   Token IS Expired  please Refresh  The Token..."));
//	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleAccessDeniedException(CustomAccessDeniedException ex) {
		logger.error("AccessDeniedException occurred: {}", ex.getMessage(), ex);

		// Create a structured error response
		ErrorStructure<String> errorStructure = new ErrorStructure<>();
		errorStructure.setStatusCode(HttpStatus.FORBIDDEN.value()).setMessage("Access Denied: " + ex.getMessage())
				.setRootCouse(
						"You do not have permission to access this resource. Please contact admin if this is unexpected.");

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorStructure);
	}

}
