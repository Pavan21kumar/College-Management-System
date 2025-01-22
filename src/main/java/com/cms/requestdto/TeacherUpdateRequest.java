package com.cms.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TeacherUpdateRequest {

	private String name;

	@Email(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Invalid Email")
	private String email;

	@Size(max = 16, min = 8)
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", message = "give propere password")
	private String password;

	private String subject;
}
