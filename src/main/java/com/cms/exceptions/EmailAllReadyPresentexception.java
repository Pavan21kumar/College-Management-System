package com.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmailAllReadyPresentexception extends RuntimeException {

	private String message;
}
