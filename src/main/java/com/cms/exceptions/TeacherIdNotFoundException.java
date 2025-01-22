package com.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TeacherIdNotFoundException extends RuntimeException {

	private String message;
}
