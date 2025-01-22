package com.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentNotFoundByIdException extends RuntimeException {

	private String message;
}
