package com.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReftreshTokenExpireException extends RuntimeException {

	private String meesage;
}
