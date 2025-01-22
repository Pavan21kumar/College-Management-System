package com.cms.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsernameNotFoundException extends RuntimeException {

	private String message;
}
