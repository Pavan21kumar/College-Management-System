package com.cms.exceptionshandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cms.exceptions.NoStudentsFoundException;
import com.cms.exceptions.StudentNotFoundByIdException;
import com.cms.exceptions.StudentUsernameAllReadyPresentException;
import com.cms.util.ErrorStructure;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class StudentExceptionsHandler {

	private static final Logger logger = LoggerFactory.getLogger(StudentExceptionsHandler.class);

	private ErrorStructure<String> errorStructure;

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleStudentNotFoundByIdException(StudentNotFoundByIdException ex) {
		logger.error("StudentNotFoundByIdException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Invalid Details Please choose Correct details  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleStudentNotFoundByIdException(NoStudentsFoundException ex) {
		logger.error("NoStudentsFoundException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("No Students Found ...."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleStudentUsernameAllReadyPresentException(
			StudentUsernameAllReadyPresentException ex) {
		logger.error("StudentUsernameAllReadyPresentException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Username is Allready present ...."));
	}

}
