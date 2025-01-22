package com.cms.responsedto;

import com.cms.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class AuthResponse {

	
	private String name;
	private String userName;
	private Role userRole;
	private long accessExpiration;
	private long refreshExpiration;

}
