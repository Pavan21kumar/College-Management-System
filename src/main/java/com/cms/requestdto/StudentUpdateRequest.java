package com.cms.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class StudentUpdateRequest {

	private String name;
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Invalid Email")
	private String email;
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", message = "give propere password")
	private String password;
	private int marks;
	private double grade;
}
