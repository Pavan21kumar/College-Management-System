package com.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoTeachersFoundException extends RuntimeException {

	private String message;
}
