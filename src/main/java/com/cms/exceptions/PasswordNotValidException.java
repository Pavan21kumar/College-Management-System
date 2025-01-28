package com.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordNotValidException extends RuntimeException {

	private String message;
}
