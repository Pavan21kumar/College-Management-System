package com.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PleaseGiveRefreshAccessTokenRequest extends RuntimeException {

	private String message;
}
