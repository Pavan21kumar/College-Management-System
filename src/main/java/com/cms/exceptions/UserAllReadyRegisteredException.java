package com.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserAllReadyRegisteredException extends RuntimeException {

	private String message;

}
