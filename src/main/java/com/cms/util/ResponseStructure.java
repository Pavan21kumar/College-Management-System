package com.cms.util;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ResponseStructure<T> {

	private int statusCode;
	private String message;
	private T body;

	public ResponseStructure<T> setStatusCode(int code) {
		this.statusCode = code;
		return this;
	}

	public ResponseStructure<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public ResponseStructure<T> setBody(T body) {
		this.body = body;
		return this;
	}
}
