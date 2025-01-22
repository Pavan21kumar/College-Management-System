package com.cms.requestdto;

import com.cms.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

	@NotNull(message = "Username Should be Enter")
	private String name;
	@NotNull
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Invalid Email")
	private String email;
	@NotNull
	@Size(max = 16, min = 8)
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", message = "give propere password")
	private String password;
	@NotNull
	private Role role;
}
