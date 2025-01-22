package com.cms.exceptionshandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cms.exceptions.AccessTokenExpireException;
import com.cms.exceptions.EmailAllReadyPresentexception;
import com.cms.exceptions.InvalidCreadentialsException;
import com.cms.exceptions.InvalidRoleException;
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

	private ErrorStructure<String> errorStructure;

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleAccessTockenExpairedException(AccessTokenExpireException ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("AccessTocken Is Expaired Please Refresh The Access Token"));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleRefreshTockenExpairedException(
			ReftreshTokenExpireException ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("RefreshTocken Is Expaired Please Login Again"));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserIsAllReadyLoginExpairedException(
			UserIsAllreadyLoginException ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("User Is Allready Login ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserIsAllReadyRegisterExpairedException(
			UserAllReadyRegisteredException ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("User Is Allready Registered. please login ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserNotLoginExpairedException(UserIsNotLoginException ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("User Is Not Login ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleInvalidRoleException(InvalidRoleException ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessaString()).setRootCouse("Invalid Role Please choose Correct Role  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleInvalidCredadentialException(InvalidCreadentialsException ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Invalid Details Please choose Correct details  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleEmailAllReadyPresentException(
			EmailAllReadyPresentexception ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Email  is  allready Present  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUnathorizedException(UnauthorizedException ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Unathorzed for your Role  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {

		return ResponseEntity.badRequest()
				.body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value()).setMessage(ex.getMessage())
						.setRootCouse("Invalid UsernameDetails Please choose Correct details  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlePleaseGiveRefreshAccessTokenRequest(
			PleaseGiveRefreshAccessTokenRequest ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("AccessToken Expired please Refresh It  ..."));
	}

}
