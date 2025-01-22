package com.cms.exceptionshandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cms.exceptions.NoTeachersFoundException;
import com.cms.exceptions.TeacherIdNotFoundException;
import com.cms.util.ErrorStructure;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class TeacherExceptionsHandler {

	private ErrorStructure<String> errorStructure;

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleNoTeachersFoundException(NoTeachersFoundException ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Invalid Details Please choose Correct details  ..."));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleNoTeachersFoundException(TeacherIdNotFoundException ex) {

		return ResponseEntity.badRequest().body(errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value())
				.setMessage(ex.getMessage()).setRootCouse("Invalid Id  ..."));
	}

}
