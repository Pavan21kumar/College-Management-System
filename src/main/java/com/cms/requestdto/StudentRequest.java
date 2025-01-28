package com.cms.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class StudentRequest {

	@NotNull(message = "name is Manditory ")
	private String name;
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Invalid Email")
	@NotNull
	private String email;
	@NotNull
	@Size(min = 8, max = 16)
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", message = "give propere password")
	private String password;

}
