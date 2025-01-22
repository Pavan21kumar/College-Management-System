package com.cms.util;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ErrorStructure<T> {

	private int statusCode;
	private String message;
	private T rootCouse;

	public ErrorStructure<T> setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	public ErrorStructure<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public ErrorStructure<T> setRootCouse(T rootCouse) {
		this.rootCouse = rootCouse;
		return this;
	}
}
