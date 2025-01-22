package com.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidRoleException extends RuntimeException {

	private String messaString;
}
