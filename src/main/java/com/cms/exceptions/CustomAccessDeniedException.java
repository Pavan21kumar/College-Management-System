package com.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomAccessDeniedException extends RuntimeException {
	private String message;

}
